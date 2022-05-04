package tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class DemoWebShopHWTest {

    @Test
    void addToWishListTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("addtocart_18.EnteredQuantity=1&addtocart_19.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/18/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is(notNullValue()));


                                        /*
        HttpResponse<String> response = Unirest.post("http://demowebshop.tricentis.com/addproducttocart/details/18/2")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Cookie", "__utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utmc=78382081; __utma=78382081.1290585258.1649697010.1649781141.1649840397.6; nop.CompareProducts=CompareProductIds=72; __utmt=1; Nop.customer=89b53566-8280-47f9-b18a-16154e8afbbf; NOPCOMMERCE.AUTH=12A4E188C14469950B8F7AC7883609F52818C233974F051886EFDCD56E67B8B7F201E1AB3FC8E8417BC3DAE9BB97DEC333248CE7C970AC5FC302B75F625DE109DD0129A98E107642D4C2D19A62F516053F3E76CCCE59C0BFF1387CCD7AE00E4F68AE85AA8269D133AF927CC6925C32162B9884E67C2F2134D565008BABD89EC9AA2FFBD5AF2C47D989651727B3E9E53913C812CB79848009F2805BF058318C65; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=17&RecentlyViewedProductIds=72&RecentlyViewedProductIds=75&RecentlyViewedProductIds=74; __atuvc=16%7C15; __atuvs=625691282b2420cc005; __utmb=78382081.24.10.1649840397; ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; Nop.customer=89b53566-8280-47f9-b18a-16154e8afbbf")
                .body("addtocart_18.EnteredQuantity=1&addtocart_19.EnteredQuantity=1")
                .asString();


        "success": true,
                "message": "The product has been added to your <a href=\"/wishlist\">wishlist</a>",
                "updatetopwishlistsectionhtml": "(15)"

                                         */
    }


    @Test
    void authorizationTest() {
        String authorizationCookie =

                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", "mol@gmail.com")
                        .formParam("Password", "123123")
                        .when()
                        .post("http://demowebshop.tricentis.com/login")
                        .then()
                        .log().all()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        open("http://demowebshop.tricentis.com/customer/info"); // открываем сайт для установки куки

        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)); // устанавливаем куки

       open("http://demowebshop.tricentis.com/customer/info"); // глазами смотрим что авторизация прошла
      //  sleep(5000);

        $(".account").shouldHave(text("mol@gmail"));

         /*
         "Cookie", "__utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none);
         ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utmc=78382081; __utma=78382081.1290585258.1649697010.1649781141.1649840397.6;
         nop.CompareProducts=CompareProductIds=72; __RequestVerificationToken=3AYpOc8N99BOVcxc-jsk5LRZePiRmuPYR3Ar_lpv7HPTQ5259Zgro6TG7yxX-S7t4p_jo9HB3mOK6OiT8q7HWJtpLYRIF51d0J8rGoenT3o1;
         ASP.NET_SessionId=mcr0zwwnaw1o1b3t0hwya3cu;
         NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=17&RecentlyViewedProductIds=75&RecentlyViewedProductIds=74; __utmt=1;
         NOPCOMMERCE.AUTH=449CA6A93FA75568AA0EFE1307C3B4AC483CD9FA7EC47851E73792C4A2E4C749C08DC7D102211C8D294DB3496453BEDA28B2ED88D25045469CC609AF7F5201CA73823514661CACF3759519F508272B87943C01057BDF335CA134F7DE9CEF28E6F78DCEE17B0B89D0006123D3589CFE9357E23E1DC76C8018FE82D906F9B58BE5;
         Nop.customer=353f21bb-9085-4a20-ab2c-57aafa867350; __atuvc=24%7C15; __atuvs=62569b8314c1cf24007; __utmb=78382081.83.10.1649840397;
         ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687;
         Nop.customer=353f21bb-9085-4a20-ab2c-57aafa867350; nop.CompareProducts=CompareProductIds=72")
          */


    }
}

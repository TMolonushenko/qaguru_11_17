package tests;

import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class demoWebShopTests {
    @Test
    void addToCartAsNewUserTest() {


        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(1)"));
        //      assertThat(response.extract().path("updatetopcartsectionhtml").toString())
        //    .body("updatetopcartsectionhtml", is("(28)"));

    }

    @Test
    void addToCartWhithCokkieTest() {
        Integer carSize = 0;

        ValidatableResponse response =

                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie("Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36; ") /* +
                                "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utma=78382081.1290585258.1649697010.1649697010.1649697010.1; __utmc=78382081; __utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); " +
                                "NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __atuvc=4%7C15; __atuvs=6254613d8c9f5185003; __utmt=1; __utmb=78382081.5.10.1649697010; " +
                                "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; " +
                                "Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36")*/
                        .body("product_attribute_72_5_18=53" +
                                "&product_attribute_72_6_19=54" +
                                "&product_attribute_72_3_20=57" +
                                "&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your  " +
                                "<a href=\"/cart\">shopping cart</a>"));

        //      assertThat(response.extract().path("updatetopcartsectionhtml").toString())
        //    .body("updatetopcartsectionhtml", is("(28)"));

    }


    /*
    Unirest.setTimeouts(0, 0);
HttpResponse<String> response = Unirest.post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
  .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
  .header("Cookie", "Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36; ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utma=78382081.1290585258.1649697010.1649697010.1649697010.1; __utmc=78382081; __utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __atuvc=4%7C15; __atuvs=6254613d8c9f5185003; __utmt=1; __utmb=78382081.5.10.1649697010; ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36")
  .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
  .asString();


     */

    @Test
    void addToCartTest() {
        String response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie("Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36;")
                        .body("product_attribute_72_5_18=53" +
                                "&product_attribute_72_6_19=54" +
                                "&product_attribute_72_3_20=57" +
                                "&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .extract().response().asString();
        System.out.println(response);
    }
}

### REST API

добавляется еще один промежутоный слой между браузером и сервером, т.е. мы делаем все тоже самое в обход браузера.
Имитируем сбросы которые идут от браузера сервер. и при разработке сложных тестов мы часть взаимоотношений с продуктом
переносим на более легкий и более дешевый слой.

Есть ситуации когда при запуске тестов мы ошиблись в URLe и чтобы этого избежать при запуске в дженкинс в сборке в
разделе http request мы указываем парамметр $(REMOTE_DRIVER_URL)
В таком случае если сразу обнаружена ошибка даже gradle не будет запускаться и не будут тратиться ресурсы.

статус коды надо знать)
информационные 100-199

успешные 200-299

перенаправления 300-399

клиентские ошибки 400-499

серверные ошибки 500-599

создаем build.gradle для паралелизации добавляем код

```java

if(System.getProperty("threads")!=null){
        systemProperties+=[
        'junit.jupiter.execution.parallel.enabled':true,
        'junit.jupiter.execution.parallel.mode.default':'concurrent',
        'junit.jupiter.execution.parallel.mode.classes.default':'concurrent',
        'junit.jupiter.execution.parallel.config.strategy':'fixed',
        'junit.jupiter.execution.parallel.config.fixed.parallelism':System.getProperty("threads").toInteger()
        ]
```

добавляем папку tests и создам SelenoidTests будем делать одно и тоже разными способами

надежные сайты для серча baeldung.com и stackoverflow.com и mkyong.com

для получения джейсона можно в командной строке сделать запрос curl сайт

классический подход к rest Assured для json это

```java
given()
        .config(RestAssured.config()jsonConfig(jsonConfig().numberReturnType(BIG-DECIMAL)))
        .when()
        .get("/price")
        .then()
        .body("price",is(new BigDecimal(12.12)); 
```

классическая структура тестов это

arrange-act-assert это шаблон для форматирования Unit тестов. Обозначающий разделения теста на 3 части

Arrange - все необходимые подготовки и входные данные Act - собственно вызов того метода который вы тестируете Assert -
проверки, что метод делает то что надо В ваших пример со списком должно быть что-то такое:

```java@Test
public void testAdditionAndSize() {
// Arrange - подготавливаем данные для теста
SomeClass obj = new SomeClass;

    // Act - выполняем метод add
    list.add(obj);

    // Assert - проверяем, что объект добавился
    assertEquals(1, list.size());
    assertEquals(obj, list.get(1));
    // другие тесты проверяющие что добавился правильный объект, что он не изменился и т.д.
}
```

либо given-when-then Given — первоначальный контекст (предусловие)
When — событие (что является триггером сценария)
Then — результат, который мы хотим получить

в самом простом понимании это Дано-когда-тогда

для того чтобы это все работало добавляем в build.gradle Rest Assured
"io.rest-assured:rest-assured:$restAssuredVersion"

```java
 @Test
    void checkTotal(){
            given()
            .when()
            .get("https://selenoid.autotests.cloud/status")
            .then()
            .body("total",is(20));
 ```

импортируе is - org.hamcrest.Matchers.is

в данном случаем в given и when ничего нет и мы можем их просто убрать

```java
 @Test
    void checkTotalWithoutGiven(){
            get("https://selenoid.autotests.cloud/status")
            .then()
            .body("total",is(20));
            }
```

для удобного чтения json в хроме надо включить плагин JSONVue json с которым мы работаем

```java
{
        "total":20,
        "used":0,
        "queued":0,
        "pending":0,
        "browsers":{
        "chrome":{
        "100.0":{},
        "99.0":{}
        },
        "firefox":{
        "97.0":{},
        "98.0":{}
        },
        "opera":{
        "84.0":{},
        "85.0":{}
        }
        }
        }
```

далее мы хотим проверить версию браузера.

```java
 @Test
    void checkChromeVersion(){
            get("https://selenoid.autotests.cloud/status")
            .then()
            .body("browsers.chrome",hasKey("100.0"));
            }
```

далее пример как делать плохо. Результат ответа сохраняем в переменную response, и мы хотим в качесве ответа забрать все
содержимое extract().response().asString(); далее создаем expectedResponse в нее вставляем весь полученный ответ и
проводим проверку. В итоге получается непонятная каша. Так лучше не работать, надо либо убрать это в текстовый файл,
либо создать модель.

```java
 @Test
    void checkTotalBadPractice(){
            String response=get("https://selenoid.autotests.cloud/status")
            .then()
            .extract().response().asString();

            System.out.println("Response: "+response);

            String expectedResponse="{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,"+
            "\"browsers\":"+
            "{\"chrome\":{\"100.0\":{},\"99.0\":{}},"+
            "\"firefox\":{\"97.0\":{},\"98.0\":{}},"+
            "\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";
            assertEquals(expectedResponse,response);
            }

```

теперь делаем хорошую проверку. Сразу достаем то поле которое проверяем .extract().path("total)
тип ответа бует не string, а integer. присваиваем integer expectedResponse 20(потому что мы как раз хотели проверить что
тотал = 20)
а проверку оставляем прежней

```java
 @Test
    void checkTotalGoodPractice(){
            int response=get("https://selenoid.autotests.cloud/status")
            .then()
            .extract()
            .path("total");

            System.out.println("Response: "+response);

            int expectedResponse=20;
            assertEquals(expectedResponse,response);
            }
```

далее заменяем тип объекта на Response, затем если мы выводим +response выходит что-то непонятное, тоже самое если
выводим +response.toString. Для того чтобы мы увидели вразумительный ответ необходимо вывести +response.asString()
результаты разного вывода на экран

```java
Response:io.restassured.internal.RestAssuredResponseImpl@6682b659
        Response.toString():io.restassured.internal.RestAssuredResponseImpl@6682b659
        Response.asString():{"total":20,"used":0,"queued":0,"pending":0,"browsers":{"chrome":{"100.0":{},"99.0":{}},"firefox":{"97.0":{},"98.0":{}},"opera":{"84.0":{},"85.0":{}}}}

        Response.path("total"):20
        Response.path("browsers.chrome"):{100.0={},99.0={}}

```

селеноид у нас запаролен. если мы откроем наш адресс без авторизации то мы увидим ошибку 401 это значит что все плохо.
следующем тесте у нас немного поменялся адрес. в предыдущих тестах стутус код был 200. для того чтобы зайти запароленным
мы в начале пути указываем наш логин/пароль.
"https://user1:1234@selenoid.autotests.cloud/wd/hub/status"

```java
 @Test
    void checkStatus401(){
            get("https://selenoid.autotests.cloud/wd/hub/status")
            .then()
            .statusCode(401);
            }

@Test
    void checkStatus200(){
            get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
            .then()
            .statusCode(200);
            }
```

авторизацию можно сделать и покрасивее

```java

@Test
    void checkStatus200WithAuth(){
            given()
            .auth().basic("user1","1234")
            .get("https://selenoid.autotests.cloud/wd/hub/status")
            .then()
            .statusCode(200);
            }
```

### reqres.in

это сайт тренажер для api. в нем есть разные endpoint, куда можно обраться для отработки разных запросов. он возвразает
массив данных, например список пользователей. важно! в селеиде есть простые запросы типа $(""").clic()  и мы не
разделяем на типы запросов, а в  
Rest Api есть разные типы запросов

*GET — получение ресурса/просто взять

*POST — создание ресурса/дать и забрать(логин/пароль)

*PUT — обновление ресурса/

*DELETE — удаление ресурса

Создаем простой тест на авторизацию ReqresinTest

```java
 @Test
    void successfulLogin(){
        /*
        request: https://reqres.in/api/login
        data:
        {
            "email": "eve.holt@reqres.in",
            "password": "cityslicka"
        }
        response:
        {
            "token": "QpwL5tke4Pnpja7X4"
        }
         */

            String authorizedData="{\"email\": \"eve.holt@reqres.in\", "+
            "\"password\": \"cityslicka\"}";

            given()
            .body(authorizedData)
            .contentType(JSON)
            .when()
            .post("https://reqres.in/api/login")
            .then()
            .statusCode(200)
            .body("token",is("QpwL5tke4Pnpja7X4"));
            }
```

важно! Указать contentType(json)

Далее пишем тест на негативную авторизацию

```java
 @Test
    void missingPasswordLogin(){
        /*
        request: https://reqres.in/api/login
        data:
        {
            "email": "eve.holt@reqres.in"
        }
        response:
        {
            "error": "Missing password"
        }
         */

            String authorizedData="{\"email\": \"eve.holt@reqres.in\"}";

            given()
            .body(authorizedData)
            .contentType(JSON)
            .when()
            .post("https://reqres.in/api/login")
            .then()
            .statusCode(400)
            .body("error",is("Missing password"));
            }
```

#### REST API №2

ТРЕНАЖЕР DEMOWEBSHOP.TRICENTIS.COM

добавляем товар в корзину, пробуем подделать этот запрос под api. сделать его на более низком уровне чтобы можно было
добавлять товар в корзину при этом совсем не трогая браузер и ничего не кликая.

в браузере открываем http://demowebshop.tricentis.com/ открываем код, жмем вкладку network там должно быть пусто, если
там заполнено очищаем жмя на крестик. там можно увидеть все детали страницы в разобранном виде. все иконки, все кнопки и
т.д. и браузер(фронтэнд) он из всего этого формирует приложение. жмем на add to cart и во встроенном в хром перехватчике
запросов появляется какая-то гифка загрузчика какойто запрос(который скорее всего нам и нужен) и какаято картинка на
закрытие окна(в норме такие картинки приходить не должны)
у них не очень хорошо сделан сайт. выделяем запрос и во вкладке headers видим какой-то url, видим метод post, status
code 200.

этот запрос попробуем перенести в автотест

если на запросе нажать правой кнопкой мышки и скопировать для bash в командной строке можно вставить и посмотреть что из
себя представляет этот запрос. открываем терминал, вставляем и смотрим при каждом новом запуске можно увидеть как
увеличивается число товаров в корзине, браузер для этого не нужен совсем. в нашем запросе где-то передаются данные
авторизации.

во вкладке network есть фильтры, мы можем выбрать fetch/XHR и мы будем видеть только запросы, но не всегда срабатывает.

для простых задач этого достаточно, но при увеличении количества запросов может понадобиться сторонний анализатор
трафика.

Charles, fiddler это инструменты которые в этом помогут. они могут имитировать трафик, это незаменимая вещь в мобильной
автоматизации. он так же помогает

Для композинга мы будем использовать Postman берем нашу ссылку на запрос для bash из networka, идем в postman file->
import->raw text и вставляем делаем импорт выбираем вкладку herders делаем send видим что товары снова добавились в
корзину теперь поиграемся с параметрами чтобы понять минимум который нам необходим, потому что каждый параметр это
дополнительная строчка кода, а возможно она нам и не нужна. по одному убираем галочки и снова запускаем, смотрим
добавляются ли товары в корзину.

в итоге нас остается contentType и cookie. здесь можно сгенерировать автотесты, но они не айс.

в идее создаем demoWebShopTests переносим данные из postman по строчкам берем данные из автогенератора в постман

```java
Unirest.setTimeouts(0,0);
        HttpResponse<String> 
        response=Unirest.post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .header("Cookie","__utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); "+
                "Nop.customer=56c84236-b920-47ff-8d55-1e18c6da298d; "+
                "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utma=78382081.1290585258.1649697010.1651587403.1651591257.12; __utmc=78382081; __utmt=1; "+
                "NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __atuvc=32%7C15%2C1%7C16%2C0%7C17%2C1%7C18; __atuvs=6271485dcfd79d8c000; __utmb=78382081.2.10.1651591257; "+
                "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; "+
                "Nop.customer=56c84236-b920-47ff-8d55-1e18c6da298d")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                .asString();

```

и переносим его в наш код

```java
 @Test
    void addToCartTest(){
            String response=
            given()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .cookie("ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utma=78382081.1290585258.1649697010.1649697010.1649697010.1; __utmc=78382081; __utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); "+
            "NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __atuvc=4%7C15; __atuvs=6254613d8c9f5185003; __utmt=1; __utmb=78382081.5.10.1649697010; "+
            "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; "+
            "Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36")
            .body("product_attribute_72_5_18=53"+
            "&product_attribute_72_6_19=54"+
            "&product_attribute_72_3_20=57"+
            "&addtocart_72.EnteredQuantity=1")
            .when()
            .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
            .then()
            .extract().response().asString();
            System.out.println(response);
            }

```

лишние куки можно убрать

```java
@Test
    void addToCartTest(){
            String response=
            given()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .cookie("Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36;")
            .body("product_attribute_72_5_18=53"+
            "&product_attribute_72_6_19=54"+
            "&product_attribute_72_3_20=57"+
            "&addtocart_72.EnteredQuantity=1")
            .when()
            .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
            .then()
            .extract().response().asString();
            System.out.println(response);
            }
```

получаем вот такой ответ

```java

{"success":true,"message":"The product has been added to your \u003ca "+
        "href=\"/cart\"\u003eshopping cart\u003c/a\u003e",
        "updatetopcartsectionhtml":"(8)",
        "updateflyoutcartsectionhtml":"\u003cdiv id=\"flyout-cart\" "+
        "class=\"flyout-cart\"\u003e\r\n   "+
        " \u003cdiv class=\"mini-shopping-cart\"\u003e\r\n        "+
        "\u003cdiv class=\"count\"\u003e\r\nThere are \u003ca href=\"/cart\" "+
        "class=\"items\"\u003e8 item(s)\u003c/a\u003e in your cart.        "+
        "\u003c/div\u003e\r\n            "+
        "\u003cdiv class=\"items\"\u003e\r\n                    "+
        "\u003cdiv class=\"item first\"\u003e\r\n                           "+
        " \u003cdiv class=\"picture\"\u003e\r\n                               "+
        " \u003ca href=\"/build-your-cheap-own-computer\" "+
        "title=\"Show details for Build your own cheap computer\"\u003e\r\n                                   "+
        " \u003cimg alt=\""+
        "Picture of Build your own cheap computer\" src=\"http://demowebshop.tricentis.com/content/images/thumbs/0000172_build-your-own-cheap-computer_47.jpeg\" "+
        "title=\"Show details for Build your own cheap computer\" "+
        "/\u003e\r\n                                "+
        "\u003c/a\u003e\r\n                            "+
        "\u003c/div\u003e\r\n                       "+
        " \u003cdiv class=\"product\"\u003e\r\n                            "+
        "\u003cdiv class=\"name\"\u003e\r\n                                "+
        "\u003ca href=\"/build-your-cheap-own-computer\""+
        "\u003eBuild your own cheap computer\u003c/a\u003e\r\n                           "+
        " \u003c/div\u003e\r\n                                "+
        "\u003cdiv class=\"attributes\"\u003e\r\n                                    "+
        "Processor: Medium [+15.00]\u003cbr /\u003eRAM:"+
        " 2 GB\u003cbr /\u003eHDD: 320 GB\r\n                                "+
        "\u003c/div\u003e\r\n                            "+
        "\u003cdiv class=\"price\"\u003eUnit price: \u003cspan\u003e815.00\u003c/span\u003e\u003c/div\u003e\r\n                            "+
        "\u003cdiv class=\"quantity\"\u003eQuantity: "+
        "\u003cspan\u003e8\u003c/span\u003e\u003c/div\u003e\r\n                        "+
        "\u003c/div\u003e\r\n                    "+
        "\u003c/div\u003e\r\n            "+
        "\u003c/div\u003e\r\n            "+
        "\u003cdiv class=\"totals\"\u003eSub-Total: \u003cstrong\u003e6520.00\u003c/strong\u003e\u003c/div\u003e\r\n            "+
        "\u003cdiv class=\"buttons\"\u003e\r\n                    "+
        "\u003cinput type=\"button\" value=\"Go to cart\" class=\"button-1 cart-button\" onclick=\"setLocation(\u0027/cart\u0027)\" /\u003e\r\n                            "+
        "\u003c/div\u003e\r\n    "+
        "\u003c/div\u003e\r\n\u003c/div\u003e\r\n"}
        demoWebShopTests>addToCartTest()STARTED
```

берем данные из ответа и добавлЯем проверки и добавляем логи

````java
@Test
    void addToCartAsNewUserTest(){


            given()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .body("product_attribute_72_5_18=53"+
            "&product_attribute_72_6_19=54"+
            "&product_attribute_72_3_20=57"+
            "&addtocart_72.EnteredQuantity=1")
            .when()
            .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
            .then()
            .log().all()
            .statusCode(200)
            .body("success",is(true))
            .body("message",is("The product has been added to your "+
            "<a href=\"/cart\">shopping cart</a>"))
            .body("updatetopcartsectionhtml",is("(1)"));
            //      assertThat(response.extract().path("updatetopcartsectionhtml").toString())
            //    .body("updatetopcartsectionhtml", is("(28)"));

            }
````

для того чтобы правильно срабатывал счетчик товаров в корзине

```` @Test
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
````

в конце этой лекции есть код по авторизации лекция №18

#### REST API. ПОДКЛЮЧАЕМ ALLURE

ОТКРЫВАЕМ demoqa.com заходим во вкладку Book Store Application и мы видим магазин, максимально простой и максимально
примитивный. есть форма логина, есть профиль пользователя не доступный без авторизации. и есть book store api. это
swagger - автодокументация на rest api, на те интерфейсы которые у нас есть. Бэкенд сам по себе похож на то что мы
делаем в автотестах. тоже какие-то методы, какие-то анннотации через @ грубо говоря тоже самое только запускаюся не
тесты а какие-то сервисы на которые можно стучаться на которые тесты можно написать. У бэкенд разработчиков есть
механизм который позволяет посторить документацию на все существующие интерфейсы и эндпоинты. т.е. адреса куда мы
стучимся. в бекэнде при добавлеии определенных аннотаций и библиотек можно сделать так чтобы на лету генерилась
отчетность и документация. в которой видно весь функционал можно сделать так что даже до того как тесты впервые будут
запущены есть но уже есть список всех тестов проекте и их специфика название

на demoqa.com есть некий микросервис где swagger добавлен и мы по сути видим типы запроса и ендпоинты куда он
обращается, а так же детали запроса. обычно ручные тестировщики берут контент из swageer и оформляют его в postman

Создаем новый BookStoreTests пишем в нем @BeforeAll

```java
 @BeforeAll
static void beforeAll(){
        RestAssured.baseURI="https://demoqa.com/";
        }
```

пишем простой тест. проверяем что внутри books не нулевое количество элементов

```java
 @Test
    void getBooksTest(){
            get("BookStore/v1/Books")
            .then()
            .body("books",hasSize(greaterThan(0)));
            }
 ```

попробуем расширить логирование

```java
    @Test
    void getBooksWithAllLogsTest(){
            given()
            .log().all()
            .when()
            .get("BookStore/v1/Books")
            .then()
            .log().all()
            .body("books",hasSize(greaterThan(0)));
            }

```

в итоге у нас выводяться и донные запроса и данные ответа

```java
Request method:GET
        Request URI:https://demoqa.com/BookStore/v1/Books
        Proxy:<none>
Request params:<none>
Query params:<none>
Form params:<none>
Path params:<none>
Headers:Accept=*/*
Cookies:		<none>
Multiparts:		<none>
Body:			<none>
HTTP/1.1 200 OK
Server: nginx/1.17.10 (Ubuntu)
Date: Fri, 06 May 2022 18:54:19 GMT
Content-Type: application/json; charset=utf-8
Content-Length: 4514
Connection: keep-alive
X-Powered-By: Express
ETag: W/"11a2-8zfX++QwcgaCjSU6F8JP9fUd1tY"

{
    "books": [
        {
            "isbn": "9781449325862",
            "title": "Git Pocket Guide",
            "subTitle": "A Working Introduction",
            "author": "Richard E. Silverman",
            "publish_date": "2020-06-04T08:48:39.000Z",
            "publisher": "O'Reilly Media",
            "pages": 234,
            "description": "This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp",
            "website": "http://chimera.labs.oreilly.com/books/1230000000561/index.html"
        },
        {
            "isbn": "9781449331818",
            "title": "Learning JavaScript Design Patterns",
            "subTitle": "A JavaScript and jQuery Developer's Guide",
            "author": "Addy Osmani",
            "publish_date": "2020-06-04T09:11:40.000Z",
            "publisher": "O'Reilly Media",
            "pages": 254,
            "description": "With Learning JavaScript Design Patterns, you'll learn how to write beautiful, structured, and maintainable JavaScript by applying classical and modern design patterns to the language. If you want to keep your code efficient, more manageable, and up-to-da",
            "website": "http://www.addyosmani.com/resources/essentialjsdesignpatterns/book/"
        },
        {
            "isbn": "9781449337711",
            "title": "Designing Evolvable Web APIs with ASP.NET",
            "subTitle": "Harnessing the Power of the Web",
            "author": "Glenn Block et al.",
            "publish_date": "2020-06-04T09:12:43.000Z",
            "publisher": "O'Reilly Media",
            "pages": 238,
            "description": "Design and build Web APIs for a broad range of clients—including browsers and mobile devices—that can adapt to change over time. This practical, hands-on guide takes you through the theory and tools you need to build evolvable HTTP services with Microsoft",
            "website": "http://chimera.labs.oreilly.com/books/1234000001708/index.html"
        },
        {
            "isbn": "9781449365035",
            "title": "Speaking JavaScript",
            "subTitle": "An In-Depth Guide for Programmers",
            "author": "Axel Rauschmayer",
            "publish_date": "2014-02-01T00:00:00.000Z",
            "publisher": "O'Reilly Media",
            "pages": 460,
            "description": "Like it or not, JavaScript is everywhere these days-from browser to server to mobile-and now you, too, need to learn the language or dive deeper than you have. This concise book guides you into and through JavaScript, written by a veteran programmer who o",
            "website": "http://speakingjs.com/"
        },
        {
            "isbn": "9781491904244",
            "title": "You Don't Know JS",
            "subTitle": "ES6 & Beyond",
            "author": "Kyle Simpson",
            "publish_date": "2015-12-27T00:00:00.000Z",
            "publisher": "O'Reilly Media",
            "pages": 278,
            "description": "No matter how much experience you have with JavaScript, odds are you don’t fully understand the language. As part of the \\\"You Don’t Know JS\\\" series, this compact guide focuses on new features available in ECMAScript 6 (ES6), the latest version of the st",
            "website": "https://github.com/getify/You-Dont-Know-JS/tree/master/es6%20&%20beyond"
        },
        {
            "isbn": "9781491950296",
            "title": "Programming JavaScript Applications",
            "subTitle": "Robust Web Architecture with Node, HTML5, and Modern JS Libraries",
            "author": "Eric Elliott",
            "publish_date": "2014-07-01T00:00:00.000Z",
            "publisher": "O'Reilly Media",
            "pages": 254,
            "description": "Take advantage of JavaScript's power to build robust web-scale or enterprise applications that are easy to extend and maintain. By applying the design patterns outlined in this practical book, experienced JavaScript developers will learn how to write flex",
            "website": "http://chimera.labs.oreilly.com/books/1234000000262/index.html"
        },
        {
            "isbn": "9781593275846",
            "title": "Eloquent JavaScript, Second Edition",
            "subTitle": "A Modern Introduction to Programming",
            "author": "Marijn Haverbeke",
            "publish_date": "2014-12-14T00:00:00.000Z",
            "publisher": "No Starch Press",
            "pages": 472,
            "description": "JavaScript lies at the heart of almost every modern web application, from social apps to the newest browser-based games. Though simple for beginners to pick up and play with, JavaScript is a flexible, complex language that you can use to build full-scale ",
            "website": "http://eloquentjavascript.net/"
        },
        {
            "isbn": "9781593277574",
            "title": "Understanding ECMAScript 6",
            "subTitle": "The Definitive Guide for JavaScript Developers",
            "author": "Nicholas C. Zakas",
            "publish_date": "2016-09-03T00:00:00.000Z",
            "publisher": "No Starch Press",
            "pages": 352,
            "description": "ECMAScript 6 represents the biggest update to the core of JavaScript in the history of the language. In Understanding ECMAScript 6, expert developer Nicholas C. Zakas provides a complete guide to the object types, syntax, and other exciting changes that E",
            "website": "https://leanpub.com/understandinges6/read"
        }
    ]
}
BookStoreTests > getBooksWithAllLogsTest() STARTED
```

если мы не хотим чтобы выводилось все то можно убрать второе логирование, тогда выйдет только логирование запроса.

либо можно вывести только body и status

```java
@Test
    void getBooksWithSomeLogsTest(){
            given()
            .log().uri()
            .log().body()
            .when()
            .get("BookStore/v1/Books")
            .then()
            .log().status()
            .log().body()
            .body("books",hasSize(greaterThan(0)));
            }
```

тут выведен лог в request:uri, body в response: status, body

```java
Request URI:https://demoqa.com/BookStore/v1/Books
        Body:<none>
HTTP/1.1 200OK
        {
        "books":[
        {
        "isbn":"9781449325862",
        "title":"Git Pocket Guide",
        "subTitle":"A Working Introduction",
        "author":"Richard E. Silverman",
        "publish_date":"2020-06-04T08:48:39.000Z",
        "publisher":"O'Reilly Media",
        "pages":234,
        "description":"This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp",
        "website":"http://chimera.labs.oreilly.com/books/1230000000561/index.html"
        },
        {
        "isbn":"9781449331818",
        "title":"Learning JavaScript Design Patterns",
        "subTitle":"A JavaScript and jQuery Developer's Guide",
        "author":"Addy Osmani",
        "publish_date":"2020-06-04T09:11:40.000Z",
        "publisher":"O'Reilly Media",
        "pages":254,
        "description":"With Learning JavaScript Design Patterns, you'll learn how to write beautiful, structured, and maintainable JavaScript by applying classical and modern design patterns to the language. If you want to keep your code efficient, more manageable, and up-to-da",
        "website":"http://www.addyosmani.com/resources/essentialjsdesignpatterns/book/"
        },
        {
        "isbn":"9781449337711",
        "title":"Designing Evolvable Web APIs with ASP.NET",
        "subTitle":"Harnessing the Power of the Web",
        "author":"Glenn Block et al.",
        "publish_date":"2020-06-04T09:12:43.000Z",
        "publisher":"O'Reilly Media",
        "pages":238,
        "description":"Design and build Web APIs for a broad range of clients—including browsers and mobile devices—that can adapt to change over time. This practical, hands-on guide takes you through the theory and tools you need to build evolvable HTTP services with Microsoft",
        "website":"http://chimera.labs.oreilly.com/books/1234000001708/index.html"
        },
        {
        "isbn":"9781449365035",
        "title":"Speaking JavaScript",
        "subTitle":"An In-Depth Guide for Programmers",
        "author":"Axel Rauschmayer",
        "publish_date":"2014-02-01T00:00:00.000Z",
        "publisher":"O'Reilly Media",
        "pages":460,
        "description":"Like it or not, JavaScript is everywhere these days-from browser to server to mobile-and now you, too, need to learn the language or dive deeper than you have. This concise book guides you into and through JavaScript, written by a veteran programmer who o",
        "website":"http://speakingjs.com/"
        },
        {
        "isbn":"9781491904244",
        "title":"You Don't Know JS",
        "subTitle":"ES6 & Beyond",
        "author":"Kyle Simpson",
        "publish_date":"2015-12-27T00:00:00.000Z",
        "publisher":"O'Reilly Media",
        "pages":278,
        "description":"No matter how much experience you have with JavaScript, odds are you don’t fully understand the language. As part of the \\\"You Don’t Know JS\\\" series, this compact guide focuses on new features available in ECMAScript 6 (ES6), the latest version of the st",
        "website":"https://github.com/getify/You-Dont-Know-JS/tree/master/es6%20&%20beyond"
        },
        {
        "isbn":"9781491950296",
        "title":"Programming JavaScript Applications",
        "subTitle":"Robust Web Architecture with Node, HTML5, and Modern JS Libraries",
        "author":"Eric Elliott",
        "publish_date":"2014-07-01T00:00:00.000Z",
        "publisher":"O'Reilly Media",
        "pages":254,
        "description":"Take advantage of JavaScript's power to build robust web-scale or enterprise applications that are easy to extend and maintain. By applying the design patterns outlined in this practical book, experienced JavaScript developers will learn how to write flex",
        "website":"http://chimera.labs.oreilly.com/books/1234000000262/index.html"
        },
        {
        "isbn":"9781593275846",
        "title":"Eloquent JavaScript, Second Edition",
        "subTitle":"A Modern Introduction to Programming",
        "author":"Marijn Haverbeke",
        "publish_date":"2014-12-14T00:00:00.000Z",
        "publisher":"No Starch Press",
        "pages":472,
        "description":"JavaScript lies at the heart of almost every modern web application, from social apps to the newest browser-based games. Though simple for beginners to pick up and play with, JavaScript is a flexible, complex language that you can use to build full-scale ",
        "website":"http://eloquentjavascript.net/"
        },
        {
        "isbn":"9781593277574",
        "title":"Understanding ECMAScript 6",
        "subTitle":"The Definitive Guide for JavaScript Developers",
        "author":"Nicholas C. Zakas",
        "publish_date":"2016-09-03T00:00:00.000Z",
        "publisher":"No Starch Press",
        "pages":352,
        "description":"ECMAScript 6 represents the biggest update to the core of JavaScript in the history of the language. In Understanding ECMAScript 6, expert developer Nicholas C. Zakas provides a complete guide to the object types, syntax, and other exciting changes that E",
        "website":"https://leanpub.com/understandinges6/read"
        }
        ]
        }
        BookStoreTests>getBooksWithSomeLogsTest()STARTED
        BUILD SUCCESSFUL in 7s
        3actionable tasks:1executed,2up-to-date
        22:03:41:Execution finished':test --tests "tests.BookStoreTests.getBooksWithSomeLogsTest"'.

```

пишем тест с авторизацией и проверкой токена проверкой статуса и result не забываем прописать contetnType

```java
 @Test
    void generateTokenTest(){
            String data="{ \"userName\": \"Alex\", "+
            "\"password\": \"asdsad#frew_DFS2\" }";

            given()
            .body(data)
            .contentType(JSON)
            .log().uri()
            .log().body()
            .when()
            .post("Account/v1/GenerateToken")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .body("status",is("Success"))
            .body("result",is("User authorized successfully."))
            .body("token.size()",(greaterThan(10)));
            }
```

в дальнейшем data надо будет убрать в модельдля того чтобы подключить allurу идем в build.gradle и добавляем библиотеки

```java

plugins{
        id'java-library'
        id"io.qameta.allure"version"2.9.6"

        def restAssuredVersion="5.0.1",
        allureVersion="2.17.3"

        allure{
        adapter{
        aspectjWeaver.set(true)
        frameworks{
        junit5{
        adapterVersion.set(allureVersion)
        }
        }
        }
        report{
        version.set(allureVersion)
        }


        "io.rest-assured:rest-assured:$restAssuredVersion"
        "io.qameta.allure:allure-rest-assured:$allureVersion",
```
для подключения пишем строчку
RestAssured.filters(new AllureRestAssured());

и прописываем фильтр

```java

 @Test
    void generateTokenWithAllureListenerTest() {
        String data = "{ \"userName\": \"Alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";

        // RestAssured.filters(new AllureRestAssured()); //можно перенести в @BeforeAll

        given()
                .filters(new AllureRestAssured())//это лог именно в алюр отчет
                .body(data)
                .contentType(JSON)
                .log().uri()//эти логи обеспечивают логирование к консоль
                .log().body()
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }
```
за счет того что мы добавили библитечку теперь в отчете аалюр у нас есть вывод body и headers
задать так чтобы в аллюр отчет выводилось только body нельзя
логи типа .log().all() выводят логирование именно в консоль
а  .filters(new AllureRestAssured()) именно в отчет аллюра

мы можем добавить более красивый отчет в аллюр
создаем пакет listener и вв нем создаем CustomAllureListener 
```java
public class CustomAllureListener {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    public static AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");

        return FILTER;
    }
}
```
здесь есть пара файлов request.ftl и response.ftl их неодходимо создатью
в resources мы создаем папку tpl а в этой папке создаем 
request.ftl
```java
<html>
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<head>
<meta http-equiv="content-type" content="text/html; charset = UTF-8">
    <script src="https://yastatic.net/jquery/2.2.3/jquery.min.js" crossorigin="anonymous"></script>

    <link href="https://yastatic.net/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <script src="https://yastatic.net/bootstrap/3.3.6/js/bootstrap.min.js" crossorigin="anonymous"></script>

    <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/bash.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/json.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/xml.min.js"></script>
    <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

    <style>
        pre {
white-space: pre-wrap;
}
    </style>
</head>
<body>
<div>
    <pre><code><#if data.method??>${data.method}<#else>GET</#if>: <#if data.url??>${data.url}<#else>Unknown</#if></code></pre>
</div>

<#if data.body??>
    <h4>Body</h4>
    <div>
        <pre><code>${data.body}</code></pre>
    </div>
</#if>

<#if (data.headers)?has_content>
    <h4>Headers</h4>
    <div>
        <#list data.headers as name, value>
            <div>
                <pre><code><b>${name}</b>: ${value}</code></pre>
            </div>
        </#list>
    </div>
</#if>


<#if (data.cookies)?has_content>
    <h4>Cookies</h4>
    <div>
        <#list data.cookies as name, value>
            <div>
                <pre><code><b>${name}</b>: ${value}</code></pre>
            </div>
        </#list>
    </div>
</#if>

<#if data.curl??>
    <h4>Curl</h4>
    <div>
        <pre><code>${data.curl}</code></pre>
    </div>
</#if>
</body>
</html>
```
и response.ftl
```java
<html>
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<head>
<meta http-equiv="content-type" content="text/html; charset = UTF-8">
    <script src="https://yastatic.net/jquery/2.2.3/jquery.min.js" crossorigin="anonymous"></script>

    <link href="https://yastatic.net/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <script src="https://yastatic.net/bootstrap/3.3.6/js/bootstrap.min.js" crossorigin="anonymous"></script>

    <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/bash.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/json.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/xml.min.js"></script>
    <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

    <style>
        pre {
white-space: pre-wrap;
}
    </style>
</head>
<body>
<div><h4>Status code</h4> <#if data.responseCode??>
        <pre><code><b>${data.responseCode}</b></code></pre>
    <#else>Unknown</#if></div>
<#if data.url??>

    <div>
    <pre><code>${data.url}</code></pre>
    </div></#if>

<#if (data.headers)?has_content>
    <h4>Headers</h4>
    <div>
        <#list data.headers as name, value>
            <div>
                <pre><code><b>${name}</b>: ${value}</code></pre>
            </div>
        </#list>
    </div>
</#if>

<#if data.body??>
    <h4>Body</h4>
    <div>
        <pre><code>${data.body}</code></pre>
    </div>
</#if>

<#if (data.cookies)?has_content>
    <h4>Cookies</h4>
    <div>
        <#list data.cookies as name, value>
            <div>
                <pre><code><b>${name}</b>: ${value}</code></pre>
            </div>
        </#list>
    </div>
</#if>
</body>
</html>
```

это просто работающий вариант надо точно так же копипастом это создать у себя

меняем фильтры

```java
 @Test
    void generateTokenWithCustomAllureListenerTest() {
        String data = "{ \"userName\": \"Alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";


        given()
                .filters(withCustomTemplates())//это лог именно в алюр отчет
                .body(data)
                .contentType(JSON)
                .log().uri()//эти логи обеспечивают логирование к консоль
                .log().body()
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }
```
это нам дало красоту в аллюр отчете, теперь все в рамочках и выделенно цветом

для того чтобы извлечь токен создаем стрингу токен и добавляем extract

```java

 @Test
    void generateTokenJsonSchemeCheckTest() {
        String data = "{ \"userName\": \"Alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";


        given()
                .filters(withCustomTemplates())//это лог именно в алюр отчет
                .body(data)
                .contentType(JSON)
                .log().uri()//эти логи обеспечивают логирование к консоль
                .log().body()
                .when()
                .post("Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }
```

это нужно для того чтобы токен можно было использовать в дальнейшем.

добавим тест на проверку соответсвия джейсон схеме

есть разные браузерные приложения для того чтобы сгенерить джейсон схему
например https://jsonschema.net/
мы можем просто взять ответ из теста и вставить туда можно получить структурированную джейсон схему

забираем схему из автогенератора
создаем в resources папку schemas
создаем файл GenerateToken_response_scheme.json и в него вставляем эту схему
и в тест добавляем проверку на эту схему. проверку ставим в начало, т.к. предусловиеэто проверка не пройдена нет смыслави
проводить остальные

в build.gradle добавляем ещ зависимость

```java

"io.rest-assured:json-schema-validator:$restAssuredVersion",
```
в тесте в боди прописываем
.body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))


```java
    @Test
    void generateTokenJsonSchemeCheckTest() {
            String data = "{ \"userName\": \"Alex\", " +
            "\"password\": \"asdsad#frew_DFS2\" }";


            given()
            .filters(withCustomTemplates())//это лог именно в алюр отчет
            .body(data)
            .contentType(JSON)
            .log().uri()//эти логи обеспечивают логирование к консоль
            .log().body()
            .when()
            .post("Account/v1/GenerateToken")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
            .body("status", is("Success"))
            .body("result", is("User authorized successfully."))
            .body("token.size()", (greaterThan(10)));
            }
```

теперь попробуем убрать данные в модель.
создаем пакет  models
и в нем создадим новый класс Credentials
приватно создаем юзер/пароль, а публично методы которые их используют
создаем get и set/ но по итогу в этом конкертом тесте get мы не используем и можем убрать

```java
package models;

public class Credentials {
    /*{
        "userName": "Alex",
            "password": "asdsad#frew_DFS2"
    }*/
    private String userName;
    private String password;


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setPassword(String password) {
        this.password = password;
    }


}

```
далее добавляем связь в BookStoreTests
```java
  Credentials credentials = new Credentials();
        credentials.setUserName("Alex");
        credentials.setPassword("asdsad#frew_DFS2");

```
далее данные можно убрать в owner

```java
 @Test
    void generateTokenWithModelTest() {

            Credentials credentials = new Credentials();
            credentials.setUserName("Alex");
            credentials.setPassword("asdsad#frew_DFS2");


            given()
            .filters(withCustomTemplates())//это лог именно в алюр отчет
            .body(credentials)
            .contentType(JSON)
            .log().uri()//эти логи обеспечивают логирование к консоль
            .log().body()
            .when()
            .post("Account/v1/GenerateToken")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
            .body("status", is("Success"))
            .body("result", is("User authorized successfully."))
            .body("token.size()", (greaterThan(10)));
```

теперь мы наш ответ результата теста тоже уберем в модель
```java
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6IkFsZXgiLCJwYXNzd29yZCI6ImFzZHNhZCNmcmV3X0RGUzIiLCJpYXQiOjE2NTIxMTI0MjN9.fHYGuYb3IYon1sje7myEirqBGpAw1R1UZzlwBzwv90U",
    "expires": "2022-05-16T16:07:03.160Z",
    "status": "Success",
    "result": "User authorized successfully."
}

```

создаем в папке models GenerateTokenResponse 

```java
package models;

public class GenerateTokenResponse {
    /*{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6IkFsZXgiLCJwYXNzd29yZCI6ImFzZHNhZCNmcmV3X0RGUzIiLCJpYXQiOjE2NTAyNzExNTl9.ap1f8pWSdvCnDwsL9KGpkHU3xXRv-65lUOgt78bHCII",
    "expires": "2022-04-25T08:39:19.126Z",
    "status": "Success",
    "result": "User authorized successfully."
}*/

    private String token;
    private String expires;
    private String status;
    private String result;

    public String getToken() {
        return token;
    }

    public String getExpires() {
        return expires;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
    
}

```
убираем все в GenerateTokenResponse и меняем проверку body на extract().as(GenerateTokenResponse.class)

и делаем проверки через библиотеку AssertJ 
 добавляем ее в build.gradle

```java
  "org.assertj:assertj-core:3.22.0",

```

импортируем assertThat org.assertj.core.api.Assertions.assertThat;

```java
 @Test
    void generateTokenWithModelTest() {

        Credentials credentials = new Credentials();
        credentials.setUserName("Alex");
        credentials.setPassword("asdsad#frew_DFS2");

        GenerateTokenResponse tokenResponse =
                given()
                        .filters(withCustomTemplates())//это лог именно в алюр отчет
                        .body(credentials)
                        .contentType(JSON)
                        .log().uri()//эти логи обеспечивают логирование к консоль
                        .log().body()
                        .when()
                        .post("Account/v1/GenerateToken")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
                        .extract().as(GenerateTokenResponse.class);

        assertThat(tokenResponse.getStatus()).isEqualTo("Success");
        assertThat(tokenResponse.getResult()).isEqualTo("User authorized successfully.");
        assertThat(tokenResponse.getExpires()).hasSizeGreaterThan(10);
        assertThat(tokenResponse.getToken()).hasSizeGreaterThan(10).startsWith("eyJ");

    }

```
так писать правильнее потому так смы аккуратнее управляем и работаем с данными

###lombok
в структуре с моделями не оченнь хорошо что есть сякие геттеры сеттеры. И lombok этого
более аккуратный подход для работы с этими моделями. 
идем в build.gradle и подключаем lombok

````
plugins {
    id 'java-library'
    id "io.qameta.allure" version "2.9.6"
    id "io.freefair.lombok" version "6.0.0-m2"
    
    
````

создаем папку lombok и копируем тада наши модели, переименовываем добавляя lombok 
в названии файла

и минимально работающие ломбоки будут выгляедеть так

```java
package models.lombok;

import lombok.Data;

@Data
public class CredentialsLombok {

    private String userName;
    private String password;
}

```

```java
package models.lombok;

import lombok.Data;

@Data
public class GenerateTokenResponseLombok {

    private String token;
    private String expires;
    private String status;
    private String result;
}

```
@Data позволяет нам сделать так чтобы в Credentials сам генерился код get() и set()

```java
 @Test
    void generateTokenWithLombokTest() {

        CredentialsLombok credentials = new CredentialsLombok();
        credentials.setUserName("Alex");
        credentials.setPassword("asdsad#frew_DFS2");

        GenerateTokenResponseLombok tokenResponse =
                given()
                        .filters(withCustomTemplates())//это лог именно в алюр отчет
                        .body(credentials)
                        .contentType(JSON)
                        .log().uri()//эти логи обеспечивают логирование к консоль
                        .log().body()
                        .when()
                        .post("Account/v1/GenerateToken")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("schemas/GenerateToken_response_scheme.json"))
                        .extract().as(GenerateTokenResponseLombok.class);

        assertThat(tokenResponse.getStatus()).isEqualTo("Success");
        assertThat(tokenResponse.getResult()).isEqualTo("User authorized successfully.");
        assertThat(tokenResponse.getExpires()).hasSizeGreaterThan(10);
        assertThat(tokenResponse.getToken()).hasSizeGreaterThan(10).startsWith("eyJ");

    }
```
###занятие с кузнецовым
###Specification
очень часто во всех тестах идет одиноковая конструкция. в селции given()
описаны urlы, body, какие-то дефолтные для всех запросов заголовки
может авторизации и тд
от теста к тесту информация повторяется, поэтому сделали такую удобную вещь как спецификации которые по существующие
можно назвать контейнерами с конфигурациями которые хранят вещи которые являются общими либо для всего прокета
либо для тех тестов которые используют один эндпоинт

раньше их писали с помощью ResponseSpecBuilder либо RequestSpecBuilder
сейчас просто RequestSpecification и для локаничности достаточно просто использовать конструкцию with()
и затем использовать всякие перечисления
что здесь можно указать: базовый урл, базовый путь эндпоинта, уровень логирования, contetnType
```java
public class Specs {
    public static RequestSpecification request = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);
}
```


в RequestSpecification можно указать ожидаем статускод, или проверку что строка не пустая и не равна null 
обычно сюда выосится та часть которая в ответе от сервера приходит всегда одинаковая и не хочется ее каждй раз писать


```java
public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
        .expectStatusCode(expectedStatusCoe:200)
        .build();
```
как спецификации отрабатывают:
в тесте в секции given() мы вызываем метод spec и подлючаем в него спецификацию.
если нам надо передать спецификацию которая относится с запросы мы вызываем перед when(
дальше после then() вызываем spec(responseSpec)
и у нас сначала будут отрабаитывать те проверки которые мы убрали в спецификафию. если они пройдены успешно то дальше будут выполнятся те методы которые указаны ниже.
если ошибки произошли то на этом выполнение остановиться 
 получаем вот такой код

```java
package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;

public class Specs {
    public static RequestSpecification request =
            with()
                    .baseUri("https://reqres.in")
                    .basePath("/api")
                    .log().all()
                    .contentType(JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
           // .expectBody(containsString("success"))
            .build();

}

```

```java
public class UserTest {

    @Test
    void singUser() {
        given()
                .spec(request)
                .when()
                .get("users/2")
                .then()
                .spec(responseSpec)
                .log().body();

    }
```
ответ:

```java
Request method:	GET
Request URI:	https://reqres.in/api/users/2
Proxy:			<none>
Request params:	<none>
Query params:	<none>
Form params:	<none>
Path params:	<none>
Headers:		Accept=*/*
				Content-Type=application/json
Cookies:		<none>
Multiparts:		<none>
Body:			<none>
{
    "data": {
        "id": 2,
        "email": "janet.weaver@reqres.in",
        "first_name": "Janet",
        "last_name": "Weaver",
        "avatar": "https://reqres.in/img/faces/2-image.jpg"
    },
    "support": {
        "url": "https://reqres.in/#support-heading",
        "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
    }
}
UserTest > singUser() STARTED
BUILD SUCCESSFUL in 6s
```

### модели 
как мы опелируем запросами и ответами к нашему тестируемому приложению уже с точки зрения не отправить 
что-то в виде строчки  и получить какую-то строчку. А уже с точки зрения того как это находится у разработчиков нашего бэкэнда. 
лучше всего пойти при тестировании вашего проекта в репозиторий проекта, узнать у разработчиков
где они хранят все модели и просто скопировать себе.

возьмем пример из request.in

```java
{
    "data": {
        "id": 2,
        "email": "janet.weaver@reqres.in",
        "first_name": "Janet",
        "last_name": "Weaver",
        "avatar": "https://reqres.in/img/faces/2-image.jpg"
    },
    "support": {
        "url": "https://reqres.in/#support-heading",
        "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
    }
}
```

если мы хотим поработать не только с id, но и с email, first_name и тд.

не хочется все это писать в одну очень длинную строку, потому что это возможно практически невозможно прочитать.

нам необходимо этот текст, который представлят из себя json превратить в объект которым
мы будем пользоваться, чтобы читать в него данные возможно какие-то данные модифицировать и отправлять.

нас интересуют следующие поля: id, email, first_name, last_name. 
в данный момент поле avatar нам не интересно,поэтому мы ставим из билиотеки jekson.
аннотацию JsonIgnoreProperties и он будет игнорировать все поля которые мы не указали в коде специально

```java
@JsonIgnoreProperties(ignoreUnknown = true)
```

и теперь надо придумать что делать с ключом data.
как мы выбираем какие типы данных нам использовать. В поле id цифра без кавычек, то скорее всего это целое число. 
Но так как у нас здесь всегда статичные данные можно использовать любой тип, хоть byte 
хоть integer. Обязательно ссылочные, т.к. эта информация может отсутсвовать и прийти как null. 
Соответсвенно в примитив это не запишется, пэтому мы тут работает только с сылочными типами. 
И чаще всего на реальных проектах id чаще всего записывается как long потому что проекты
могут быть долгоживущими может быть огромная база данных и в integer все может не уместиться.
Но так как это учебный пример integer нам тут хватит. 
Все остальное это обычные строки 
по итогу в коде мы определили наш id, наш email
Т.к. по синтаксису джавы мы не можем писать переменные с нижним подчеркиванием, то мы пишем через
КамелКейс и сверху ставим аннотацию @JsonProperties("first_name) чтобы jekson знал что
ключ "first_name" на самом деле относится к полю firstName. Тоже самое с last_name. 
Чтобы у нас не возникло никаких проблем.
И для того чтобы jekson мог установить значение полей нам нужно сгенерировать Геттеры и Сеттеры на эти поля. 
```java
package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
{
    "data": {
        "id": 2,
        "email": "janet.weaver@reqres.in",
        "first_name": "Janet",
        "last_name": "Weaver",
        "avatar": "https://reqres.in/img/faces/2-image.jpg"
    },
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Integer id;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}

```
в итоге получается очень длинный код несмотря на то что у нас всего 4 поля. а в реальном тесте это может быть огромная простыня. 

И соответсвенно так как все эти данные вложены внутрь объекта который относитсяя к ключу Data
мы делаем отдельную модель на data. пищем небольшую оберточку, называем ее UserData
и в ней просто создаем уже созданную модель, описываем ее, называем data потому что название 
модели и название ключей должны совпадать между собой, и так же добавляем сеттер и геттер. 

`````
package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
`````
казалось бы хроший подход, удобно, но писать постоянно кучу этих геттеров сеттеров надоедает
несмотря на то что это может генерировать сама идея. 
Здесь нам на помощь приходит lombok который делает это все за нас. 

###lombok:

```java
package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLombok {
    private Integer id;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
}
```
это lombok на те же самые данные что и модель. Поля остались те же самые, но при этом появляется
дополнительная аннотация @Data и больше ничего нет. Нет ни геттеров ни Сеттеров которые
есть в модели, но при всем при этом геттерами и сеттрами мы можем здесь пользоваться.
Lombok добавляет все эти конструкции на этапе компиляции. т.е. во время запуска тестов
где используется lombok он сначала скомпелирует свои части в нормальный код который джава принимает 
и соответвенно у тех классов у которых мы пытались получить геттеры они появятся и никаких проблем не возникнет

И соответсвенно такой подход сильно упрощает нам создание моделей потому что мы поставили одну аннотацию
накидали сколько угодно полей в нашу модель и все. Нам больше не нужно писать конструкторы
геттеры сеттеры и тому подобное. за нас это все сделют
 
И все это нам нужно например для того чтобы мы могли сравнивать получение какого-то id
не в ответе rest-assured: через body, а могли получить целиковый объект и выполнить какие-то операции с ним.

например в нашем тесте мы получаем id и проверяем что он равен 2
 ````
    @Test
    void SingleUserWithLombokModel() {
        LombokUserData data =
                given()
                        .spec(request)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(responseSpec)
                        .log().body()
                        .extract().as(LombokUserData.class);

        assertEquals(2, data.getUser().getId());
    }
 ````

например после этого мы могли бы поменять у пользователя фамилию, или имя, или аватар добавить 
отправить этот запрос через put, снова получить модель и сравнить что ту модель которую мы 
изменили и отправили теперь она соответсвует той модели которую мы получили обратно.

через rest-assured: описывать все эти проверки это очень долго, поэтому модель используются
в большом количестве тестов, потому что с ними удобно работать. Их можно воспринимать как маленькие контейнеры
для данных которыми можно удобно пользоваться потому что вы можете всегда модель передать
в метод. ее даже можно отправить как body, всегда можно модель удобно поменять. какие-то поля
можно отредактировать и заново отправить. Не нужно ханить 10 jsonов на все слуаи жизни,
не нужно писать огромные строки и склеивать их с соблюдением все кавычек и прочее.
Это существенно упрощает жизнь при тестировании, потому что здесь вы пользуетесь не каккой-то непонятной абстракцией
в виде файла или строки, а вы пользуетесь полноценным объектом который видоизменяется, 
который можно передавать, можно манипулировать им в него можно записывать новые данные
их можно вкладывать друг в друга как м нашем примере с User data. 
Это сильно сокращает время на работу с jsonom при написании тестов. и сильно помогает в сложных проверках.


На каждый объект в джейсоне надо писать свою модель. 

###groove
самое основное для чего используется groove  в rest-assured: это наверное для проверок.
и еще для того чтобы нужные нам данные извлекать в модели, это фильтрация.
Фильтрация может быть разная, можно отфильтровать по условиям и в groove есть 2 удобных варианта
для этого. 

```java
def list = [1, 2, 3, 4]
println list.find{it > 2}
```
в ответе мы получим 3

описание к коду: создаем список, в groove списки задаются примерно как массивы в джава
def list = [1, 2, 3, 4]

и допустим мы хотим найти все число которые больше 2
println list.find{it > 2}

it - это служебное слово которое обозначет что мы указваем на все элементы по очереди.
в итоге будет идти итеритивно по списку и втсавлять  каждое наше число из массива.
1>2
2>2
3>2
4>2
 в этом случае в ответе мы получим 3 потому что find берет только первый элемент соответвующий условию, а остальное все отбрасывает.


если мы хотим чтобы ответ у нас был в виде списка и мы видели все варианты нашей выборки
то пишем
```java
def list = [1, 2, 3, 4]
println list.findAll{it > 2}
```
и в ответе уже получим [3, 4]

это не единственное отличе их работы, в них еще есть такой нюанс из-за которого советуют 
брать поиск именно через findAll даже если будет всего один элемент в списке 
все равно лучше всегда использовать findAll особенно когда вы работаете с джавой
потому что там более предсказуемое поведение при выполнении 
если мы напишем примерно такой код, это может быть когда мы получили не тот список который ожидали результатом будет null
```java
def list = [1, 2, 3, 4]
println list.find{it > 5}
```
результат null
если в коде проверок в json дальше идут какие-то поля то при поытке их вызвать будет ошибка null 
по enterExceptions который моментально уронит весь тест, возможно не только текущий но и все остальные 
и вы будете долго искать причину возникновения этой ошибки
Соответсвенно чтобы этого избежать, самы простой вариант использовать findAll 
потому что если он ничего не нашел, он вернет просто пустой список
```java
def list = [1, 2, 3, 4]
println list.findAll{it > 5}
```
ответ []
и в данном случае пустой сптсок это всегда лучше чем null, ну потому что взяв что-то 
у пустого списка мы можем получить вполне понятную ошибку. и мы сразу поймем что это вылетело
вот в такм тесте, в такой строке. В случае с null потому что все плохо, мы получили nullPointer
откуда он вылетел непонятно, а у нас может быть 100 тестов и попробуй найди где эта ошибка.


###Трансформация двумерных списков в одномерный массив
это очень полезно, потому что чато бывает так что мы нашли чтото по условию и оно превратилось
не в один красивый список, а например в несколько
```java
def list =[ [1, 2], [3, 4]]
println list.findAll{it > 1}
```
и при таком варианте выдает ошибку, потому что направильно работаем. у нас двумерный список, а мы пытаемся
его сравнить не списком. 

Для того чтобы это работало надо использовать метод flatten
```java
def list = [1, 2, 3, 4]
println list.flatten().findAll{it > 1}
```
в ответ получаем: [2, 3, 4]

что делает этот метод? по сути он просто берет, проходит по всему списку и превращает список
с вложенным списком в один однородный список. и это очень удобно, потому что нам и такие списку вполне могут прийти
такое бывает когда например мы результат прогоняем через фильтр и он нам вместо того
чтобы завернуть это в один список ввыдает ответ из одного списка в другом. И для того чтобы в 
дальнейшем работать с простым списком мы просто применяем flatten и все работает. 

кроме этого можно использовать не только такие выражения как {it > 1}, но и регулярные выражения
````
.body("data.findAll{it.email=~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));

````
берем строчку из нашего теста для примера. тут в body нам приходит data в которой
есть id, email, firstName, lastName. и в данном случае мы говорим что для списка всех пользователей
мы хотим найти наш email у которого по ~/.*?@reqres.in/ регулярному выражению мы говорим 
что нам надо найти все email которые не важо с чего начинаются, но заканчиваются на reques.in
он соберет нам весь список из них, потом мы заберем поле email, а так как он получится тоже списком
мы его сразу сделаем одномерным применив flatten. и дальше говорим что список который мы получили 
должен иметь в себе  "eve.holt@reqres.in" вот такой email.
 
```java

    @Test
    public void CheckEmailUsingGroovy() {
        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email=~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));

    }
```

Ответ
```java
Request method:	GET
Request URI:	https://reqres.in/api/users
Proxy:			<none>
Request params:	<none>
Query params:	<none>
Form params:	<none>
Path params:	<none>
Headers:		Accept=*/*
				Content-Type=application/json
Cookies:		<none>
Multiparts:		<none>
Body:			<none>
{
    "page": 1,
    "per_page": 6,
    "total": 12,
    "total_pages": 2,
    "data": [
        {
            "id": 1,
            "email": "george.bluth@reqres.in",
            "first_name": "George",
            "last_name": "Bluth",
            "avatar": "https://reqres.in/img/faces/1-image.jpg"
        },
        {
            "id": 2,
            "email": "janet.weaver@reqres.in",
            "first_name": "Janet",
            "last_name": "Weaver",
            "avatar": "https://reqres.in/img/faces/2-image.jpg"
        },
        {
            "id": 3,
            "email": "emma.wong@reqres.in",
            "first_name": "Emma",
            "last_name": "Wong",
            "avatar": "https://reqres.in/img/faces/3-image.jpg"
        },
        {
            "id": 4,
            "email": "eve.holt@reqres.in",
            "first_name": "Eve",
            "last_name": "Holt",
            "avatar": "https://reqres.in/img/faces/4-image.jpg"
        },
        {
            "id": 5,
            "email": "charles.morris@reqres.in",
            "first_name": "Charles",
            "last_name": "Morris",
            "avatar": "https://reqres.in/img/faces/5-image.jpg"
        },
        {
            "id": 6,
            "email": "tracey.ramos@reqres.in",
            "first_name": "Tracey",
            "last_name": "Ramos",
            "avatar": "https://reqres.in/img/faces/6-image.jpg"
        }
    ],
    "support": {
        "url": "https://reqres.in/#support-heading",
        "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
    }
}

```
в ответе видно что пришел большой список
видно data это список в котором лежит список из этих email. условно полуается массив в массиве
и чтобы не было проблем мы все это собираем в однородный список, проверяем и выясняем присутвует
ли нужный email.
можно делать и более сложные регулярные выражения. это все позволяет делать проверки 
в rest-assured: не используя assert

rest-assured: в сбе использует groove поэтому его не надо подключать отдельно в build.gradle


для использования groove надо упустить джаву до 8





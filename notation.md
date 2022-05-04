### REST API
добавляется еще один промежутоный слой между браузером и сервером, т.е. мы делаем все тоже самое в обход браузера.
Имитируем сбросы которые идут от браузера сервер. и при разработке сложных тестов мы часть взаимоотношений с продуктом переносим на более легкий и более дешевый слой.


Есть ситуации когда при запуске тестов мы ошиблись в URLe и чтобы этого избежать при запуске в дженкинс в сборке в разделе http request мы указываем парамметр $(REMOTE_DRIVER_URL)
 В таком случае если сразу обнаружена ошибка даже gradle не будет запускаться и не будут тратиться ресурсы.

статус коды надо знать)
информационные 100-199 

успешные 200-299

перенаправления 300-399

клиентские  ошибки 400-499

серверные ошибки 500-599 


создаем build.gradle 
для паралелизации добавляем код

```java

    if (System.getProperty("threads") != null) {
        systemProperties += [
                'junit.jupiter.execution.parallel.enabled'                 : true,
                'junit.jupiter.execution.parallel.mode.default'            : 'concurrent',
                'junit.jupiter.execution.parallel.mode.classes.default'    : 'concurrent',
                'junit.jupiter.execution.parallel.config.strategy'         : 'fixed',
                'junit.jupiter.execution.parallel.config.fixed.parallelism': System.getProperty("threads").toInteger()
        ]
```

добавляем папку tests и создам SelenoidTests 
будем делать одно и тоже разными способами

надежные сайты для серча baeldung.com и stackoverflow.com и mkyong.com 

для получения джейсона можно в командной строке сделать запрос curl сайт

классический подход к rest Assured для json  это
```java
given()
       .config(RestAssured.config()jsonConfig(jsonConfig().numberReturnType(BIG-DECIMAL)))
.when()
        .get("/price")
.then()
        .body("price", is(new BigDecimal(12.12)); 
```
 

классическая структура тестов это

arrange-act-assert
это шаблон для форматирования Unit тестов. Обозначающий разделения теста на 3 части

Arrange - все необходимые подготовки и входные данные
Act - собственно вызов того метода который вы тестируете
Assert - проверки, что метод делает то что надо
В ваших пример со списком должно быть что-то такое:
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
либо given-when-then
Given — первоначальный контекст (предусловие)
When — событие (что является триггером сценария)
Then — результат, который мы хотим получить

в самом простом понимании это Дано-когда-тогда

для того чтобы это все работало добавляем в build.gradle Rest Assured
"io.rest-assured:rest-assured:$restAssuredVersion"

```java
 @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
 ```
импортируе is  -  org.hamcrest.Matchers.is

в данном случаем в given и when ничего нет и мы можем их просто убрать

```java
 @Test
    void checkTotalWithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }
```
для удобного чтения json в хроме надо включить плагин JSONVue 
json с которым мы работаем
```java
{
"total": 20,
"used": 0,
"queued": 0,
"pending": 0,
"browsers": {
"chrome": {
"100.0": { },
"99.0": { }
},
"firefox": {
"97.0": { },
"98.0": { }
},
"opera": {
"84.0": { },
"85.0": { }
}
}
}
```
далее мы хотим проверить версию браузера. 
```java
 @Test
    void checkChromeVersion() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("browsers.chrome", hasKey("100.0"));
    }
```

далее пример как делать плохо. Результат ответа сохраняем в переменную response, и мы хотим в качесве ответа 
забрать все содержимое extract().response().asString();
далее создаем expectedResponse в нее вставляем весь полученный ответ и проводим проверку.
В итоге получается непонятная каша. Так лучше не работать, надо либо убрать это в текстовый файл, 
либо создать модель.
```java
 @Test
    void checkTotalBadPractice() {
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response().asString();

        System.out.println("Response: " + response);

        String expectedResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0," +
                "\"browsers\":" +
                "{\"chrome\":{\"100.0\":{},\"99.0\":{}}," +
                "\"firefox\":{\"97.0\":{},\"98.0\":{}}," +
                "\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";
        assertEquals(expectedResponse, response);
    }

```
теперь делаем хорошую проверку. Сразу достаем то поле которое проверяем
.extract().path("total)
тип ответа бует не string, а integer.
присваиваем integer expectedResponse 20(потому что мы как раз хотели проверить что тотал = 20)
а проверку оставляем прежней

```java
 @Test
    void checkTotalGoodPractice() {
        int response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract()
                .path("total");

        System.out.println("Response: " + response);

        int expectedResponse = 20;
        assertEquals(expectedResponse, response);
    }
```
далее заменяем тип объекта на Response, затем если мы выводим +response выходит что-то
непонятное, тоже самое если выводим +response.toString. Для того чтобы мы увидели вразумительный ответ необходимо вывести
+response.asString()
результаты разного вывода на экран
```java
Response: io.restassured.internal.RestAssuredResponseImpl@6682b659
Response .toString(): io.restassured.internal.RestAssuredResponseImpl@6682b659
Response .asString(): {"total":20,"used":0,"queued":0,"pending":0,"browsers":{"chrome":{"100.0":{},"99.0":{}},"firefox":{"97.0":{},"98.0":{}},"opera":{"84.0":{},"85.0":{}}}}

Response .path("total"): 20
Response .path("browsers.chrome"): {100.0={}, 99.0={}}

```

селеноид у нас запаролен. если мы откроем наш адресс без авторизации то мы увидим ошибку
401 это значит что все плохо. следующем тесте у нас немного поменялся адрес. в предыдущих тестах
стутус код был 200. для того чтобы зайти запароленным мы в начале пути указываем наш логин/пароль.
"https://user1:1234@selenoid.autotests.cloud/wd/hub/status"
```java
 @Test
    void checkStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

@Test
    void checkStatus200() {
            get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
            .then()
            .statusCode(200);
            }
```

авторизацию можно сделать и покрасивее

```java

    @Test
    void checkStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }
```

### reqres.in
это сайт тренажер для api. в нем есть разные endpoint, куда можно обраться для отработки разных запросов.
он возвразает массив данных, например список пользователей. 
важно! в селеиде есть простые запросы типа $(""").clic()  и мы не разделяем на типы запросов, а в  
Rest Api есть разные типы запросов

*GET — получение ресурса/просто взять

*POST — создание ресурса/дать и забрать(логин/пароль)

*PUT — обновление ресурса/

*DELETE — удаление ресурса

Создаем простой тест на авторизацию ReqresinTest

```java
 @Test
    void successfulLogin() {
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

        String authorizedData = "{\"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\"}";

        given()
                .body(authorizedData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
```

важно! Указать contentType(json)

Далее пишем тест на негативную авторизацию

```java
 @Test
    void missingPasswordLogin() {
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

        String authorizedData = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .body(authorizedData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
```


#### REST API №2

ТРЕНАЖЕР DEMOWEBSHOP.TRICENTIS.COM

добавляем товар в корзину, пробуем подделать этот запрос под api. сделать его на более низком
уровне чтобы можно было добавлять товар в корзину при этом совсем не трогая браузер и ничего не кликая.

в браузере открываем http://demowebshop.tricentis.com/ открываем код, жмем вкладку network
там должно быть пусто, если там заполнено очищаем жмя на крестик. там можно увидеть все детали страницы в разобранном
виде. все иконки, все кнопки и т.д.
и браузер(фронтэнд) он из всего этого формирует приложение.
жмем на add to cart и во встроенном в хром перехватчике запросов появляется какая-то гифка загрузчика
какойто запрос(который скорее всего нам и нужен) и какаято картинка на закрытие окна(в норме такие картинки приходить не должны)
у них не очень хорошо сделан сайт. 
выделяем запрос и во вкладке headers видим какой-то url, видим метод post, status code 200.

этот запрос попробуем перенести в автотест

если на запросе нажать правой кнопкой мышки и скопировать для bash в командной строке можно вставить и посмотреть что из себя представляет этот запрос.
открываем терминал, вставляем и смотрим
при каждом новом запуске можно увидеть как увеличивается число товаров в корзине, браузер для этого не нужен совсем. 
в нашем запросе где-то передаются данные авторизации. 

во вкладке network есть фильтры, мы можем выбрать fetch/XHR и мы будем видеть только запросы, но не всегда срабатывает.

для простых задач этого достаточно, но при увеличении количества запросов может понадобиться сторонний анализатор трафика.

Charles, fiddler это инструменты которые в этом помогут. они могут имитировать трафик, это незаменимая вещь в мобильной автоматизации.
он так же помогает 

Для композинга мы будем использовать Postman
берем нашу ссылку на запрос для bash из networka, идем в postman
file->import->raw text и вставляем
делаем импорт
выбираем вкладку herders делаем send
видим что товары снова добавились в корзину
теперь поиграемся с параметрами чтобы понять минимум который нам необходим, потому что каждый параметр это 
дополнительная строчка кода, а возможно она нам и не нужна. 
по одному убираем галочки и снова запускаем, смотрим добавляются ли товары в корзину.

в итоге нас остается contentType и cookie. 
здесь можно сгенерировать автотесты, но они не айс.

в идее создаем demoWebShopTests
переносим данные из postman по строчкам
берем данные из автогенератора в постман
```java
Unirest.setTimeouts(0, 0);
        HttpResponse<String> 
        response = Unirest.post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .header("Cookie", "__utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); " +
        "Nop.customer=56c84236-b920-47ff-8d55-1e18c6da298d; " +
        "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utma=78382081.1290585258.1649697010.1651587403.1651591257.12; __utmc=78382081; __utmt=1; " +
        "NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __atuvc=32%7C15%2C1%7C16%2C0%7C17%2C1%7C18; __atuvs=6271485dcfd79d8c000; __utmb=78382081.2.10.1651591257; " +
        "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; " +
        "Nop.customer=56c84236-b920-47ff-8d55-1e18c6da298d")
        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
        .asString();

```

и переносим его в наш код
```java
 @Test
    void addToCartTest() {
    String response = 
    given()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .cookie("ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; __utma=78382081.1290585258.1649697010.1649697010.1649697010.1; __utmc=78382081; __utmz=78382081.1649697010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); " +
                       "NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; __atuvc=4%7C15; __atuvs=6254613d8c9f5185003; __utmt=1; __utmb=78382081.5.10.1649697010; " +
                       "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; " +
                       "Nop.customer=b64e760d-494e-4329-9db7-3b2dce7a6a36")
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

```
лишние куки можно убрать


```java
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
```

получаем вот такой ответ
```java

{"success":true,"message":"The product has been added to your \u003ca " +
        "href=\"/cart\"\u003eshopping cart\u003c/a\u003e",
        "updatetopcartsectionhtml":"(8)",
        "updateflyoutcartsectionhtml":"\u003cdiv id=\"flyout-cart\" " +
        "class=\"flyout-cart\"\u003e\r\n   " +
        " \u003cdiv class=\"mini-shopping-cart\"\u003e\r\n        " +
        "\u003cdiv class=\"count\"\u003e\r\nThere are \u003ca href=\"/cart\" " +
        "class=\"items\"\u003e8 item(s)\u003c/a\u003e in your cart.        " +
        "\u003c/div\u003e\r\n            " +
        "\u003cdiv class=\"items\"\u003e\r\n                    " +
        "\u003cdiv class=\"item first\"\u003e\r\n                           " +
        " \u003cdiv class=\"picture\"\u003e\r\n                               " +
        " \u003ca href=\"/build-your-cheap-own-computer\" " +
        "title=\"Show details for Build your own cheap computer\"\u003e\r\n                                   " +
        " \u003cimg alt=\"" +
        "Picture of Build your own cheap computer\" src=\"http://demowebshop.tricentis.com/content/images/thumbs/0000172_build-your-own-cheap-computer_47.jpeg\" " +
        "title=\"Show details for Build your own cheap computer\" " +
        "/\u003e\r\n                                " +
        "\u003c/a\u003e\r\n                            " +
        "\u003c/div\u003e\r\n                       " +
        " \u003cdiv class=\"product\"\u003e\r\n                            " +
        "\u003cdiv class=\"name\"\u003e\r\n                                " +
        "\u003ca href=\"/build-your-cheap-own-computer\"" +
        "\u003eBuild your own cheap computer\u003c/a\u003e\r\n                           " +
        " \u003c/div\u003e\r\n                                " +
        "\u003cdiv class=\"attributes\"\u003e\r\n                                    " +
        "Processor: Medium [+15.00]\u003cbr /\u003eRAM:" +
        " 2 GB\u003cbr /\u003eHDD: 320 GB\r\n                                " +
        "\u003c/div\u003e\r\n                            " +
        "\u003cdiv class=\"price\"\u003eUnit price: \u003cspan\u003e815.00\u003c/span\u003e\u003c/div\u003e\r\n                            " +
        "\u003cdiv class=\"quantity\"\u003eQuantity: " +
        "\u003cspan\u003e8\u003c/span\u003e\u003c/div\u003e\r\n                        " +
        "\u003c/div\u003e\r\n                    " +
        "\u003c/div\u003e\r\n            " +
        "\u003c/div\u003e\r\n            " +
        "\u003cdiv class=\"totals\"\u003eSub-Total: \u003cstrong\u003e6520.00\u003c/strong\u003e\u003c/div\u003e\r\n            " +
        "\u003cdiv class=\"buttons\"\u003e\r\n                    " +
        "\u003cinput type=\"button\" value=\"Go to cart\" class=\"button-1 cart-button\" onclick=\"setLocation(\u0027/cart\u0027)\" /\u003e\r\n                            " +
        "\u003c/div\u003e\r\n    " +
        "\u003c/div\u003e\r\n\u003c/div\u003e\r\n"}
demoWebShopTests > addToCartTest() STARTED
```
берем данные из ответа и добавлЯем проверки и добавляем логи

````java
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

в конце этой лекции есть код по авторизации
лекция №18


#### REST API. ПОДКЛЮЧАЕМ ALLURE

ОТКРЫВАЕМ demoqa.com
заходим во вкладку Book Store Application
и мы видим магазин, максимально простой и максимально примитивный. есть форма логина, 
есть профиль пользователя не доступный без авторизации. и есть book store api.
это swagger - автодокументация на rest api, на те интерфейсы которые у нас есть.
Бэкенд сам по себе похож на то что мы делаем в автотестах. тоже какие-то методы, какие-то анннотации через @
грубо говоря тоже самое только запускаюся не тесты а какие-то сервисы на которые можно стучаться
на которые тесты можно написать. 
У бэкенд разработчиков есть механизм который позволяет посторить документацию на все существующие интерфейсы и эндпоинты.
т.е. адреса куда мы стучимся.
в бекэнде при добавлеии определенных аннотаций и библиотек можно сделать так чтобы на лету генерилась отчетность и документация.
в которой видно весь функционал



package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static listener.CustomAllureListener.withCustomTemplates;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {
    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://demoqa.com/";
    }

    @Test
    void getBooksTest() {
        get("BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void getBooksWithAllLogsTest() {
        given()
                .log().all()
                .when()
                .get("BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void getBooksWithSomeLogsTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void generateTokenTest() {
        String data = "{ \"userName\": \"Alex\", " +
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
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", (greaterThan(10)));
    }

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
}

package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import models.Credentials;
import models.GenerateTokenResponse;
import models.lombok.CredentialsLombok;
import models.lombok.GenerateTokenResponseLombok;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static listener.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.Assertions.assertThat;
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
    void getTokenTest() {
        String data = "{ \"userName\": \"Alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";

        String token =
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
                        .extract().path("token");

        System.out.println("Token:" + token);

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
}

package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAPIHWTest {


    @Test
    void singleUserTest() {

        given()
                .when()
                .contentType(JSON)
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.email", equalTo("janet.weaver@reqres.in"));
    }

    @Test
    void singleUserNotFoundTest() {
        given()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void listTest() {
        given()
                .param("id", 2)
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("data.name", is("fuchsia rose"));
    }

    @Test
    void createTest() {
        String authDataCreate = "{\"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        given()
                .body(authDataCreate)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"));

    }

    @Test
    void upDateTest() {
        String upData = "{\"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";
        given()
                .body(upData)
                .param("name", "morpheus")
                .param("job", "zion resident")
                .param("updatedAt", "2022-04-08T09:32:51.115Z")
                .contentType(JSON)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("job", is("zion resident"));
    }


    @Test
    void deleteTest() {
        given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);

    }



    @Test
    void registerSuccessfulTest() {
        String inputData = "{\"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";

        given()
                .body(inputData)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("token", is(notNullValue()));
    }


}

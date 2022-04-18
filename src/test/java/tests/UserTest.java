package tests;

import models.UserData;
import models.lombok.LombokUserData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.Specs.request;
import static tests.Specs.responseSpec;

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

    @Test
    void listTest() {
        given()
                .spec(request)
                .when()
                .get("users/?page=2")
                .then()
                .log().body();

    }

    @Test
    void singleUserWithModel() {

        UserData data=
                given()
                        .spec(request)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(responseSpec)
                        .log().body()
                        .extract().as(UserData.class);
        assertEquals(2, data.getData().getId());
      //  assertThat(data.getData().getId()).isEqualTo(2);
    }

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
}

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

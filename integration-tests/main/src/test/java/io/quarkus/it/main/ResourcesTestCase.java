package io.quarkus.it.main;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.DisabledOnIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
public class ResourcesTestCase {

    @Test
    public void included() {
        RestAssured.when()
                .get("/resources/test-resources/file.txt")
                .then()
                .statusCode(200)
                .body(is("A text file"));
    }

    @Test
    @DisabledOnIntegrationTest
    public void excludedJvm() {
        RestAssured.when()
                .get("/resources/test-resources/file.adoc")
                .then()
                .statusCode(200)
                .body(is("= An AsciiDoc File"));

        RestAssured.when()
                .get("/resources/test-resources/excluded/unwanted.txt")
                .then()
                .statusCode(200)
                .body(is("An unwanted file"));
    }
}

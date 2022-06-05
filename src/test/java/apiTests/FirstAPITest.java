package apiTests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class FirstAPITest {
    @Test
    public void getPosts() {
        given()
                .when()
                .get("http://training.skillo-bg.com:3100/posts/public?take=1&skip=0")
                .then()
                .log()
//                .body();
                .all();

    }
}

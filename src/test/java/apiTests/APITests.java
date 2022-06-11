package apiTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class APITests {
    static String loginToken;

    @BeforeTest
    public void loginTest() throws JsonProcessingException {
        //create new loginPOJO class object named login
        LoginPOJO login = new LoginPOJO();

        //set the login credentials to our login object
        login.setUsernameOrEmail("test51");
        login.setPassword("test51");

        //Convert POJO object to Json
        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson = objectMapper.writeValueAsString(login);
        System.out.println("Converted Json is: ");
        System.out.println(convertedJson);

        baseURI = "http://training.skillo-bg.com:3100";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(convertedJson)
                .when()
                .post("/users/login");

        response
                .then()
                .statusCode(201);


        // convert the response body json into a string
        String loginResponseBody = response.getBody().asString();

        loginToken = JsonPath.parse(loginResponseBody).read("$.token");

    }

    @Test
    public void likePost() throws JsonProcessingException {
        // create an object of ActionsPOJO class and add value for the fields
        ActionsPOJO likePost = new ActionsPOJO();
        likePost.setAction("likePost");
    ValidatableResponse validatableResponse = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + loginToken)
            .and()
            .body(likePost)
            .when()
            .patch("/posts/4626")
            .then()
            .assertThat().body("post.id", equalTo(4626))
            .log()
            .all();

//        System.out.println("Extracted validatable response item is: ");
//        System.out.println((String) validatableResponse.extract().path("post.user.username"));
//        System.out.println((String) validatableResponse.extract().path("user.username"));

        String myUser = validatableResponse.extract().path("user.username");

        Assert.assertEquals(myUser, "test52");
    }

    @Test
    public void commentPost() throws JsonProcessingException {
        ActionsPOJO commentPost = new ActionsPOJO();
        commentPost.setContent(("Comment post content"));
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(commentPost)
                .when()
                .post("/posts/4626/comment")
                .then()
                .assertThat().body("content", equalTo("Comment post content"))
                .assertThat().body("user.id", equalTo(2393))
                .log()
                .all()
                .statusCode(201);

//        Integer comentID = JsonPath.parse(commentResponseBody).read("$.id");


            }

    @Test
    public void getPosts() {
        ValidatableResponse validatableResponse =
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .queryParam("take", 6)
                .queryParam("skip", 0)
                .when()
                .get("/posts/public")
                .then()
                .log()
                .all()
                .statusCode(200);

        ArrayList<Integer> postIds = validatableResponse.extract().path("id");
        Assert.assertNotEquals(postIds, null);
        for (int element : postIds) {
            System.out.println(element);
        }
    }

    @Test
    public void deleteComment() throws JsonProcessingException {
        commentPost();

given()
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + loginToken)
        .when()
        .delete("/posts/4622/comments/")
        .then()
        //.statusCode(200)
        .log()
        .all();
    }

}

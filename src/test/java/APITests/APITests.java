package APITests;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APITests {
    static String authToken;

    @BeforeClass


    @Test
    public void likePost() throws JsonProcessingException {

    }

//    @Test
//    public void commentPost() throws JsonProcessingException {
//        actionsPOJO commentPost = new actionsPOJO();
//        commentPost.setContent(("Comment post content"));
//        given()
//                .header()
//                .header()
//                .body(commentPost)
//                .when()
//                .post("/posts/id/comment")
//                .then()
//                .assertThat().body("content", equalTo(""));
//    }

//    @Test
//    public void getPosts() {
//        ValidatableResponse validatableResponse =
//        given()
//                .header()
//                .header()
//                .queryParam("take", 6)
//                .queryParam("skip", 0)
//                .when()
//                .get("/posts/public")
//                .then()
//                .log()
//                .all()
//                .statusCode(200);
//
//        Integer postId = validatableResponse.extract().path("id");
//        Assert.assertNotEquals(postId, null);
//    }

}

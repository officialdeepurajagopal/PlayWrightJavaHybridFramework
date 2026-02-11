package org.example.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.api.ApiClient;
import org.example.model.Post;
import org.example.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class APITest extends BaseTest {

    @Test(groups = {"apitest"})
    public void sampleAPITest() {
        String env = System.getProperty("env", "dev");
        ConfigLoader.load(env);
        String apiBaseUrl = ConfigLoader.get("apiBaseUrl");
        System.out.println("Loaded apiBaseUrl: " + apiBaseUrl);
        ApiClient client = new ApiClient(apiBaseUrl);
        Response response = client.get("/posts");
        System.out.println("API Response: " + response.asString());

        JsonPath json = response.jsonPath();
        int firstUserId = json.getInt("[0].userId");

        // Map JSON response to List<Post>
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Post> posts = mapper.readValue(response.asString(), new TypeReference<List<Post>>() {});
            for (Post post : posts) {
                System.out.println(post.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(response.getStatusCode(), 200);
    }
}

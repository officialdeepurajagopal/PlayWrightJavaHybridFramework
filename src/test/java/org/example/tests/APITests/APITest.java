package org.example.tests.APITests;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.response.Response;
import org.example.model.Book;
import org.example.tests.Common.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class APITest extends BaseTest {

    @Test(groups = {"apitest"})
    public void getBookById() {
        Response response = client.get("/Library/GetBook.php?ID=bcg97692000");
        System.out.println("API Response: " + response.asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        // Map JSON response to List<Book> using ObjectMapper
        List<Book> books = null;
        try {
            books = objectMapper.readValue(response.asString(), new TypeReference<List<Book>>() {});
            System.out.println("Mapped Books: " + books);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to map response to Book object: " + e.getMessage());
        }

        Assert.assertNotNull(books, "Books list should not be null");
        Assert.assertFalse(books.isEmpty(), "Books list should not be empty");
        Assert.assertEquals(books.get(0).getBook_name(), "DeepTest9000");
    }

    @Test(groups = {"apitest"})
    public void getBookByAuthor(){
        Response response = client.get("/Library/GetBook.php?AuthorName=John foer");
        System.out.println("API Response: " + response.asString());
    }
}

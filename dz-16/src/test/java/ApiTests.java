import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static io.restassured.RestAssured.put;

public class ApiTests {

    private static String token;

    @BeforeMethod
    public void baseSetup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com/";
    }


    @Test
    public void getKeyTest() {

        JSONObject body = new JSONObject();
        body.put("username", "admin");
        body.put("password", "password123");


        Response response = RestAssured.given()
                .body(body.toString())
                .header("Content-Type", "application/json")
                .post("/auth");
        response.then().statusCode(200);
        response.prettyPrint();

        String responseBody = response.getBody().asString();

        JSONObject jsonResponse = new JSONObject(responseBody);

        token = jsonResponse.getString("token");

    }

    @Test
    public void getBookingIdsTest() {
        Response response = RestAssured.given()
                .get("booking");
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test
    public void partiallyChangeBookingPriceTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("totalprice", 1);

        Response response = RestAssured.given()
                .body(requestBody.toString())
                .header("Content-Type", "application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .patch("booking/1");
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test
    public void changeBookingUsingPutTest() {


        LocalDate checkinDate = LocalDate.parse("2018-02-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate checkoutDate = LocalDate.parse("2019-02-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", checkinDate);
        bookingDates.put("checkout", checkoutDate);

        JSONObject requestBody = new JSONObject();
        requestBody.put("firstname", "Josh");
        requestBody.put("lastname","Allen");
        requestBody.put("totalprice",1);
        requestBody.put("depositpaid", true);
        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds","super bowls");

        Response response = RestAssured
                .given()
                .body(requestBody.toString())
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .put("booking/1");
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test
    public void deleteBookingTest() {

        Response response = RestAssured
                .given()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .delete("booking/2");
        response.then().statusCode(201);
        response.prettyPrint();

    }


}

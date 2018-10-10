package tests.restexamples;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

public class GetWeatherTests
{
    @Before
    public void setup()
    {
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
    }

    @Test
    public void get_weather_details_and_println()
    {
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "/Sydney");
        String responseBody = response.getBody().asString();
        System.out.println("Response Body:  " + "\n" + responseBody);
    }

    @Test
    public void get_weather_details_then_extract_response()
    {
        Response response =
                when()
                 .get("/Brisbane")
                 .then()
                 .body("City", equalTo("Brisbane"))
                 .extract()
                 .response();

        String weatherDescription = response.path("WeatherDescription");
        System.out.print("weather description is " + weatherDescription);
    }
}

package tests.restexamples;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Story;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
public class CountrySearchTests
{

    @Test
    public void verify_country_search_using_code_AU()
    {
        RestAssured
                .when()
                .get("http://services.groupkt.com/country/get/iso2code/AU")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("RestResponse.result.name", is("Australia"));
    }

    @Step("To Verify the Country Code US")
    @Test
    public void verify_country_search_using_code_US()
    {
        RestAssured
                .when()
                .get("http://services.groupkt.com/country/get/iso2code/US")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("RestResponse.result.name", is("United States of America"));
    }
}

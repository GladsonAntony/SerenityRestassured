package restassured.junit;

import io.restassured.RestAssured;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

public class CountrySearchTests {

    @Test
    public void verify_country_search_using_code_AU() {
        RestAssured.
                when().get("http://services.groupkt.com/country/get/iso2code/AU").
                then().assertThat().statusCode(200).
                and().body("RestResponse.result.name", is("Australia"));
    }

    @Test
    public void verify_country_search_using_code_US(){
        RestAssured.
                when().get("http://services.groupkt.com/country/get/iso2code/US").
                then().assertThat().statusCode(200).
                and().body("RestResponse.result.name", is("United States of America"));
    }

}

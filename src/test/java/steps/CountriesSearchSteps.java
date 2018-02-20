package steps;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import static org.hamcrest.Matchers.is;

public class CountriesSearchSteps {

    private String ISO_CODE_SEARCH = "http://services.groupkt.com/country/get/iso2code/";
    private Response response;

    @Step("When I try to search for the country code {0}")
    public void searchCountryByCode(String code){
        response = SerenityRest.when().get(ISO_CODE_SEARCH + code);
    }

    @Step("Then the search is successful")
    public void searchIsExecutedSuccesfully(){
        response.then().statusCode(200);
    }

    @Step("And the result shows the country name is {0}")
    public void iShouldFindCountry(String country){
        response.then().body("RestResponse.result.name", is(country));
    }
}

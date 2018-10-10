/**
 * @Author Gladson Antony
 * @Date 02-OCT-2018
 */

package examples.soap;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.FileWriter;
import java.io.IOException;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("./src/test/resources/test-data.csv")
public class GetBankDetailsCSVParameters
{
    @Getter @Setter String bankBlzCode;

    @Qualifier
    public String getQualifier()
    {
        return this.bankBlzCode;
    }

    @Test
    public void shouldRunTheseSoapExamples()
    {
        String getBankDetailsXML = "<soapenv:Envelope \n" +
                "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "    xmlns:blz=\"http://thomas-bayer.com/blz/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <blz:getBank>\n" +
                "            <blz:blz>" + this.bankBlzCode + "</blz:blz>\n" +
                "        </blz:getBank>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        try
        {
            FileWriter file = new FileWriter("./src/test/resources/Requests/" + bankBlzCode + "_Request.xml");
            file.write(getBankDetailsXML);
            file.flush();
            file.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Response response = RestAssured
                .given()
                .auth()
                .basic("admin", "admin")
                .config(RestAssured.config().logConfig(
                        LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)))
                .baseUri("http://www.thomas-bayer.com/axis2/services/BLZService")
                .request()
                .body(getBankDetailsXML)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Assert.assertTrue(response.getBody().prettyPrint().toString().contains("ACREDOBANK"));

        try
        {
            FileWriter file = new FileWriter("./src/test/resources/Response/" + bankBlzCode + "_Response.xml");
            file.write(response.getBody().prettyPrint().toString());
            file.flush();
            file.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}

/**
 * @Author Gladson Antony
 * @Date 02-OCT-2018
 */

package tests.soapexamples;

import dataprovider.XlsSheetDataProvider;
import dataprovider.XlsxSheetDataProvider;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.junit.annotations.Concurrent;
import net.thucydides.junit.annotations.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@RunWith(SerenityParameterizedRunner.class)
@Concurrent
public class GetBankDetails
{
    @TestData
    public static Collection spreadsheetData() throws Exception
    {
        InputStream spreadsheet = new FileInputStream("src/test/resources/Excel Files/getBankDetails.xls");
        return new XlsSheetDataProvider(spreadsheet).getData();
    }

    private String bankBlzCode;

    public GetBankDetails(String bankBlzCode)
    {
        this.bankBlzCode=bankBlzCode;
    }

    @Test
    public void getBankDetail() throws Exception
    {
        String getBankDetailsXML = "<soapenv:Envelope \n" +
                "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "    xmlns:blz=\"http://thomas-bayer.com/blz/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <blz:getBank>\n" +
                "            <blz:blz>" + bankBlzCode + "</blz:blz>\n" +
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

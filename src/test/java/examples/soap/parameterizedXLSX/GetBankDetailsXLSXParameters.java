/**
 * @Author Gladson Antony
 * @Date 10-OCT-2018
 */

package examples.soap.parameterizedXLSX;


import dataprovider.XlsxSheetDataProvider;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.Qualifier;
import net.thucydides.junit.annotations.TestData;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@RunWith(SerenityParameterizedRunner.class)
public class GetBankDetailsXLSXParameters
{
    private @Getter @Setter String bankBlzCode1;
    private @Getter @Setter String bankBlzCode2;

    public GetBankDetailsXLSXParameters(String bankBlzCode1,String bankBlzCode2)
    {
        super();
        this.bankBlzCode1 = bankBlzCode1;
        this.bankBlzCode2 = bankBlzCode2;
    }

    @TestData
    public static Collection xlsxSheetData() throws Exception
    {
        InputStream xlsxSpreadsheet = new FileInputStream("./src/test/resources/getBankDetails.xlsx");
        return new XlsxSheetDataProvider(xlsxSpreadsheet).getData();
    }

    @Qualifier
    public String getQualifier()
    {
        return this.bankBlzCode1;
    }

    @Test
    public void shouldGetBankBlzCode() throws Exception
    {
        String getBankDetailsXML = "<soapenv:Envelope \n" +
                "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "    xmlns:blz=\"http://thomas-bayer.com/blz/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <blz:getBank>\n" +
                "            <blz:blz>" + this.bankBlzCode1 + "</blz:blz>\n" +
                "        </blz:getBank>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        try
        {
            FileWriter file = new FileWriter("./src/test/resources/Requests/" + bankBlzCode1 + "_Request.xml");
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
            FileWriter file = new FileWriter("./src/test/resources/Response/" + bankBlzCode1 + "_Response.xml");
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

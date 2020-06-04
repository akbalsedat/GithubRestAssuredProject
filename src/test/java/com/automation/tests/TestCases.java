package com.automation.tests;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCases {
    @BeforeAll
    public static void beforeAll() {
        baseURI = ConfigurationReader.getProperty("UI_API_URI");
    }

    /**1.Send a get request without providing any parameters
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that name, surname, gender, region fields have value
     */
    @Test
    public void uiNamesAPITestingCase1(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().contentType("application/json; charset=utf-8");
        response.then().assertThat().body("name", notNullValue());
        response.then().assertThat().body("surname", notNullValue());
        response.then().assertThat().body("gender", notNullValue());
        response.then().assertThat().body("region", notNullValue());
    }

    /**Gender test
     * 1.Create a request by providing query parameter: gender, male or female
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that value of gender field is same from step 1
     */
    @Test
    public void uiNamesAPITestingCase2(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("gender", "male").
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().contentType("application/json; charset=utf-8");
        response.then().assertThat().body("gender", equalTo("male"));

        Response response1 = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("gender", "female").
                when().
                get(baseURI).prettyPeek();
        response1.then().assertThat().statusCode(200);
        response1.then().assertThat().contentType("application/json; charset=utf-8");
        response1.then().assertThat().body("gender", equalTo("female"));
    }

    /**2 params test
     * 1.Create a request by providing query parameters: a valid region and gender
     * NOTE: Available region values are given in the documentation
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that value of gender field is same from step 1
     * 4.Verify that value of region field is same from step 1
     */
    @Test
    public void uiNamesAPITestingCase3(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("gender", "female").
                queryParam("region", "Turkey").
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().contentType("application/json; charset=utf-8");
        response.then().assertThat().body("gender", equalTo("female"));
        response.then().assertThat().body("region", is("Turkey"));
    }

    /**Invalid gender test
     * 1.Create a request by providing query parameter: invalid gender
     * 2.Verify status code 400 and status line contains Bad Request
     * 3.Verify that value of error field is Invalid gender
     */
    @Test
    public void uiNamesAPITestingCase4(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("gender", "invalid gender").
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(400);
        response.then().assertThat().statusLine(containsString("Bad Request"));
        response.then().assertThat().body("error", is("Invalid gender"));
    }

    /**Invalid region test
     * 1.Create a request by providing query parameter: invalid region
     * 2.Verify status code 400 and status line contains Bad Request
     * 3.Verify that value of error field is Regionorlanguagenotfound
     */
    @Test
    public void uiNamesAPITestingCase5(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("region", "invalid region").
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(400);
        response.then().assertThat().statusLine(containsString("Bad Request"));
        response.then().assertThat().body("error", is("Region or language not found"));
    }

    /**Amount and regions test
     * 1.Create request by providing query parameters: a valid region and amount(must be bigger than 1)
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that all objects have different name+surname combination
     */
    @Test
    public void uiNamesAPITestingCase6(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("region", "Georgia").
                queryParam("amount", 3).
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(200);
        List<String> names = new ArrayList<>();
        List<String> sameNames = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
             String fullName = response.then().extract().response().body().path("["+index+"].name") +" "+
                     response.then().extract().response().body().path("["+index+"].surname");
           names.add(fullName);
        }
        
        for (int i = 0; i < names.size(); i++) {
            for (int j = 1; j < names.size(); j++) {
                if (names.get(i) == names.get(j) && i != j) {
                    // same names are found
                    sameNames.add(names.get(i));
                    break;
                }
            }
        }
        System.out.println("sameNames = " + sameNames);
    }

    /**3 params test
     * 1.Create a request by providing query parameters: a valid region, gender and amount (must be biggerthan 1)
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that all objects the response have the same region and gender passed in step 1
     */
    @Test
    public void uiNamesAPITestingCase7(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("region", "Georgia").
                queryParam("amount", 3).
                queryParam("gender", "male").
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(200);
        for (int index = 0; index < 3; index++) {
            assertEquals("Georgia", response.then().extract().response().body().path("["+index+"].region"));
            assertEquals("male", (response.then().extract().response().body().path("["+index+"].gender")));
        }
    }

    /**Amount count test
     * 1.Create a request by providing query parameter: amount (must be bigger than 1)
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify that number of objects returned in the response is same as the amount passed in step
     */
    @Test
    public void uiNamesAPITestingCase8(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                queryParam("amount", 3).
                when().
                get(baseURI).prettyPeek();
        response.then().assertThat().statusCode(200);

       response.then().assertThat().body("list.size()", is(3));

    }
}
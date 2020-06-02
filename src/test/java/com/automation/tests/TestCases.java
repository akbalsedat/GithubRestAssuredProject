package com.automation.tests;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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

}

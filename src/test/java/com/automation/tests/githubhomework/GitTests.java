package com.automation.tests.githubhomework;

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

public class GitTests {
    @BeforeAll
    public static void beforeAll() {
        baseURI = ConfigurationReader.getProperty("GITHUB_URI");
    }

    @Test
    public void GitTestingCase1(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                pathParam("org", "cucumber").
                when().
                get("/orgs/").prettyPeek();
    }
}

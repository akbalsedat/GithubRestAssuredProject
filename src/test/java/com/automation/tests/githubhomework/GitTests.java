package com.automation.tests.githubhomework;

import com.automation.utilities.ConfigurationReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.codehaus.groovy.ast.ImportNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class GitTests {
    @BeforeAll
    public static void beforeAll() {
        baseURI = ConfigurationReader.getProperty("GITHUB_URI");
    }

    /**Verify organization information
     * 1.Send a get request to /orgs/:org. Request includes :•Path param org with value cucumber
     * 2.Verify status code 200, content type application/json; charset=utf-8
     * 3.Verify value of the login field is cucumber
     * 4.Verify value of the name field is cucumber
     * 5.Verify value of the id field is 320565
     */
    @Test
    public void GitTestingCase1(){
        Response response = given().
                baseUri(baseURI).
                contentType(ContentType.JSON).
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}").prettyPeek();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().contentType("application/json; charset=utf-");
        response.then().assertThat().body("login", is("cucumber"));
        response.then().assertThat().body("name", is("Cucumber"));
        response.then().assertThat().body("id", is(320565));
    }
    /**Verify error message
     * 1.Send a get request to /orgs/:org.
     * Request includes :•Header Accept with value application/xml•Path param org with value cucumber
     * 2.Verify status code 415, content type application/json; charset=utf-8
     * 3.Verify response status line include message Unsupported Media Type
     */
    @Test
    public void GitTestingCase2(){
        Response response = given().
                baseUri(baseURI).
                accept("application/xml").
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}").prettyPeek();
        response.then().assertThat().statusCode(415);
        response.then().assertThat().contentType("application/json; charset=utf-");
        response.then().assertThat().statusLine(containsString("Unsupported Media Type"));
    }
    /**Number of repositories
     * 1.Send a get request to /orgs/:org. Request includes :•Path param org with value cucumber
     * 2.Grab the value of the field public_repos
     * 3.Send a get request to /orgs/:org/repos. Request includes :•Path param org with value cucumber
     * 4.Verify that number of objects in the response  is equal to value from step 2
     */
    @Test
    public void GitTestingCase3() {
        Response response = given().
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}");

        int number = response.jsonPath().getInt("public_repos");
        System.out.println("number = " + number);

        Response response1 = given().
                queryParam("per_page", 100). // ???
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}/repos").prettyPeek();

        List<Integer> objectList = response1.jsonPath().getList("id");
        objectList.forEach(each -> System.out.println("each = " + each));
        assertEquals(number, objectList.size());
    }
    /**Repository id information
     * 1.Send a get request to /orgs/:org/repos. Request includes :•Path param org with value cucumber
     * 2.Verify that id field is unique in every in every object in the response
     * 3.Verify that node_id field is unique in every in every object in the response
     */

    @Test
    public void GitTestingCase4() {
        Response response = given().
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}/repos");

        List<Integer> idList = response.jsonPath().getList("id");
        List<String> nodeIdList = response.jsonPath().getList("node_id");
        System.out.println("nodeIdList = " + nodeIdList);
        Collections.sort(idList);
        for (int index = 0; index < idList.size() - 1; index++)
            assertNotEquals(idList.get(index), idList.get(index+1));

        for (int i = 0; i < nodeIdList.size(); i++) {
            for (int j = 1; j < nodeIdList.size(); j++) {
                if (nodeIdList.get(i).equals(nodeIdList.get(j)) && i != j) {
                    // check out if there are same node IDs
                    assertNull(nodeIdList.get(i));
                }
            }
        }
    }

    /**Repository owner information
     * 1.Send a get request to /orgs/:org. Request includes :•Path param org with value cucumber
     * 2.Grab the value of the field id
     * 3.Send a get request to /orgs/:org/repos. Request includes :•Path param org with value cucumber
     * 4.Verify that value of the id inside the owner object in every response is equal to value from step 2
     */
    @Test
    public void GitTestingCase5() {
        Response response = given().
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}");
        int id = response.jsonPath().getInt("id");

        Response response1 = given().
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}/repos");
        List<Integer> idList = response1.jsonPath().getList("owner.id");
        for (Integer each:
             idList)
            assertEquals(id, each);
    }

    /**Ascending order by full_name sort
     * 1.Send a get request to /orgs/:org/repos. Request includes :
     * •Path param org with value cucumber
     * •Query param sort with value full_name
     * 2.Verify that all repositories are listed in alphabetical order based on the value of the field name
     */
    @Test
    public void GitTestingCase6() {
        Response response1 = given().
                pathParam("org", "cucumber").
                queryParam("sort", "full_name").
                when().
                get("/orgs/{org}/repos");
        
        List<String> nameList = response1.jsonPath().getList("name");
        System.out.println("nameList = " + nameList);
        for (int i = 0; i < nameList.size() - 1; i++)
                    assertTrue(nameList.get(i).compareTo(nameList.get(i+1)) < 0);
    }
    /**Descending order by full_name sort
     * 1.Send a get request to /orgs/:org/repos. Request includes :
     * •Path param org with value cucumber
     * •Query param sort with value full_name
     * •Query param direction with value desc
     * 2.Verify that all repositories are listed in reverser alphabetical order based on the value of the field name
     */
    @Test
    public void GitTestingCase7() {
        Response response1 = given().
                pathParam("org", "cucumber").
                queryParam("sort", "full_name").
                queryParam("direction", "desc").
                when().
                get("/orgs/{org}/repos");

        List<String> nameList = response1.jsonPath().getList("name");
        System.out.println("nameList = " + nameList);
        for (int i = 0; i < nameList.size() - 1; i++)
            assertTrue(nameList.get(i).compareTo(nameList.get(i+1)) > 0);
    }

    /**Default sort
     * 1.Send a get request to /orgs/:org/repos. Request includes :
     * •Path param org with value cucumber
     * 2.Verify that by default all repositories are listed in descending order based on the value of the field created_at
     */
    @Test
    public void GitTestingCase8() {
        Response response1 = given().
                pathParam("org", "cucumber").
                when().
                get("/orgs/{org}/repos").prettyPeek();

        List<String> repoList = response1.jsonPath().getList("created_at");
        for (int i = 0; i < repoList.size() - 1; i++)
            assertTrue(repoList.get(i).compareTo(repoList.get(i+1)) < 0);
    }
}


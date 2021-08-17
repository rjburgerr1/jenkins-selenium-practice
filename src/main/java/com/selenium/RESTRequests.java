package com.selenium;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


/**
 * This class serves as practice for testing API requests using the okhttp http client
 */
public class RESTRequests {
    // Create http client for REST API testing
    OkHttpClient httpClient = new OkHttpClient();
    // Base url for Jenkins
    private String BASE_URL = "http://192.168.1.243:8080";
    private enum TYPES {POST, PUT, GET, PATCH, DELETE};
    // Credentials
    String username;
    String password;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    String randomString(int len){

        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * Test for rapidapi.com's imdb API
     * Sends a GET request to rapidapi's auto-complete endpoint
     * This API gets auto-complete suggestions for a given search term
     * Test PASSES if http response code of 2xx and response message is "OK", otherwise fails
     *
     */
    @Test(groups = {"imdb", "positive-tests"})
    public void imdbGETRequest() {
        try {
            // IMDb endpoint
            String endpoint = "https://imdb8.p.rapidapi.com/auto-complete";

            // Build URL
            HttpUrl url = HttpUrl.parse("https://imdb8.p.rapidapi.com/auto-complete").newBuilder()
                    .addQueryParameter("q", "game of thr")
                    .build();

            // Build request
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                    .build();

            // Make API request
            Response response = httpClient.newCall(request).execute();
            System.out.println(response);

            // Retrieve the response code
            String resStr = String.valueOf(response.code());
            // Assert that response code is in the 200s, representing an OK request
            Assert.assertEquals(resStr.substring(0, 1), "2");
            // Assert that response message is "OK"
            Assert.assertEquals(response.message(), "OK");
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }

    }

    /**
     * Test for rapidapi.com's idmb API
     * This test builds an invalid GET request by passing incorrect credentials for signing into the imdb API
     * Test PASSES if http response code of 403 is received and response message is 'Forbidden', otherwise fails
     *
     */
    @Test(groups = {"imdb", "negative-tests", "security-tests"})
    public void imdbWrongAccessKey() {
        try {
            // IMDb endpoint
            String endpoint = "https://imdb8.p.rapidapi.com/auto-complete";

            // Build request
            Request request = new Request.Builder()
                    .url("https://imdb8.p.rapidapi.com/auto-complete?q=game%20of%20thr")
                    .get()
                    .addHeader("x-rapidapi-key", "wrongACcesKey")
                    .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                    .build();

            // Make API request
            Response response = httpClient.newCall(request).execute();

            // Assert that response is 403 - Forbidden
            Assert.assertEquals(response.code(), 403);
            Assert.assertEquals(response.message(), "Forbidden");
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    /**
     * Test for rapidapi.com's idmb API
     * This test builds an invalid GET request by passing an incomplete url, missing a query parameter, for the imdb API
     * Test PASSES if http response code of 400 is received and resposne message contains "Bad", otherwise fails
     */
    @Test(groups = {"imdb", "negative-tests"})
    public void imdbMissingQueryParameter()  {
        try {
            // IMDb endpoint
            String endpoint = "https://imdb8.p.rapidapi.com/auto-complete";

            // Build request
            Request request = new Request.Builder()
                    .url("https://imdb8.p.rapidapi.com/auto-complete?")
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                    .build();

            // Make API request
            Response response = httpClient.newCall(request).execute();

            // Assert that response code is 400 - Bad request
            Assert.assertEquals(response.code(), 400);
            Assert.assertTrue(response.message().contains("Bad"));
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    /**
     * Test for rapidapi.com's idmb API
     * This test validates that the payload in the http response after sending a GET request matches a JSON object as expected for the imdb API
     * Test PASSES if http response is contains an instance of a JSON Object, otherwise fails
     */
    @Test(groups = {"imdb", "negative-tests", "security-tests"})
    public void imdbGETValidatePayload() {
        try {
            // IMDb endpoint
            String endpoint = "https://imdb8.p.rapidapi.com/auto-complete";

            // Build request
            Request request = new Request.Builder()
                    .url("https://imdb8.p.rapidapi.com/auto-complete?q=game%20of%20thr")
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                    .build();

            // Make API request
            Response response = httpClient.newCall(request).execute();


            String jsonResponse = response.body().string();
            JSONObject responseObject = new JSONObject(jsonResponse);
            // Assert that response is a well-formed JSON object
            Assert.assertTrue(responseObject instanceof JSONObject);
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    /**
     * Test for rapidapi.com's imdb API
     * Sends a GET request to rapidapi's "/auto-complete" endpoint and then uses the response from that GET request
     * as the query parameter in a GET request to the "/actors/get-bio?" endpoint
     * This API gets a bio for a given actor code
     * Test PASSES if http response code of 2xx and resposne message contains "OK", otherwise fails
     *
     */
    @Test(groups = {"imdb", "positive-tests"})
    public void imdbGetActorBio() {
        try {
            // IMDb endpoint
            String endpoint = "https://imdb8.p.rapidapi.com/auto-complete";

            // Get actor code
            Request request = new Request.Builder()
                    .url("https://imdb8.p.rapidapi.com/actors/list-most-popular-celebs?homeCountry=US&currentCountry=US&purchaseCountry=US")
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                    .build();

            Response response = httpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();

            String[] actorNameCodes = responseBody.string().split("\"");
            String actorNameCode = actorNameCodes[1].split("/")[2];


            // Build request for getting actors bio
            request = new Request.Builder()
                    .url("https://imdb8.p.rapidapi.com/actors/get-bio?nconst=" + actorNameCode)
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                    .build();

            // Make API request
            response = httpClient.newCall(request).execute();

            // Retrieve the response code
            String resStr = String.valueOf(response.code());
            // Assert that response code is in the 200s - OK
            Assert.assertEquals(resStr.substring(0, 1), "2");
            Assert.assertTrue(response.message().contains("OK"));
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    /**
     * Test for rapidapi.com's yahoo finance API
     * Sends a POST request to rapidapi's "/news/v2/list?" endpoint
     * This API gets news for a given region based on a given search term
     * Test PASSES if http response code of 2xx and response message contains "OK",
     * and the response data contains "main" and "data" objects returned by yahoo finance, otherwise fails
     *
     */
    @Test(groups = {"yahoo-finance", "positive-tests"})
    public void yahooFinanceListNews()  {
        try {
            // Set request media type
            MediaType mediaType = MediaType.parse("text/plain");
            // Build request body
            RequestBody body = RequestBody.create(mediaType, ""); // Empty search query
            // Build request
            Request request = new Request.Builder()
                    .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/news/v2/list?region=US&snippetCount=28")
                    .post(body)
                    .addHeader("content-type", "text/plain")
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                    .build();

            Response response = httpClient.newCall(request).execute();

            String jsonResponse = response.body().string();
            JSONObject responseObject = new JSONObject(jsonResponse);


            // Assert that response code is 200 - OK
            Assert.assertEquals(response.code(), 200);
            Assert.assertTrue(response.message().contains("OK"));
            Assert.assertTrue(responseObject.has("data")); // Check if response data has 'data' object
            Assert.assertTrue(responseObject.get("data").toString().contains("main")); // Check if response data has 'main' object
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    @Test(groups = {"yahoo-finance", "positive-tests"})
    public void yahooFinanceListNewsWithSearchQuery() {
        try {
            // Set request media type
            MediaType mediaType = MediaType.parse("text/plain");
            // Build request body
            RequestBody body = RequestBody.create(mediaType, "Trump");
            // Build request
            Request request = new Request.Builder()
                    .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/news/v2/list?region=US&snippetCount=28")
                    .post(body)
                    .addHeader("content-type", "text/plain")
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                    .build();

            Response response = httpClient.newCall(request).execute();

            String jsonResponse = response.body().string();
            JSONObject responseObject = new JSONObject(jsonResponse);

            // Assert that response code is 200 - OK
            Assert.assertEquals(response.code(), 200);
            Assert.assertTrue(responseObject.has("data")); // Check if response data has 'data' object
            Assert.assertTrue(responseObject.get("data").toString().contains("main")); // Check if response data has 'main' object
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    /**
     * Test for rapidapi.com's yahoo finance API
     * Sends a POST request to rapidapi's "/news/v2/list?" endpoint
     * This API gets news for a given region based on a empty query parameter
     * Test PASSES if http response code of 2xx and response message contains "OK",
     * and the response data contains "main" and "data" objects returned by yahoo finance, otherwise fails
     *
     */
    @Test(groups = {"yahoo-finance", "negative-tests"})
    public void yahooFinanceListNewsEmptyPayload() {
        try {
            // Set request media type
            MediaType mediaType = MediaType.parse("text/plain");
            // Build request body
            RequestBody body = RequestBody.create(null, new byte[0]);
            // Build request
            Request request = new Request.Builder()
                    .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/news/v2/list?region=US&snippetCount=28")
                    .post(body)
                    .addHeader("content-type", "text/plain")
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                    .build();

            Response response = httpClient.newCall(request).execute();

            String jsonResponse = response.body().string();
            JSONObject responseObject = new JSONObject(jsonResponse);

            // Assert that response code is 200 - OK
            Assert.assertEquals(response.code(), 200);
            Assert.assertTrue(responseObject.has("data")); // Check if response data has 'data' object
            Assert.assertTrue(responseObject.get("data").toString().contains("main")); // Check if response data has 'main' object
        } catch (Exception error) {
            Assert.fail(error.getMessage());
        }
    }

    /**
     * Test for rapidapi.com's google search API
     * Sends a POST request to rapidapi's "/api/v1/search" endpoint
     * This API completes a google search with given search term and a limit for the number of results
     * Test PASSES if http response code of 2xx and response message contains "OK", otherwise fails
     *
     */
    @Test(groups = {"google-search", "positive-tests"})
    public void googleSearchSERP() throws IOException {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\"query\": \"q=google+search+api&num=100\",\r\"website\": \"https://rapidapi.com\"\r }");
            Request request = new Request.Builder()
                    .url("https://google-search3.p.rapidapi.com/api/v1/serp/")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "google-search3.p.rapidapi.com")
                    .build();

            Response response = httpClient.newCall(request).execute();

            Assert.assertEquals(response.code(), 200);
            Assert.assertTrue(response.message().contains("OK"));
    }

    /**
     * Test for rapidapi.com's google search API
     * Sends a POST request to rapidapi's "/api/v1/search" endpoint
     * This API completes a google search with given search term and a limit for the number of results
     * Test PASSES if http response code of 2xx and response message contains "OK", otherwise fails
     *
     */
    @Test(groups = {"google-search", "positive-tests"})
    public void googleSearch() throws IOException {
            Request request = new Request.Builder()
                    .url("https://google-search3.p.rapidapi.com/api/v1/search/q=elon+musk&num=100")
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "google-search3.p.rapidapi.com")
                    .build();
            Response response = httpClient.newCall(request).execute();

            Assert.assertEquals(response.code(), 200);
            Assert.assertTrue(response.message().contains("OK"));
    }

    /**
     * Test for rapidapi.com's google search API
     * Sends a POST request to rapidapi's "/api/v1/search" endpoint
     * This API completes a google search with given search term and a limit for the number of results
     * Test PASSES if http response code of 2xx and response message contains "OK", otherwise fails
     *
     */
    @Test(groups = {"google-search", "negative-tests"})
    public void googleSearchInvalidInput() throws IOException, JSONException {
        try {
        String tooLongStr = "q=" + randomString(10000) + "&num=100";
        String url = "https://google-search3.p.rapidapi.com/api/v1/search/" + tooLongStr;
            Request request = new Request.Builder()
                    .url(url) //
                    .get()
                    .addHeader("x-rapidapi-key", "6aeaa7e413msh1162787d813bf8ap17dec0jsnd4d4efb2e864")
                    .addHeader("x-rapidapi-host", "google-search3.p.rapidapi.com")
                    .build();
            httpClient.newCall(request).execute();

        } catch (Exception error) {
            Assert.fail(String.valueOf(error.getCause()));
        }

    }


    @Test
    public void buildJenkinsJobWithToken() throws Exception {
        // Set Jenkins credentials
        setCredentials();
        // Create http client for REST API testing
        OkHttpClient httpClient = new OkHttpClient();
        // Jenkins Job endpoint to test
        String endpoint =  BASE_URL + "/job/Negative-TestNG-Tests/build";

        // Create blank request body for request builder to run with POST request
        RequestBody reqbody = RequestBody.create(null, new byte[0]);

        // Build request
        Request request = new Request.Builder().url(endpoint)
                .addHeader("Authorization", Credentials.basic(username, password)) // Set header for authenticating client with Jenkins
                .post(reqbody) // Add empty request body
                .build();
        // Make API request
        Response response = httpClient.newCall(request).execute();

        // Retrieve the response code
        String resStr = String.valueOf( response.code());
        // Assert that response code is in the 200s
        Assert.assertEquals( resStr.substring(0,1), "2");
    }






    private Response post(OkHttpClient httpClient, String url, RequestBody body) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            return response;
        }
    }


    // Failed attempt to authenticate client :(
    private static OkHttpClient createAuthenticatedClient(final String username,
                                                          final String password) {
        // build client with authentication information.
        OkHttpClient httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();
        return httpClient;
    }

    private static Response doRequest(OkHttpClient httpClient, String anyURL) throws Exception {
        Request request = new Request.Builder().url(anyURL).build();
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        System.out.println(response.body().string());
        return response;
    }

    public void setCredentials() throws IOException {
        Properties login = new Properties();
        try {
            FileReader in = new FileReader("E:\\Users\\rjbra\\eclipse-workspace\\Selenium\\src\\login.properties");
            login.load(in);
        } catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        this.username = login.getProperty("username");
        this.password = login.getProperty("password");

    }






}


//curl -X POST -L --user rjburgerr1:115b825fbadcfcb0646235856b0f7582ed http://192.168.1.243:8080/job/Negative-TestNG-Tests/build
// curl -v -X GET http://192.168.1.243:8080/crumbIssuer/api/json --user rjburgerr1:115b825fbadcfcb0646235856b0f7582ed
// Request request = new Request.Builder().url("http://192.168.1.243:8080/crumbIssuer/api/json")
//                .addHeader("Authorization", Credentials.basic(username, "115b825fbadcfcb0646235856b0f7582ed"))
//        .build();
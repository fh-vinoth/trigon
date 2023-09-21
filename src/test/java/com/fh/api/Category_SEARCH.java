package com.fh.api;

import com.fh.core.TestLocalController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class Category_SEARCH extends TestLocalController {

    private static final Logger logger = LogManager.getLogger(Category_SEARCH.class);

    @Test (enabled = true)
    public void searchCategory_test() {
        try{
            author_ScenarioName("Gayathri", "Searching the particular category from category/search endpoint");


//            System.out.println("Test");
            logStepAction("Creating category and getting the category id");
            hardWait(30000);
            String category_id = createCategory();
            hardWait(5000);
            logger.info("Created category_id: " + category_id);

            logStepAction("Searching the created category");
//            searchCategory(category_id);

            createCategory();

           /* updateCategory(category_id, "thursday", "0");


            logStepAction("Deleting the created category");
            deleteCategory(category_id);
*/
        }catch (Exception e){
            hardFail("Failes "+e);
        }finally {
            testTearDown();
        }

    }

    @Test(enabled = false)
    public void searchCategory_test2() {
        try{
            author_ScenarioName("Reddy", "Searching the particular category from category/search endpoint2");

            logStepAction("Creating category and getting the category id");
            String category_id = createCategory();
            logger.info("Created category_id: " + category_id);

        }catch (Exception e){
            hardFail("Failed due to :: "+e);
        }finally {
            testTearDown();
        }

    }

    @Test(enabled = false)
    public void searchCategory_test3() {
        try{
            author_ScenarioName("Reddy", "Searching the particular category from category/search endpoint3");

            logStepAction("Creating category and getting the category id");
            String category_id = createCategory();
            logger.info("Created category_id: " + category_id);

        }catch (Exception e){
            hardFail(e);
        }finally {
            testTearDown();
        }

    }
    @Test(enabled = false)
    public void searchCategory_test4() {
        try{
            author_ScenarioName("Bhaskar", "Searching the particular category from category/search endpoint");

            logStepAction("Creating category and getting the category id");
            String category_id = createCategory();
            logger.info("Created category_id: " + category_id);

            logStepAction("Searching the created category");
            searchCategory(category_id);

            logStepAction("Deleting the created category");
            deleteCategory(category_id);

        }catch (Exception e){
            hardFail(e);
        }finally {
            testTearDown();
        }

    }

    public String createCategory() {
        //logStepAction("Creating Categorey");
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> formparams = new HashMap<>();
        Map<String, Object> queryparams = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Store", tEnv().getApiStore());
        queryparams.put("api_token", tEnv().getApiToken());
        String category_id = null;
        String endpoint = "category";
        formparams.put("name", "TestCateg" + cUtils().generateRandomString(4));
        try {
            Map<String, Object> postresponse = api().validateStaticResponse("POST", endpoint, headers, null, queryparams, formparams, null, null, "201", null);
            System.out.println(postresponse);
//            String s = db.sendQuery("select * from config where host = 'automation-uk1.t2scdn.com'", 1);
//            System.out.println(s);
            category_id = postresponse.get("resource_id").toString();

        } catch (Exception e) {
            hardFail("Category is not created."+e);
        }
        if (category_id == null) {
            hardFail("Category id is returned as null.");
        }
        return category_id;
    }

    public void searchCategory(String category_id) {

        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> formparams = new HashMap<>();
        Map<String, Object> queryparams = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Store", tEnv().getApiStore());
        queryparams.put("api_token", tEnv().getApiToken());

        String endPoint = "category/search";
        formparams.put("q", "id="+category_id+"" );

        try {
            api().validateStaticResponse("POST", endPoint, headers, null, queryparams, formparams, null, null, "201", null);
        } catch (Exception e) {
            hardFail("Searching Category is not found");
        }
    }


    public Map<String, Object> updateCategory(String categoryId, String parameterToUpdate, String valueToUpdate) {
        logStepAction("Update parameter " + parameterToUpdate + " with value " + valueToUpdate + " for the Category with id " + categoryId);

        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> formparams = new HashMap<>();
        Map<String, Object> queryparams = new HashMap<>();

        Map<String, Object> response = new HashMap<>();
        try {

            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Store", tEnv().getApiStore());
            queryparams.put("api_token", tEnv().getApiToken());
            formparams.put(parameterToUpdate, valueToUpdate);
            String endpoint = "category/" + categoryId;
            response = api().validateStaticResponse("PUT", endpoint, headers, null, queryparams, formparams, null, null, "201", null);
        } catch (Exception e) {
            hardFail("Failed to update category");
        }
        return response;
    }


    public void deleteCategory(String category_id) {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> queryparams = new HashMap<>();
        Map<String, Object> expectedResponse = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Store", tEnv().getApiStore());
        queryparams.put("api_token", tEnv().getApiToken());

        String endpoint = "category/" + category_id;
        expectedResponse.put("outcome","success");

        try {
            api().validateStaticResponse("DELETE", endpoint, headers, null, queryparams, null, null, null, "200", expectedResponse);
        } catch (Exception e) {
            hardFail("Category is not deleted.");
        }
    }

}

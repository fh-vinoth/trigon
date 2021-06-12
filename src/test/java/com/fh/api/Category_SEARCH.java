package com.fh.api;

import com.fh.core.TestLocalController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class Category_SEARCH extends TestLocalController {

    private static final Logger logger = LogManager.getLogger(Category_SEARCH.class);

    @Test
    public void searchCategory_test() {
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
            category_id = postresponse.get("resource_id").toString();

        } catch (Exception e) {
            hardFail("Category is not created.");
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
        formparams.put("q", category_id);

        try {
            api().validateStaticResponse("POST", endPoint, headers, null, queryparams, formparams, null, null, "200", null);
        } catch (Exception e) {
            hardFail("Searching Category is not found");
        }
    }

    public void deleteCategory(String category_id) {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> queryparams = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Store", tEnv().getApiStore());
        queryparams.put("api_token", tEnv().getApiToken());

        String endpoint = "category/" + category_id;

        try {
            api().validateStaticResponse("DELETE", endpoint, headers, null, queryparams, null, null, null, "200", null);
        } catch (Exception e) {
            hardFail("Category is not deleted.");
        }
    }

}
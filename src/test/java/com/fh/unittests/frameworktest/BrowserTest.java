package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class BrowserTest extends TestLocalController {

    @Test(groups = { "Regression"})
    public void abcTest() {
        try{
            author_ScenarioName("Bhaskar", "Searching the particular category from category/search endpoint");
            logStepAction("Creating category and getting the category id");
            String category_id = createCategory();
            System.out.println(category_id);

//            browser().findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]")).sendKeys("iphone");
//            logStepAction(" Performing Search click");
//            browser().findElement(By.xpath("(//input[@type=\"submit\"])[1]")).click();
//            logReport("PASS","Test Passed");
            //hardFail("test failed");
        }catch (Exception e){
            hardFail(e);
        }finally {
            testTearDown();
        }

    }

    public String createCategory() {
        logScenario("Creating Categorey");
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
//    @Test(groups = {"sanity", "Regression"})
//    public void abcTest1() {
//        author_ScenarioName("Bhaskar", "Browser Test   1");
//        logStepAction(" Performing Search");
//        browser().findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]")).sendKeys("iphone");
//        logStepAction(" Performing Search click");
//        browser().findElement(By.xpath("(//input[@type=\"submit\"])[1]")).click();
//
//    }
}

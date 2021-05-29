package com.trigon.testrail;


public class Client {

    final APIClient apiClient;

    /**
     * For reporting test case result this steps need to be done
     * 1. Create run from project by projectId
     * 2. Start test case execution
     * 3. After test case is finished, update test case by runId and testCaseId
     * 4. When all test cases finished, close run by closeRunById
     **/

    public Client() {
        apiClient = new APIClient("https://touch2success.testrail.com/");
        apiClient.setUser("bhaskar.marrikunta@touch2success.com");
        apiClient.setPassword("aERRvHt0Psd3TNyeGbXp-31X/Y9JI8lvJ5HVA49P/");
    }
}

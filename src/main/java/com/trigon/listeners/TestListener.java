package com.trigon.listeners;

import com.trigon.reports.ReportManager;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.influxdb.dto.Point;
import java.util.concurrent.TimeUnit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

public class TestListener extends ReportManager implements IAnnotationTransformer, ITestListener, IRetryAnalyzer  {
    int counter = 0;
    int retryLimit = 1;

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(TestListener.class);
    }

    @Override
    public void onFinish(ITestContext context) {
        Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
        for (ITestResult temp : failedTests) {
            ITestNGMethod method = temp.getMethod();
            if (context.getFailedTests().getResults(method).size() > 1) {
                failedTests.remove(temp);
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    failedTests.remove(temp);
                }
            }
        }
        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
        for (ITestResult temp : skippedTests) {
            ITestNGMethod method = temp.getMethod();
            if (context.getSkippedTests().getResults(method).size() > 1) {
                skippedTests.remove(temp);
            }
        }
    }

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
        this.postTestMethodStatus(result, "PASS");
    }

    public void onTestFailure(ITestResult result) {
        this.postTestMethodStatus(result, "FAIL");
    }

    public void onTestSkipped(ITestResult result) {
        this.postTestMethodStatus(result, "SKIPPED");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onStart(ITestContext context) {
    }

    private void postTestMethodStatus(ITestResult iTestResult, String status) {
        Point point = Point.measurement("testmethod").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("testclass", iTestResult.getTestClass().getName()).tag("name", iTestResult.getName())
                .tag("description", iTestResult.getMethod().getDescription()).tag("result", status)
                .addField("duration", (iTestResult.getEndMillis() - iTestResult.getStartMillis())).build();
        UpdateResults.post(point);
    }
    private void postTestClassStatus(ITestContext iTestContext) {
        Point point = Point.measurement("testclass").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("name", iTestContext.getAllTestMethods()[0].getTestClass().getName())
                .addField("duration", (iTestContext.getEndDate().getTime() - iTestContext.getStartDate().getTime()))
                .build();
        UpdateResults.post(point);
    }

    @Override
    public boolean retry(ITestResult result) {

        if (counter < retryLimit) {
            counter++;
            return true;
        }
        return false;
    }

}
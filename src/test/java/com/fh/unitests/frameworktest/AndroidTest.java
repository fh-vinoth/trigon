package com.fh.unitests.frameworktest;

import com.fh.core.TestLocalController;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class AndroidTest extends TestLocalController {

    @Test
    public void androidTest() {
        author_ScenarioName("bhaskar_parallelClass1_Method1", "parallelClass1_Method1 scenario");
        System.out.println("Calculate sum of two numbers");
        android().findElement(By.xpath("//*[@text='1']")).click();
        android().findElement(By.xpath("//*[@text='+']")).click();
        android().findElement(By.xpath("//*[@text='2']")).click();
        android().findElement(By.xpath("//*[@content-desc='equals']")).click();
    }
}

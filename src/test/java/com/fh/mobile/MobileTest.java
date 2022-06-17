package com.fh.mobile;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.Map;

public class MobileTest extends TestLocalController {

    @Test(groups = {"Sanity_MYT, Sanity_Fusion"})
    public void sanity_Orders_AllRegions() {
        try {
            author_ScenarioName("Srikrishna", "To check if the build fulfills the acceptance criteria");
            //sample();
            System.out.println("Testing");
        } catch (Exception e) {
            hardFail("sanity failed");
        } finally {
            testTearDown();
        }
    }


    public void sample(){
        try{
            String num = "1000";
            String Exp = "11.34";
            int difference = Integer.parseInt(num)-Integer.parseInt(Exp);
            System.out.println(difference);
        }catch(Exception e){
            hardFail("Failed Sample");
        }
    }
}

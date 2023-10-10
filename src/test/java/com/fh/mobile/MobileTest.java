package com.fh.mobile;

import com.fh.core.TestLocalController;
import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class MobileTest extends TestLocalController {

    @Test(groups = {"Sanity_MYT, Sanity_Fusion"})
    public void sanity_Orders_AllRegions() {
        try {
            author_ScenarioName("Srikrishna", "To check if the build fulfills the acceptance criteria");
            //sample();
            System.out.println("Testing");

            /*HashMap<String,Object> map = new HashMap<>();
            map.put("offline", false);
            map.put("latency", 5);
            map.put("download_throughput", 75000);
            map.put("upload_throughput", 20000);
            CommandExecutor executor = browser().getCommandExecutor();
            Response response = executor.execute(new Command(browser().getSessionId(),"setNetworkConnection", ImmutableMap.of("network_connection",ImmutableMap.copyOf(map))));
            System.out.println(response);*/

        } catch (Exception e) {
            hardFail("sanity failed");
        } finally {
            testTearDown();
        }
    }


    /*public void sample(){
        try{
            String num = "1000";
            String Exp = "11.34";
            int difference = Integer.parseInt(num)-Integer.parseInt(Exp);
            System.out.println(difference);
        }catch(Exception e){
            hardFail("Failed Sample");
        }
    }*/
}

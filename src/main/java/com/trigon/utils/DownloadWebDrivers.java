package com.trigon.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.Test;

public class DownloadWebDrivers {

    @Test
    public void downloadWebDrivers(){
        try {
//            WebDriverManager.chromedriver().config().setAvoidAutoReset(true);
            WebDriverManager.chromedriver().clearResolutionCache().forceDownload()
                    .setup();
//            WebDriverManager.firefoxdriver().config().setAvoidAutoReset(true);
            WebDriverManager.firefoxdriver().clearResolutionCache().forceDownload()
                    .setup();
        }catch (Exception e){
            System.out.println("Failed to Auto Download, Check your network and try again");
        }
    }
}

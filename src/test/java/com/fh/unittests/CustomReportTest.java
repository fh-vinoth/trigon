package com.fh.unittests;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import static com.trigon.reports.Initializers.cUtils;

public class CustomReportTest extends TestLocalController {

    @Test
    public void customReportTest(){
        addHeaderToCustomReport("S.No","Bhaskar","reddy","reddy","reddy","reddy","reddy","reddy","reddy","reddy","reddy","reddy","reddy");
        for(int i=0;i<1000;i++){
            addRowToCustomReport(String.valueOf(i),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5),cUtils().generateRandomString(5));
        }
    }
}

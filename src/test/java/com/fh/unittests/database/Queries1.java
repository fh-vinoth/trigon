package com.fh.unittests.database;

import com.fh.core.TestLocalController;
import com.trigon.database.Database;
import org.testng.annotations.Test;

public class Queries1 extends TestLocalController {

    @Test
    public void queries(){

        String OTPMessage = db.SelectTableInDB("select * from config where host = 'automation-uk1.t2scdn.com'");
        System.out.println(OTPMessage);
    }
}

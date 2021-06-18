package com.fh.unittests.database;

import com.fh.core.TestLocalController;
import com.trigon.database.Database;
import org.testng.annotations.Test;

public class Queries extends TestLocalController {

    @Test
    public void queries(){

        String OTPMessage = db.SelectTableInDB("select count(*) from config where contact_no = '07032542435'");
        System.out.println(OTPMessage);
    }
    @Test
    public void queries1(){

        String OTPMessage = db.SelectTableInDB("select count(*) from config where contact_no = '07032542435'");
        System.out.println(OTPMessage);
    }
}

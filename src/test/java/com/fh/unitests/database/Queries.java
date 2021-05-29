package com.fh.unitests.database;

import com.fh.core.TestLocalController;
import com.trigon.database.SSHSQLConnection;
import org.testng.annotations.Test;

public class Queries extends TestLocalController {

    @Test
    public void queries(){
        SSHSQLConnection db = new SSHSQLConnection();
        //String OTPMessage = db.connecttodatabaseString("select * from phone_auth where phone = '07446411868' and app_name = '" + tEnv().getApplicationType() + "' order by id desc limit 1;", "otp");

    }
}

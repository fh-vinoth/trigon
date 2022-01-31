package com.fh.unittests.database;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;


public class Queries2 extends TestLocalController {

    @Test
    public void queries2(){

        String s = db.sendQuery("select * from config where host = 'automation-uk1.t2scdn.com'", 1);
        System.out.println(s);
    }
}

package com.fh.unittests.database;

import com.fh.core.TestLocalController;
import com.trigon.database.Database;
import org.testng.annotations.Test;

import static com.trigon.database.Database.sendQuery;

public class Queries1 extends TestLocalController {

    @Test
    public void queries(){

        String s = sendQuery("select * from config where host = 'automation-uk1.t2scdn.com'", 1);
        System.out.println(s);
    }
}

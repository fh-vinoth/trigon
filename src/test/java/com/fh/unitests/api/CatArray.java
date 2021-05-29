package com.fh.unitests.api;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

public class CatArray extends TestLocalController {

    @Test
    public void getCategoryListNew() {
        try {
            api().validateStaticResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCategoryListNew1() {
        try {

            api().validateStaticResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

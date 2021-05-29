package com.fh.unittests;

import java.io.File;

public class FileRename {
    public static void main(String[] args) {
        File f = new File("/Users/bhaskarreddy/Desktop/Data/Automation/Development/trigon/src/test/resources/TestResults/Web_Parallel_Template_2020-07-25-000246/SupportFiles/TestResultJSON/PASS-TestModule-Web_Parallel_Bulk.json");
        if (f.exists()) {
            boolean renameResult = f.renameTo(new File("/Users/bhaskarreddy/Desktop/Data/Automation/Development/trigon/src/test/resources/TestResults/Web_Parallel_Template_2020-07-25-000246/SupportFiles/TestResultJSON/FAIL-TestModule-Web_Parallel_Bulk.json"));
            if (renameResult) {
                System.out.println("Rename Success");
            } else {
                System.out.println("Rename Failed");
            }
        } else {
            System.out.println("File Not Exist");
        }
    }
}

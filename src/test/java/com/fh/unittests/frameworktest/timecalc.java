package com.fh.unittests.frameworktest;

public class timecalc {

    public static void main(String[] args) throws InterruptedException {
        long x = System.currentTimeMillis();
        Thread.sleep(60000);
        System.out.println(getRunDuration(x, System.currentTimeMillis()));
    }


    public static String getRunDuration(long startTime, long endTime) {
        long duration = (endTime - startTime);  //Total execution time in milli seconds
        long secs = duration / 1000L;
        long millis = duration % 1000L;
        long mins = secs / 60L;
        secs %= 60L;
        long hours = mins / 60L;
        mins %= 60L;
        return hours + "h " + mins + "m " + secs + "s+" + millis + "ms";
    }
}


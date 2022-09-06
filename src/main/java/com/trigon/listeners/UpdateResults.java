package com.trigon.listeners;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;


public class UpdateResults {
    private static final InfluxDB INFLXUDB = InfluxDBFactory.connect("http://localhost:8086", "automation", "auto1234");
    private static final String DB_NAME = "fh_automation";

    static {
        INFLXUDB.setDatabase(DB_NAME);
    }

    public static void post(final Point point) {
        INFLXUDB.write(point);
    }
}
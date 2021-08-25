package com.trigon.bean;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class CReportPojo {
    private   boolean customReportStartFlag = false;
    private  BufferedWriter customReport;
    private  List<String> customHeaderData = new ArrayList<>();
    private  int customReportHeaderSize = 2;

    public boolean isCustomReportStartFlag() {
        return customReportStartFlag;
    }

    public void setCustomReportStartFlag(boolean customReportStartFlag) {
        this.customReportStartFlag = customReportStartFlag;
    }

    public BufferedWriter getCustomReport() {
        return customReport;
    }

    public void setCustomReport(BufferedWriter customReport) {
        this.customReport = customReport;
    }

    public List<String> getCustomHeaderData() {
        return customHeaderData;
    }

    public void setCustomHeaderData(List<String> customHeaderData) {
        this.customHeaderData = customHeaderData;
    }

    public int getCustomReportHeaderSize() {
        return customReportHeaderSize;
    }

    public void setCustomReportHeaderSize(int customReportHeaderSize) {
        this.customReportHeaderSize = customReportHeaderSize;
    }
}

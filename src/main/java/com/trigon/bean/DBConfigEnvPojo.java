package com.trigon.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DBConfigEnvPojo implements Serializable {
    @SerializedName("dbHost")
    @Expose
    private String dbHost;
    @SerializedName("dbUserName")
    @Expose
    private String dbUserName;
    @SerializedName("dbPassword")
    @Expose
    private String dbPassword;
    @SerializedName("dbSSHHost")
    @Expose
    private String dbSSHHost;
    @SerializedName("dbSSHUser")
    @Expose
    private String dbSSHUser;
    @SerializedName("dbSSHFilePath")
    @Expose
    private String dbSSHFilePath;
    @SerializedName("dbName")
    @Expose
    private String dbName;

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbSSHHost() {
        return dbSSHHost;
    }

    public void setDbSSHHost(String dbSSHHost) {
        this.dbSSHHost = dbSSHHost;
    }

    public String getDbSSHUser() {
        return dbSSHUser;
    }

    public void setDbSSHUser(String dbSSHUser) {
        this.dbSSHUser = dbSSHUser;
    }

    public String getDbSSHFilePath() {
        return dbSSHFilePath;
    }

    public void setDbSSHFilePath(String dbSSHFilePath) {
        this.dbSSHFilePath = dbSSHFilePath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}

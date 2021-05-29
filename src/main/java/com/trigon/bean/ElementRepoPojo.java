package com.trigon.bean;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ElementRepoPojo implements Serializable {
    @SerializedName("authorname")
    @Expose
    private String AuthorName;
    @SerializedName("pageTitle")
    @Expose
    private String pageTitle;
    @SerializedName("elements")
    @Expose
    private JsonObject elements;

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public JsonObject getElements() {
        return elements;
    }

    public void setElements(JsonObject elements) {
        this.elements = elements;
    }
}

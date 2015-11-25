package com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs;

import java.util.Date;

/**
 * Created by stefan.bachmann on 25.11.2015.
 */
public class News {

    private int id;
    private String title;
    private String date;
    private String body;
    private Object newsObject;

    public Object getNewsObject() {
        return newsObject;
    }

    public void setNewsObject(Object newsObject) {
        this.newsObject = newsObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }




}

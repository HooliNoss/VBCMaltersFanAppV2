package com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs;

/**
 * Created by stefan.bachmann on 25.11.2015.
 */
public class Comment {

    private int id;
    private String author;
    private String date;
    private String body;
    private Object commentObject;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Object getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(Object commentObject) {
        this.commentObject = commentObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.company.beavi5.firebaseblog;

/**
 * Created by beavi5 on 13.07.2017.
 */

public class Commend {

    String comment,uid,username;
    Long date;

    public Commend(String comment, String uid, String username, Long date) {
        this.comment = comment;
        this.uid = uid;
        this.username = username;
        this.date = date;
    }

    public Long getDate() {

        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Commend(String comment) {
        this.comment = comment;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Commend() {
    }
}

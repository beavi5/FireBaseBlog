package com.company.beavi5.firebaseblog;

/**
 * Created by admin on 02.07.2017.
 */

public class Blog  {

    public String title,desc,image,uid, username;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Blog() {


    }

    public Blog(String title, String desc, String image, String uid, String username) {
        this.uid = uid;
        this.username = username;

        this.title = title;
        this.desc = desc;
        this.image = image;
    }
}

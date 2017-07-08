package com.company.beavi5.firebaseblog;

/**
 * Created by admin on 02.07.2017.
 */

public class Blog  {

    public String title,desc,image,uid;

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

    public Blog() {

    }

    public Blog(String title, String desc, String image, String uid) {
        this.uid = uid;

        this.title = title;
        this.desc = desc;
        this.image = image;
    }
}

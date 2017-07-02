package com.company.beavi5.firebaseblog;

/**
 * Created by admin on 02.07.2017.
 */

public class Blog  {

    public String title,desc,image;

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

    public Blog(String title, String desc, String image) {

        this.title = title;
        this.desc = desc;
        this.image = image;
    }
}

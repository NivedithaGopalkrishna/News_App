package com.example.newsapp;


public class list_row {
    private String imageId;
    private String title;
    private String desc;
    private String newsdate;

    public list_row(String imageId, String title, String desc , String newsdate) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        this.newsdate = newsdate;
    }
    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsdate() {
        return newsdate;
    }

    public void setNewsdate(String newsdate) {
        this.newsdate = newsdate;
    }

    @Override
    public String toString() {
        return title + "\n" + desc + "\n" + newsdate;
    }
}

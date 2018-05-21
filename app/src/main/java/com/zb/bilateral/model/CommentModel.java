package com.zb.bilateral.model;

/**
 * Created by yaos on 2018/1/26.
 */

public class CommentModel {
    private String photo;//"": "https:\/\/dummyimage.com\/124x237\/",
    private String name;//"": "葛豪青",
    private String NAME;
    private String content;//"": "邓维丹",
    private String updateDate;//": "2018-01-26 17:00:17"

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}

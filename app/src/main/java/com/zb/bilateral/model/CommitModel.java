package com.zb.bilateral.model;

/**
 * Created by bt on 2018/1/26.
 */

public class CommitModel {
    private String status;
    private String token;
    private String imgPath;//图片上传返回
    private String totalScore;//答题得分

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

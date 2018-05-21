package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by bt on 2018/3/7.
 */

public class ExhibitionListModel {
    List<ExhibitionModel> antiquesList;
    private String plan;
    private int imageWidth;
    private int imageHeight;

    public List<ExhibitionModel> getAntiquesList() {
        return antiquesList;
    }

    public void setAntiquesList(List<ExhibitionModel> antiquesList) {
        this.antiquesList = antiquesList;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}

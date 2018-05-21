package com.zb.bilateral.model;

import com.zb.bilateral.mvp.SearchView;

import java.io.Serializable;

/**
 * Created by yaos on 2018/2/2.
 */

public class PublicListModel implements Serializable{
    PublicModel other;

    public PublicModel getOther() {
        return other;
    }

    public void setOther(PublicModel other) {
        this.other = other;
    }
}

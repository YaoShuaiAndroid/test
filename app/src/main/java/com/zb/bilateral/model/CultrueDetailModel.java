package com.zb.bilateral.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaos on 2018/2/23.
 * 文物详情
 */

public class CultrueDetailModel implements Serializable{
    private CultrueModel antiques;

    private List<CultrueModel> likeAntiques;//相似文物

    public CultrueModel getAntiques() {
        return antiques;
    }

    public void setAntiques(CultrueModel antiques) {
        this.antiques = antiques;
    }

    public List<CultrueModel> getLikeAntiques() {
        return likeAntiques;
    }

    public void setLikeAntiques(List<CultrueModel> likeAntiques) {
        this.likeAntiques = likeAntiques;
    }
}

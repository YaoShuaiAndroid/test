package com.zb.bilateral.model;

import java.io.Serializable;

/**
 * Created by bt on 2018/3/2.
 */

public class CultrueHomeModel implements Serializable{
    private String id;//": "n6QYBUE0VBGawgc0unxEMbnlOurnKL6v",
    private String digest;//": "蒲兴颖",
    private String gleaTitle;
    private String cover;//": "https:\/\/dummyimage.com\/302x279\/",
    private String gleaCover;
    private String gleaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getGleaTitle() {
        return gleaTitle;
    }

    public void setGleaTitle(String gleaTitle) {
        this.gleaTitle = gleaTitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getGleaCover() {
        return gleaCover;
    }

    public void setGleaCover(String gleaCover) {
        this.gleaCover = gleaCover;
    }

    public String getGleaId() {
        return gleaId;
    }

    public void setGleaId(String gleaId) {
        this.gleaId = gleaId;
    }
}

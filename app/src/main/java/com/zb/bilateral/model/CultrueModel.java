package com.zb.bilateral.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaos on 2018/1/26.
 * 文化拾遗列表
 */

public class CultrueModel implements Parcelable {
    private String id;//": "n6QYBUE0VBGawgc0unxEMbnlOurnKL6v",
    private String title;//": "高楠剑",
    private String digest;//": "蒲兴颖",
    private String content;
    private String name;
    private String cover;//": "https:\/\/dummyimage.com\/302x279\/",
    private int collCount;//": 28,
    private String isColl;//": 1
    private String gleaTitle;
    private int ls;
    private String gleaCover;
    private String gleaId;
    private String path;
    private int likeCount;//": 26,
    private String introduce;
    private String no;
    private String voice;
    private String isLike;//": 0,
    private String createBy;//": "元琳枫",
    private String museumName;//": "金发莉",
    private String updateDate;//": "2018-01-26 17:30:47"
    List<BannerModel> imgList;


    protected CultrueModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        digest = in.readString();
        content = in.readString();
        name = in.readString();
        cover = in.readString();
        collCount = in.readInt();
        isColl = in.readString();
        gleaTitle = in.readString();
        ls = in.readInt();
        gleaCover = in.readString();
        gleaId = in.readString();
        path = in.readString();
        likeCount = in.readInt();
        introduce = in.readString();
        no = in.readString();
        voice = in.readString();
        isLike = in.readString();
        createBy = in.readString();
        museumName = in.readString();
        updateDate = in.readString();
    }

    public static final Creator<CultrueModel> CREATOR = new Creator<CultrueModel>() {
        @Override
        public CultrueModel createFromParcel(Parcel in) {
            return new CultrueModel(in);
        }

        @Override
        public CultrueModel[] newArray(int size) {
            return new CultrueModel[size];
        }
    };

    public int getLs() {
        return ls;
    }

    public void setLs(int ls) {
        this.ls = ls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getCollCount() {
        return collCount;
    }

    public void setCollCount(int collCount) {
        this.collCount = collCount;
    }

    public String getGleaTitle() {
        return gleaTitle;
    }

    public void setGleaTitle(String gleaTitle) {
        this.gleaTitle = gleaTitle;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<BannerModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<BannerModel> imgList) {
        this.imgList = imgList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsColl() {
        return isColl;
    }

    public void setIsColl(String isColl) {
        this.isColl = isColl;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }


    @Override
    public String toString() {
        return "CultrueModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", digest='" + digest + '\'' +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", collCount=" + collCount +
                ", isColl='" + isColl + '\'' +
                ", gleaTitle='" + gleaTitle + '\'' +
                ", gleaCover='" + gleaCover + '\'' +
                ", gleaId='" + gleaId + '\'' +
                ", path='" + path + '\'' +
                ", likeCount=" + likeCount +
                ", introduce='" + introduce + '\'' +
                ", no='" + no + '\'' +
                ", voice='" + voice + '\'' +
                ", isLike='" + isLike + '\'' +
                ", createBy='" + createBy + '\'' +
                ", museumName='" + museumName + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", imgList=" + imgList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(digest);
        dest.writeString(content);
        dest.writeString(name);
        dest.writeString(cover);
        dest.writeInt(collCount);
        dest.writeString(isColl);
        dest.writeString(gleaTitle);
        dest.writeInt(ls);
        dest.writeString(gleaCover);
        dest.writeString(gleaId);
        dest.writeString(path);
        dest.writeInt(likeCount);
        dest.writeString(introduce);
        dest.writeString(no);
        dest.writeString(voice);
        dest.writeString(isLike);
        dest.writeString(createBy);
        dest.writeString(museumName);
        dest.writeString(updateDate);
    }
}

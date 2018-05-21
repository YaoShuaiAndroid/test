package com.zb.bilateral.model;

import java.io.Serializable;

/**
 * Created by yaos on 2018/2/2.
 * 公共接口
 */

public class PublicModel implements Serializable {
    private String help;
    private String about;
    private String pointRule;
    private String phone;
    private String shareRequest;
    private String apk;//": "http://win3.qbt8.com/zbjj/userfiles/1/files/apk/2018/04/MuseumGuide_v0002.apk",
    private String version;//": "V0002",

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPointRule() {
        return pointRule;
    }

    public void setPointRule(String pointRule) {
        this.pointRule = pointRule;
    }


    public String getShareRequest() {
        return shareRequest;
    }

    public void setShareRequest(String shareRequest) {
        this.shareRequest = shareRequest;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PublicModel{" +
                "help='" + help + '\'' +
                ", about='" + about + '\'' +
                ", pointRule='" + pointRule + '\'' +
                '}';
    }
}

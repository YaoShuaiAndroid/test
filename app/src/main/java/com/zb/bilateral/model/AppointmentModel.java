package com.zb.bilateral.model;

/**
 * Created by yaos on 2018/1/26.
 */

public class AppointmentModel {
    private String name;//": "古全婕",
    private String logo;//": "https:\/\/dummyimage.com\/193x238\/",
    private String cteateDate;//": "2018-01-26 18:12:19",
    private String STATUS;//": 0
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCreateDate() {
        return cteateDate;
    }

    public void setCreateDate(String createDate) {
        this.cteateDate = createDate;
    }

    public String getStatus() {
        return STATUS;
    }

    public void setStatus(String status) {
        this.STATUS = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

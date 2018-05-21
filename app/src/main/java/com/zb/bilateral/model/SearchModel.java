package com.zb.bilateral.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by hp2015-1-1 on 2017/5/5.
 */
@Entity
public class SearchModel {
    @Id(autoincrement = true)
    private Long id;
    //搜索字段
    @Unique
    private String name;
    @Generated(hash = 1009791936)
    public SearchModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 506184495)
    public SearchModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

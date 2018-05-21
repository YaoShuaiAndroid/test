package com.zb.bilateral.model;

import java.io.Serializable;

/**
 * Created by ys on 2017/5/10.
 */
public class BaseModel<T> implements Serializable{
    private String code;
    private String msg;
    private  int page;
    private  int current_page;
    private boolean vis;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isVis() {
        return vis;
    }

    public void setVis(boolean vis) {
        this.vis = vis;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

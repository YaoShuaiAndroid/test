package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/26.
 * 文化拾遗列表
 */

public class PolicyListModel {
    private int totalPage;//
    private int page;
    private List<PolicyModel> policyList;
   private PolicyModel policy;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<PolicyModel> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<PolicyModel> policyList) {
        this.policyList = policyList;
    }

    public PolicyModel getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyModel policy) {
        this.policy = policy;
    }
}

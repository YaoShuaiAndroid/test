package com.zb.bilateral.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.policy.PolicyDetailActivity;
import com.zb.bilateral.adapter.PolicyAdapter;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.header.PolicyHeader;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.PolicyListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PolicyFragment  extends BaseNewFragment<PublicPresenter<PolicyListModel>> implements SwipeRefreshLayout.OnRefreshListener,PublicView<PolicyListModel>{
    @Bind(R.id.policy_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.policy_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.policy_edit_text)
    EditText policyEditText;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private PolicyAdapter policyAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private PolicyHeader policyHeader;

    private String title="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_policy_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        initRecyclerView();
    }

    @Override
    protected void initData() {
        getBanner();

        policys(true);
    }

    /**
     * 获取轮播图
     */
    public void getBanner() {
        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.banner("2");
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }


    /**
     * 政策法规列表
     */
    public void policys(boolean is_load) {
        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.policys(""+pageNum,title,is_load);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        policyAdapter = new PolicyAdapter(mContext);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(policyAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        policyHeader=new PolicyHeader(mContext);
        pLuRecyclerViewAdapter.addHeaderView(policyHeader);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, PolicyDetailActivity.class);
                intent.putExtra("policy_id",policyAdapter.getDataList().get(position).getId());
                startActivity(intent);
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
               if (policyListModel.getPage()<policyListModel.getTotalPage()) {
                   policys(false);
                } else {
                    mLuRecyclerView.setNoMore(true);
                }
            }
        });

        //设置底部加载颜色
        mLuRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.main_color, android.R.color.white);
        //设置底部加载文字提示
        mLuRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
    }




    @Override
    public void onRefresh() {
        pageNum = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mLuRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法

        policyAdapter.clear();
        title="";

       policys(false);
    }

    @OnClick({R.id.policy_search_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.policy_search_commit:
                title=policyEditText.getText().toString();

                policyAdapter.clear();
                pageNum=1;

                policys(true);
                break;
        }
    }

    PolicyListModel policyListModel;
    @Override
    public void SendResultSuccess(PolicyListModel policyList) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        policyListModel=policyList;
        pageNum++;

        policyAdapter.addAll(policyList.getPolicyList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        AppToast.makeToast(mContext, msg);
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> list) {
        policyHeader.setBanner(list);
    }
}


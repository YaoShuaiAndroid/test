package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.home_page.ActivityDetailActivity;
import com.zb.bilateral.adapter.MyActivityAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.mvp.MyActivityPresenter;
import com.zb.bilateral.mvp.MyActivityView;
import com.zb.bilateral.view.LuRecyclerView;

import butterknife.Bind;
import butterknife.OnClick;

public class MyActivityActivity extends BaseNewActivity<MyActivityPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, MyActivityView {
    @Bind(R.id.activity_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.activity_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.empty_view)
    View emptyView;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private MyActivityAdapter myActivityAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_activity;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("我的活动");

        initRecyclerView();
    }

    @Override
    protected void initData() {
        signUpAct(true);
    }

    /**
     * 获取我的活动
     */
    public void signUpAct(boolean is_load) {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.signUpAct("" + pageNum, token, is_load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
        }
    }

    @Override
    protected MyActivityPresenter createPresenter() {
        return new MyActivityPresenter(this, mCctivity);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        myActivityAdapter = new MyActivityAdapter(mCctivity);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(myActivityAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mCctivity, ActivityDetailActivity.class);
                intent.putExtra("activity_id", myActivityAdapter.getDataList().get(position).getId());
                intent.putExtra("status", myActivityAdapter.getDataList().get(position).getStatus());
                startActivity(intent);
            }
        });

        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (activityListModels.getPage() < activityListModels.getTotalPage()) {
                    signUpAct(false);
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
        mLuRecyclerView.setRefreshing(true);

        myActivityAdapter.clear();

        signUpAct(false);
    }

    ActivityListModel activityListModels;
    @Override
    public void SendResultSuccess(ActivityListModel activityListModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        activityListModels = activityListModel;
        pageNum++;

        myActivityAdapter.addAll(activityListModel.getActList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();

        mLuRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        AppToast.makeToast(mCctivity, msg);
    }
}



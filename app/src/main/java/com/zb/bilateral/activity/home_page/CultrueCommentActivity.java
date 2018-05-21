package com.zb.bilateral.activity.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.adapter.CultruedetailAdapter;
import com.zb.bilateral.adapter.MyAppointAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.CultrueCommentPresenter;
import com.zb.bilateral.mvp.CultrueCommentView;
import com.zb.bilateral.mvp.MyAppointmentPresenter;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.CommentDialog;
import com.zb.bilateral.view.LuRecyclerView;
import com.zb.bilateral.view.PromptLoginDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class CultrueCommentActivity extends BaseNewActivity<CultrueCommentPresenter>
        implements SwipeRefreshLayout.OnRefreshListener,CultrueCommentView {
    @Bind(R.id.activity_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.activity_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.empty_view)
    View emptyView;

    private static final int COMMENT_TEXT=0;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CultruedetailAdapter cultruedetailAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String cultrue_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_activity;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        cultrue_id=getIntent().getStringExtra("cultrue_id");

        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topRightImg.setBackgroundResource(R.mipmap.top_message);
        topCenterText.setText("评论");

        initRecyclerView();
    }

    @Override
    protected void initData() {
        antComs(true);
    }

    /**
     * 文物评价列表
     */
    public void antComs(boolean is_load) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.antComs(cultrue_id,""+pageNum,is_load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     *文物评论
     */
    public void comAct(String text) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.comGlea(cultrue_id,token,text);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }



    @OnClick({R.id.top_left_img,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.top_right_rel:
                if(AppUtil.getToken(mCctivity).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mCctivity,topCenterText);
                    return;
                }
                //评价弹窗
                new CommentDialog((Activity) mCctivity,topCenterText,handler);
                break;
        }
    }

    @Override
    protected CultrueCommentPresenter createPresenter() {
        return new CultrueCommentPresenter(this,mCctivity);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        cultruedetailAdapter = new CultruedetailAdapter(mCctivity);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(cultruedetailAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (commitListModels.getPage()<commitListModels.getTotalPage()) {
                    antComs(false);
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

        cultruedetailAdapter.clear();

        antComs(false);
    }

    private CommitListModel commitListModels;

    @Override
    public void CommentListSuccess(CommitListModel commitListModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        commitListModels=commitListModel;
        pageNum++;

        cultruedetailAdapter.addAll(commitListModel.getComList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();

        mLuRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void SubmitCommitSuccess(String status) {
        AppToast.makeToast(mCctivity,"评论成功，请等待管理员审核");
    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        AppToast.makeToast(mCctivity,msg);
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COMMENT_TEXT://评论
                    String text= (String) msg.obj;

                    comAct(text);
                    break;
            }
        }
    };
}

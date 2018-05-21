package com.zb.bilateral.activity.person;

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
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.adapter.MyAppointAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.mvp.MyAppointmentPresenter;
import com.zb.bilateral.mvp.MyAppointmentView;
import com.zb.bilateral.view.CommitDialog;
import com.zb.bilateral.view.LuRecyclerView;

import butterknife.Bind;
import butterknife.OnClick;

public class MyAppointmentActivity extends BaseNewActivity<MyAppointmentPresenter> implements SwipeRefreshLayout.OnRefreshListener,MyAppointmentView {
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
    private MyAppointAdapter myActivityAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String commId;//点击的预约id
    private int clickItem;

    public final static int COMMIT_DIALOG=10;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_activity;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("我的预约讲解");

        initRecyclerView();
    }

    @Override
    protected void initData() {
        myCommentary(true);
    }

    /**
     * 获取预约列表
     */
    public void myCommentary(boolean is_load) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.myCommentary(""+pageNum,token,is_load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 取消预约列表
     */
    public void delComm(String commId) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.delComm(""+commId,token);
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
    protected MyAppointmentPresenter createPresenter() {
        return new MyAppointmentPresenter(this,mCctivity);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        myActivityAdapter = new MyAppointAdapter(mCctivity);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(myActivityAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(myActivityAdapter.getDataList().get(position).getStatus().equals("0")){
                    //当为处理中时可以取消
                    commId=myActivityAdapter.getDataList().get(position).getId();
                    clickItem=position;

                    new CommitDialog((Activity) mCctivity,topCenterText,
                            handler,"处理中可以取消当前预约，是否取消当前预约？");
                }
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
               if (appointmentListModels.getPage()<appointmentListModels.getTotalPage()) {
                   myCommentary(false);
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

        myActivityAdapter.clear();

        myCommentary(false);
    }

    AppointmentListModel appointmentListModels;
    @Override
    public void SendResultSuccess(AppointmentListModel appointmentListModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        appointmentListModels=appointmentListModel;
        pageNum++;

        myActivityAdapter.addAll(appointmentListModel.getActList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();

        mLuRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void submitSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity,"取消成功");

        myActivityAdapter.getDataList().remove(clickItem);
        myActivityAdapter.notifyDataSetChanged();
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
                case COMMIT_DIALOG:
                    delComm(commId);
                    break;
                default:
                    break;
            }
        }
    };
}


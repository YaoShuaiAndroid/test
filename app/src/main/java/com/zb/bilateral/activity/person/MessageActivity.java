package com.zb.bilateral.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.adapter.MessageAdapter;
import com.zb.bilateral.adapter.MyAppointAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.MessageModel;
import com.zb.bilateral.model.MyMessageModel;
import com.zb.bilateral.mvp.MessagePresenter;
import com.zb.bilateral.mvp.MessageView;
import com.zb.bilateral.mvp.MyAppointmentPresenter;
import com.zb.bilateral.mvp.MyAppointmentView;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.CommitDialog;
import com.zb.bilateral.view.LuRecyclerView;

import butterknife.Bind;
import butterknife.OnClick;

public class MessageActivity extends BaseNewActivity<MessagePresenter> implements SwipeRefreshLayout.OnRefreshListener,MessageView {
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
    private MessageAdapter messageAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String messageId;
    private int clickItem;

    public final static int COMMIT_DIALOG=10;
    public final static int MESSAGE_DETAIL_REQUEST=1000;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_activity;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("我的消息");

        initRecyclerView();
    }

    @Override
    protected void initData() {
        messages(true);
    }

    /**
     * 获取消息列表
     */
    public void messages(boolean is_load) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.messages(""+pageNum,token,is_load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 删除消息
     */
    public void delMessages(String id) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.delMessages(""+id,token);
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
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this,mCctivity);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        messageAdapter = new MessageAdapter(mCctivity);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(messageAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                messageId=messageAdapter.getDataList().get(position).getId();

                clickItem=position;

                new CommitDialog((Activity) mCctivity,topCenterText,
                        handler,"是否删除此条消息？");
            }
        });

        pLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                messageId=messageAdapter.getDataList().get(position).getId();

                //将消息改为已读
                if("1".equals( messageAdapter.getDataList().get(position).getIsRead())){
                    messageAdapter.getDataList().get(position).setIsRead("0");
                    messageAdapter.notifyDataSetChanged();
                }

                Intent intent=new Intent(mCctivity,MessageDetailActivity.class);
                intent.putExtra("messageId",messageId);
                startActivityForResult(intent,MESSAGE_DETAIL_REQUEST);
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (myMessageModels.getPage()<myMessageModels.getTotalPage()) {
                    messages(false);
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

        messageAdapter.clear();

        messages(false);
    }

    MyMessageModel myMessageModels;

    @Override
    public void SendResultSuccess(MyMessageModel messageModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        myMessageModels=messageModel;
        pageNum++;

        messageAdapter.addAll(messageModel.getMessageList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();

        mLuRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void delRsultSuccess(String result) {
        AppToast.makeToast(mCctivity,"删除成功");

        messageAdapter.getDataList().remove(clickItem);
        messageAdapter.notifyDataSetChanged();
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
                    delMessages(messageId);
                    break;
                default:
                    break;
            }
        }
    };
}


package com.zb.bilateral.activity.home_page;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.umeng.socialize.UMShareAPI;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.adapter.CultruedetailAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.header.ShowDetailHeader;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.ShowModel;
import com.zb.bilateral.mvp.ShowDetailPresenter;
import com.zb.bilateral.mvp.ShowDetailView;
import com.zb.bilateral.util.ShareUtil;
import com.zb.bilateral.view.PromptLoginDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class ShowDetailActivity extends BaseNewActivity<ShowDetailPresenter> implements  SwipeRefreshLayout.OnRefreshListener,ShowDetailView {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.comment_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.comment_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CultruedetailAdapter cultruedetailAdapter;

    private ShowDetailHeader showDetailHeader=null;

    private int pageNum = 1;
    private int pageSize = 10;

    private static final int LIKE=1;
    private static final int COMMENT_TEXT=0;

    private String show_id;
    /**
     * 用户标记
     */
    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cultrue;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topRightImg.setBackgroundResource(R.mipmap.policy_share);
        topCenterText.setText("展览");

        token=AppUtil.getToken(mCctivity);

        show_id=getIntent().getStringExtra("show_id");

        initRecyclerView();
    }

    @Override
    protected void initData() {
        cultrue_Detail();

        actComs(true);
    }

    @OnClick({R.id.top_left_img,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.top_right_rel:
                //分享
                PublicModel publicMode= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));

                if(showModels==null||publicMode==null){
                    AppToast.makeToast(mCctivity,"数据异常");
                    return;
                }

                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }else {
                    new ShareUtil().share(showModels.getTitle(), showModels.getContent(),
                            publicMode, mCctivity);
                }
                break;
            default:
                break;
        }
    }

    PublicModel publicMode=null;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        new ShareUtil().share(showModels.getTitle(), showModels.getContent(),
                publicMode, mCctivity);
    }

    /**
     *  展览详情
     */
    public void cultrue_Detail() {
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.show_Detail(show_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     *  活动评论列表
     */
    public void actComs(boolean is_Load) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.exhComs(show_id,""+pageNum,is_Load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     *评论
     */
    public void comExh(String text) {
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.comExh(show_id,token,text);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 活动点赞/取消赞
     */
    public void gleaLike() {
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.exhLike(show_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @Override
    protected ShowDetailPresenter createPresenter() {
        return new ShowDetailPresenter(this,mCctivity);
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

        showDetailHeader=new ShowDetailHeader(mCctivity,handler);
        pLuRecyclerViewAdapter.addHeaderView(showDetailHeader);

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
                    actComs(false);
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

        cultrue_Detail();

        actComs(false);
    }

    ShowModel showModels;
    @Override
    public void SendResultSuccess(ShowModel showModel) {
        showModels=showModel;
        showDetailHeader.setData(showModels);
    }

    CommitListModel commitListModels;
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
    }

    @Override
    public void SubmitLikeSuccess(String result) {
        if("1".equals(result)){
            showModels.setLikeCount((showModels.getLikeCount()+1));
        }else{
            showModels.setLikeCount((showModels.getLikeCount()-1));
        }
        showModels.setIsLike(result);
        showDetailHeader.setCollectAndLike(showModels);
    }


    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    public void SubmitResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
        showDetailHeader.setCollectAndLike(showModels);
    }

    @Override
    public void SubmitCommitSuccess(String result) {
        AppToast.makeToast(mCctivity,"评论成功，您当前的评论正在审核中..");
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(token.equals(ApiStores.TOURIST_TOKEN)){
                new PromptLoginDialog((Activity) mCctivity,topCenterText);
                return;
            }
            switch (msg.what){
                case LIKE://点击点赞
                    gleaLike();
                    break;
                case COMMENT_TEXT://评论
                    String text= (String) msg.obj;

                    comExh(text);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}

package com.zb.bilateral.activity.cultrue;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.adapter.CultruedetailAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.header.CultrueDetailHeader;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.mvp.CultrueDetailPresenter;
import com.zb.bilateral.mvp.CultrueDetailView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.ShareUtil;
import com.zb.bilateral.view.PromptLoginDialog;

public class CultrueDetailActivity
        extends BaseNewActivity<CultrueDetailPresenter>
        implements  SwipeRefreshLayout.OnRefreshListener, CultrueDetailView {
    private static final int COLLECT=1;
    private static final int LIKE=2;
    private static final int COMMENT_TEXT=0;

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

    CultrueDetailHeader cultrueDetailHeader= null;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CultruedetailAdapter cultruedetailAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String cultrue_id;
    /**
     * 用户登录标记
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
        topCenterText.setText("文物拾遗");

        cultrue_id=getIntent().getStringExtra("cultrue_id");

         token=AppUtil.getToken(mCctivity);

        initRecyclerView();
    }

    @Override
    protected void initData(){
        cultrue_Detail();

        gleaComs(true);
    }

    @OnClick({R.id.top_left_img,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.top_right_rel:
                //分享
                 publicMode= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));

                if(cultrueModels==null||publicMode==null){
                    AppToast.makeToast(mCctivity,"数据异常");
                    return;
                }


                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }else {
                    new ShareUtil().share(cultrueModels.getTitle(), cultrueModels.getContent(),
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
        new ShareUtil().share(cultrueModels.getTitle(), cultrueModels.getContent(),
                publicMode, mCctivity);
    }

    /**
     * 文物拾遗详情
     */
    public void cultrue_Detail() {
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.cultrue_Detail(cultrue_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 文物拾遗评论列表
     */
    public void gleaComs(boolean is_Load) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.gleaComs(cultrue_id,""+pageNum,is_Load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 文物拾遗收藏/取消收藏
     */
    public void gleaColl( ) {
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.gleaColl(cultrue_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 文物拾遗点赞/取消赞
     */
    public void gleaLike( ) {
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.gleaLike(cultrue_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    /**
     *活动评论
     */
    public void comAct(String text) {
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


    @Override
    protected CultrueDetailPresenter createPresenter() {
        return new CultrueDetailPresenter(this,mCctivity);
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

        cultrueDetailHeader=new CultrueDetailHeader(mCctivity,handler);
        pLuRecyclerViewAdapter.addHeaderView(cultrueDetailHeader);

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
                    gleaComs(false);
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
        gleaComs(false);
    }

    CultrueModel cultrueModels;
    @Override
    public void SendResultSuccess(CultrueModel cultrueModel) {
        cultrueModels=cultrueModel;
        cultrueDetailHeader.setData(cultrueModel);
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
    public void SubmitCollectSuccess(String result) {
        cultrueModels.setIsColl(result);
        cultrueDetailHeader.setCollectAndLike(cultrueModels);
    }

    @Override
    public void SubmitLikeSuccess(String result) {
        if("1".equals(result)){
            cultrueModels.setLikeCount(cultrueModels.getLikeCount()+1);
        }else{
            cultrueModels.setLikeCount(cultrueModels.getLikeCount()-1);
        }
        cultrueModels.setIsLike(result);
        cultrueDetailHeader.setCollectAndLike(cultrueModels);
    }

    @Override
    public void SubmitCommitSuccess(String result) {
        AppToast.makeToast(mCctivity,"评论成功，您当前的评论正在审核中..");
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

        cultrueDetailHeader.setCollectAndLike(cultrueModels);
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
                case COLLECT://点击收藏
                    gleaColl();
                    break;
                case LIKE://点击点赞
                    gleaLike();
                    break;
                case COMMENT_TEXT://评论
                    String text= (String) msg.obj;

                    comAct(text);
                    break;
            }
        }
    };

}

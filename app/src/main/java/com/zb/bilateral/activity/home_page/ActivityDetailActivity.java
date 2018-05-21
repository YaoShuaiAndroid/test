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
import com.zb.bilateral.header.ActivityDetailHeader;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.SignModel;
import com.zb.bilateral.mvp.ActivityDetailPresenter;
import com.zb.bilateral.mvp.ActivityDetailView;
import com.zb.bilateral.util.ShareUtil;
import com.zb.bilateral.view.PromptLoginDialog;
import com.zb.bilateral.view.SignDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class ActivityDetailActivity extends BaseNewActivity<ActivityDetailPresenter> implements SwipeRefreshLayout.OnRefreshListener, ActivityDetailView {
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

    private ActivityDetailHeader activityDetailHeader = null;

    private int pageNum = 1;
    private int pageSize = 10;

    private static final int COMMENT_TEXT = 0;
    private static final int LIKE = 1;
    private static final int SIGN_CANCEL = 2;
    private static final int SIGN_CDIALOG = 3;
    private static final int SIGN_COMMIT = 4;
    private static final int COLLECT=5;

    private String activity_id, status;
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
        topCenterText.setText("活动");

         token = AppUtil.getToken(mCctivity);

        activity_id = getIntent().getStringExtra("activity_id");
        status = getIntent().getStringExtra("status");

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

                if(activityModels==null||publicMode==null){
                    AppToast.makeToast(mCctivity,"数据异常");
                    return;
                }
                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }else {
                    new ShareUtil().share(activityModels.getTitle(), activityModels.getContent(),
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
        new ShareUtil().share(activityModels.getTitle(), activityModels.getContent(),
                publicMode, mCctivity);
    }

    /**
     * 活动详情
     */
    public void cultrue_Detail() {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.act_Detail(activity_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 活动评论列表
     */
    public void actComs(boolean is_Load) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.actComs(activity_id, "" + pageNum, is_Load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 活动点赞/取消赞
     */
    public void gleaLike() {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.actLike(activity_id, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 取消报名
     */
    public void delAct() {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.delAct(activity_id, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 立即报名
     */
    public void signUpAct(SignModel signModel) {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.signUpAct(activity_id, token, signModel);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
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
            mvpPresenter.actCollCommit(activity_id,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 活动评论
     */
    public void comAct(String text) {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.comAct(activity_id, token, text);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @Override
    protected ActivityDetailPresenter createPresenter() {
        return new ActivityDetailPresenter(this, mCctivity);
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

        activityDetailHeader = new ActivityDetailHeader(mCctivity, handler);
        pLuRecyclerViewAdapter.addHeaderView(activityDetailHeader);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (commitListModels.getPage() < commitListModels.getTotalPage()) {
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

    ActivityModel activityModels;

    @Override
    public void SendResultSuccess(ActivityModel activityModel) {
        if (status != null) {
            activityModel.setStatus(status);
        }

        activityModels = activityModel;
        activityDetailHeader.setData(activityModel);
    }

    CommitListModel commitListModels;
    @Override
    public void CommentListSuccess(CommitListModel commitListModel) {
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        commitListModels = commitListModel;
        pageNum++;

        cultruedetailAdapter.addAll(commitListModel.getComList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void CancelSignSuccess(String result) {
        AppToast.makeToast(mCctivity, "取消成功");

        activityModels.setIsSignIn("0");
        activityDetailHeader.setCollectAndLike(activityModels);
    }

    @Override
    public void SubmitSignSuccess(String result) {
        AppToast.makeToast(mCctivity, "操作成功");

        activityModels.setIsSignIn("1");
        activityDetailHeader.setCollectAndLike(activityModels);
    }

    @Override
    public void SubmitCommitSuccess(String result) {
        AppToast.makeToast(mCctivity, "评论成功，您当前的评论正在审核中..");
    }

    @Override
    public void SubmitLikeSuccess(String result) {
        if ("1".equals(result)) {
            activityModels.setLikeCount((activityModels.getLikeCount() + 1));
        } else {
            activityModels.setLikeCount((activityModels.getLikeCount() - 1));
        }
        activityModels.setIsLike(result);
        activityDetailHeader.setCollectAndLike(activityModels);
    }

    @Override
    public void SubmitCollectSuccess(String result) {
        activityModels.setIsColl(result);
        activityDetailHeader.setCollectAndLike(activityModels);
    }


    @Override
    public void SendResultFail(String msg) {
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        AppToast.makeToast(mCctivity, msg);
    }

    @Override
    public void SubmitResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
        activityDetailHeader.setCollectAndLike(activityModels);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(token.equals(ApiStores.TOURIST_TOKEN)){
                new PromptLoginDialog((Activity) mCctivity,topCenterText);
                return;
            }
            switch (msg.what) {
                case LIKE://点击点赞
                    gleaLike();
                    break;
                case SIGN_CANCEL://取消报名
                    delAct();
                    break;
                case SIGN_CDIALOG://立即报名弹窗
                    new SignDialog((Activity) mCctivity, topCenterText, this, activityModels.getTitle());
                    break;
                case SIGN_COMMIT://立即报名
                    signUpAct((SignModel) msg.obj);
                    break;
                case COMMENT_TEXT://评论
                    String text = (String) msg.obj;

                    comAct(text);
                    break;
                case COLLECT://点击收藏
                    gleaColl();
                    break;
                default:
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

package com.zb.bilateral.activity.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.adapter.AnnouncementAdapter;
import com.zb.bilateral.base.LazyFragment;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.LuRecyclerView;

import java.util.List;

import butterknife.Bind;

public class AnnouncementListActivity extends LazyFragment<PublicPresenter<ActivityListModel>> implements SwipeRefreshLayout.OnRefreshListener,PublicView<ActivityListModel>{
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";
    private static final String MUSEUM_ID = "museum_id";

    @Bind(R.id.dynamic_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.dynamic_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.empty_view)
    View emptyView;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private AnnouncementAdapter announcementAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String museumId;

    public static AnnouncementListActivity newInstance(String category, int multiType, String museum_id) {
        AnnouncementListActivity maintenanceFragment = new AnnouncementListActivity();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        bundle.putString(MUSEUM_ID,museum_id);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_list;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        museumId=getActivity().getIntent().getStringExtra(MUSEUM_ID);
        initRecyclerView();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    @Override
    protected void loadData() {
        acts(true);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        announcementAdapter = new AnnouncementAdapter(mContext);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(announcementAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, AnnouncementDetailActivity.class);
                intent.putExtra("activity_id",announcementAdapter.getDataList().get(position).getId());
                startActivity(intent);
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (activityListModels.getPage()<activityListModels.getTotalPage()) {
                    acts(false);
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

    /**
     * 查询公告列表
     */
    public void acts(boolean is_load) {
        String token=AppUtil.getToken(mContext);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.notices(museumId,""+pageNum,token,is_load);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mLuRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法

        announcementAdapter.clear();

        acts(false);
    }

    ActivityListModel activityListModels;
    @Override
    public void SendResultSuccess(ActivityListModel activityListModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        activityListModels=activityListModel;
        pageNum++;

        announcementAdapter.addAll(activityListModel.getNoticeList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();

        mLuRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {

    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        AppToast.makeToast(mContext,msg);
    }
}

package com.zb.bilateral.activity.show;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.home_page.ShowDetailActivity;
import com.zb.bilateral.adapter.ShowListAdapter;
import com.zb.bilateral.base.LazyFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.ShowListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.LuRecyclerView;

import java.util.List;

import butterknife.Bind;
/*
* 博物馆--展览列表
* */
public class ShowActivity extends LazyFragment<PublicPresenter<ShowListModel>> implements SwipeRefreshLayout.OnRefreshListener ,PublicView<ShowListModel>{
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
    private ShowListAdapter showListAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private int mMultiType;
    private String museum_id;

    public static ShowActivity newInstance(String category, int multiType,String museum_id) {
        ShowActivity maintenanceFragment = new ShowActivity();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        bundle.putString(MUSEUM_ID,museum_id);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    private int getDataMultiType() {
        return getArguments().getInt(ARGUMENT_MULTI_TYPE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_list;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        museum_id=getActivity().getIntent().getStringExtra(MUSEUM_ID);

        mMultiType=getDataMultiType();

        initRecyclerView();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    /**
     * 查询基本展览列表
     */
    public void basicExhs(boolean is_load) {
        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.basicExhs(""+pageNum,museum_id,is_load);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    /**
     * 查询临时展览列表
     */
    public void temporaryExhs(boolean is_load) {
        if(AppUtil.checkNetWork(mContext)) {
            mvpPresenter.temporaryExhs(""+pageNum,museum_id,is_load);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    @Override
    protected void loadData(){
        if(mMultiType==1){
            basicExhs(true);
        }else{
            temporaryExhs(true);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        showListAdapter = new ShowListAdapter(mContext);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(showListAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, ShowDetailActivity.class);
                intent.putExtra("show_id",showListAdapter.getDataList().get(position).getId());
                startActivity(intent);
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (showListModels.getPage()<showListModels.getTotalPage()) {
                    if(mMultiType==1){
                        basicExhs(true);
                    }else{
                        temporaryExhs(true);
                    }
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

        showListAdapter.clear();

        if(mMultiType==1){
            basicExhs(true);
        }else{
            temporaryExhs(true);
        }
    }

    ShowListModel showListModels;
    @Override
    public void SendResultSuccess(ShowListModel showModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        showListModels=showModel;
        pageNum++;
        if(mMultiType==1) {
            showListAdapter.addAll(showModel.getBasicList());
        }else{
            showListAdapter.addAll(showModel.getTemporaryList());
        }
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
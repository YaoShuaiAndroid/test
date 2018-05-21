package com.zb.bilateral.activity.person.collect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zb.bilateral.view.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.home_page.TreasureDetailActivity;
import com.zb.bilateral.adapter.CollectAdapter;
import com.zb.bilateral.base.LazyFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CollectListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;

import butterknife.Bind;

public class MyCollectFragment extends LazyFragment<PublicPresenter<CollectListModel>>
        implements SwipeRefreshLayout.OnRefreshListener,PublicView<CollectListModel>{
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";

    @Bind(R.id.museum_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.museum_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.empty_view)
    View emptyView;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CollectAdapter collectAdapter;

    private int pageNum = 1;

    public static MyCollectFragment newInstance(String category, int multiType) {
        MyCollectFragment maintenanceFragment = new MyCollectFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        initRecyclerView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_museum_fragment;
    }

    /**
     * 获取我的收藏-馆藏
     */
    public void trColl(boolean is_load) {
        String token=AppUtil.getToken(mContext);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.trColl(""+pageNum,token,is_load);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
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
        trColl(true);
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mLuRecyclerView.setLayoutManager(layoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        collectAdapter = new CollectAdapter(mContext);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(collectAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, TreasureDetailActivity.class);
                intent.putExtra("collect_id",collectAdapter.getDataList().get(position).getId());
                startActivity(intent);
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (collectListModels.getPage()<collectListModels.getTotalPage()) {
                    trColl(false);
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

    CollectListModel collectListModels;
    @Override
    public void SendResultSuccess(CollectListModel collectListModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        collectListModels=collectListModel;
        pageNum++;

        collectAdapter.addAll(collectListModel.getActList());
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

    @Override
    public void onRefresh() {
        pageNum = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        //同时调用LuRecyclerView的setRefreshing方法
        mLuRecyclerView.setRefreshing(true);

        collectAdapter.clear();

        trColl(false);
    }
}

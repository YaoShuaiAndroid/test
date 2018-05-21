package com.zb.bilateral.activity.person.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.cultrue.CultrueDetailActivity;
import com.zb.bilateral.adapter.CultrueAdapter;
import com.zb.bilateral.base.LazyFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.LuRecyclerView;

import java.util.List;

import butterknife.Bind;

public class MyCultrueFragment extends LazyFragment<PublicPresenter<CultrueListModel>>
        implements SwipeRefreshLayout.OnRefreshListener,PublicView<CultrueListModel>{
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";

    @Bind(R.id.museum_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.museum_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.empty_view)
    View emptyView;

    private int mMultiType;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CultrueAdapter cultrueAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    public static MyCultrueFragment newInstance(String category, int multiType) {
        MyCultrueFragment maintenanceFragment = new MyCultrueFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY, category);
        bundle.putInt(ARGUMENT_MULTI_TYPE, multiType);
        maintenanceFragment.setArguments(bundle);
        return maintenanceFragment;
    }

    private int getDataMultiType() {
        return getArguments().getInt(ARGUMENT_MULTI_TYPE);
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        mMultiType=getDataMultiType();

        initRecyclerView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_museum_fragment;
    }

    /**
     * 获取我的收藏-文化拾遗
     */
    public void gleaColl1(boolean is_load) {
        String token=AppUtil.getToken(mContext);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if(AppUtil.checkNetWork(mContext)) {
            mvpPresenter.gleaColl1(""+pageNum,token,is_load);
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
        gleaColl1(true);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        cultrueAdapter = new CultrueAdapter(mContext);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(cultrueAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);

        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, CultrueDetailActivity.class);
                intent.putExtra("cultrue_id",cultrueAdapter.getDataList().get(position).getId());
                startActivity(intent);
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (cultrueListModels.getPage()<cultrueListModels.getTotalPage()) {
                    gleaColl1(false);
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

    CultrueListModel cultrueListModels;
    @Override
    public void SendResultSuccess(CultrueListModel cultrueLists) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        cultrueListModels=cultrueLists;
        pageNum++;

        cultrueAdapter.addAll(cultrueListModels.getActList());
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

        cultrueAdapter.clear();

        gleaColl1(false);
    }
}

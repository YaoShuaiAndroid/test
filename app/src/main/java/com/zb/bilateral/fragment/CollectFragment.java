package com.zb.bilateral.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.home_page.TreasureDetailActivity;
import com.zb.bilateral.adapter.CollectAdapter;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CollectListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.LuRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
/**
 * @author yaos
 * @date 2016/10/31
 */
public class CollectFragment extends BaseNewFragment<PublicPresenter<CollectListModel>> implements SwipeRefreshLayout.OnRefreshListener,PublicView<CollectListModel> {
    private static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";
    private static final String ARGUMENT_MULTI_TYPE = "ARGUMENT_MULTI_TYPE";

    @Bind(R.id.collect_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.collect_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.empty_view)
    View emptyView;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CollectAdapter collectAdapter;
    private MuseumDetailActivity museumActivity;

    private int pageNum = 1;
    private int pageSize = 10;

    private String museumId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        museumActivity= (MuseumDetailActivity) activity;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        Bundle args=getArguments();
        museumId=args.getString("museum_id");
        initRecyclerView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect_fragment;
    }

    @Override
    protected void initData() {
        treasures(true);
    }

    /**
     * 馆藏
     */
    public void treasures(boolean isLoad) {
        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.treasures(""+pageNum,museumId,isLoad);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    private void initRecyclerView() {
        //setLayoutManager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL);
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
                    treasures(false);
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

        collectAdapter.addAll(collectListModel.getTreaList());
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

        treasures(false);
    }

    @OnClick({R.id.top_left_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                museumActivity.close();

                break;
            default:
                break;
        }
    }
}

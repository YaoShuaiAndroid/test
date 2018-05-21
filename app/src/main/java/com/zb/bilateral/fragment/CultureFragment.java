package com.zb.bilateral.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.cultrue.CultrueDetailActivity;
import com.zb.bilateral.adapter.CultrueAdapter;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CultureFragment extends BaseNewFragment<PublicPresenter<CultrueListModel>> implements SwipeRefreshLayout.OnRefreshListener,PublicView<CultrueListModel>{
    @Bind(R.id.cultrue_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.cultrue_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.cultrue_search_edit)
    EditText searchEditText;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private CultrueAdapter cultrueAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String title="";//搜索标题

    @Override
    protected int getLayoutId() {
        return R.layout.activity_culture_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        initRecyclerView();
    }

    @Override
    protected void initData() {
        gleanings(true);
    }

    /**
     * 获取文化拾遗列表
     */
    public void gleanings(boolean is_load) {
        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.gleanings(""+pageNum,title,is_load);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
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
               if (cultrueListModel.getPage()<cultrueListModel.getTotalPage()) {
                   gleanings(false);
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

    @OnClick({R.id.cultrue_search_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cultrue_search_commit:
                title=searchEditText.getText().toString();

                pageNum=1;
                cultrueAdapter.clear();

                gleanings(true);
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mLuRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法

        cultrueAdapter.clear();

        title="";

        gleanings(false);
    }

    CultrueListModel cultrueListModel;
    @Override
    public void SendResultSuccess(CultrueListModel cultrueList) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        cultrueListModel=cultrueList;
        pageNum++;

        cultrueAdapter.addAll(cultrueListModel.getGleaList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void SendBannerSuccess(List list) {

    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        AppToast.makeToast(mContext,msg);
    }
}

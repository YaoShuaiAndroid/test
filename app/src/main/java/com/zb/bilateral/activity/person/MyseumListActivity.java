package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.adapter.VoiceAdapter;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.MuseumListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyseumListActivity extends BaseNewActivity<PublicPresenter<MuseumListModel>> implements SwipeRefreshLayout.OnRefreshListener,PublicView<MuseumListModel>{
    @Bind(R.id.voice_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.voice_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.myseum_edit)
    EditText myseumEdit;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private VoiceAdapter voiceAdapter;

    private int pageNum = 1;
    private int pageSize = 10;

    private String searchText="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myseum_list;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initRecyclerView();
    }

    @Override
    protected void initData() {
        getMuseums(true);
    }

    @OnClick({R.id.left_img_back,R.id.museum_search_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_img_back:
                finish();
                break;
            case R.id.museum_search_commit://搜索
                searchText=myseumEdit.getText().toString();

                voiceAdapter.clear();

                getMuseums(true);
                break;
        }
    }

    /**
     * 博物馆列表
     */
    public void getMuseums(boolean is_load) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.getMuseums(searchText,""+pageNum,"",is_load);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    private void initRecyclerView() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(mCctivity,2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        voiceAdapter = new VoiceAdapter(mCctivity);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(voiceAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);
        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mCctivity,MyMessageActivity.class);
                intent.putExtra("museum_id",voiceAdapter.getDataList().get(position).getId());
                intent.putExtra("museum_name",voiceAdapter.getDataList().get(position).getName());
                setResult(100,intent);

                finish();
            }
        });


        mLuRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (museumListModels.getPage()<museumListModels.getTotalPage()) {
                    getMuseums(false);
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

        voiceAdapter.clear();
        searchText="";

        getMuseums(false);
    }

    MuseumListModel museumListModels;
    @Override
    public void SendResultSuccess(MuseumListModel museumListModel) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        museumListModels=museumListModel;
        pageNum++;

        voiceAdapter.addAll(museumListModel.getMuseumList());
        mLuRecyclerView.refreshComplete(10);
        pLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}

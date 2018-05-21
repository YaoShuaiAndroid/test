package com.zb.bilateral.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.activity.home_page.LocationActivity;
import com.zb.bilateral.util.AppUtil;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.adapter.VoiceAdapter;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.model.MuseumListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class VoiceFragment extends BaseNewFragment<PublicPresenter<MuseumListModel>> implements SwipeRefreshLayout.OnRefreshListener,PublicView<MuseumListModel> {
    @Bind(R.id.voice_list_recyler)
    LuRecyclerView mLuRecyclerView;
    @Bind(R.id.voice_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.myseum_edit)
    EditText myseumEdit;
    @Bind(R.id.voice_city)
    TextView voiceCity;

    private LuRecyclerViewAdapter pLuRecyclerViewAdapter;
    private VoiceAdapter voiceAdapter;
    private MyApplication myApplication;

    private int pageNum = 1;
    private int pageSize = 10;

    private String searchText="";

    private final static int LOCATION_REQUEST=1000;
    private final static int LOCATION_RESULT=100;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        myApplication= (MyApplication) getActivity().getApplicationContext();
        if(myApplication.getAddress()!=null){
            voiceCity.setText(myApplication.getAddress());
        }

        initRecyclerView();
    }

    @Override
    protected void initData() {
        getMuseums(true);
    }

    @OnClick({R.id.museum_search_commit,R.id.voice_left_lin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.museum_search_commit://搜索
                searchText=myseumEdit.getText().toString();
                pageNum=1;
                voiceAdapter.clear();

                getMuseums(true);
                break;
            case R.id.voice_left_lin:
                //选择城市
                Intent intent = new Intent(mContext, LocationActivity.class);
                startActivityForResult(intent,LOCATION_REQUEST);
                break;
        }
    }

    /**
     * 博物馆列表
     */
    public void getMuseums(boolean is_load) {
        if (AppUtil.checkNetWork(mContext)) {
            String city=voiceCity.getText().toString();
            String cityId=myApplication.getCityId();
            if(cityId.equals("-1")){
                mvpPresenter.getLocationMuseums(searchText,""+pageNum,city,is_load);
            }else{
                mvpPresenter.getMuseums(searchText,""+pageNum,cityId,is_load);
            }
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOCATION_REQUEST&&resultCode==LOCATION_RESULT){
            String cityName=data.getStringExtra("city_name");

            String city_id=data.getStringExtra("city_id");
            voiceCity.setText(data.getStringExtra("city_name"));

            myApplication.setAddress(cityName);
            myApplication.setCityId(city_id);

            voiceAdapter.clear();
            pageNum=1;
            searchText="";

            getMuseums(true);
        }
    }

    private void initRecyclerView() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(mContext,2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLuRecyclerView.setLayoutManager(linearLayoutManager);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        voiceAdapter = new VoiceAdapter(mContext);
        pLuRecyclerViewAdapter = new LuRecyclerViewAdapter(voiceAdapter);
        mLuRecyclerView.setAdapter(pLuRecyclerViewAdapter);
        mLuRecyclerView.setHasFixedSize(true);

        pLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext, MuseumDetailActivity.class);
                intent.putExtra("museum_id",voiceAdapter.getDataList().get(position).getId());
                intent.putExtra("museum_name",voiceAdapter.getDataList().get(position).getName());
                startActivity(intent);
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
        AppToast.makeToast(mContext,msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}

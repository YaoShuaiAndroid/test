package com.zb.bilateral.activity.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.view.GlideRoundImage;
import com.zb.bilateral.Constants;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.cultrue.CultrueDetailActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.dbHelper.GreenDaoManager;
import com.zb.bilateral.dbHelper.SearchModelDao;
import com.zb.bilateral.model.CollectModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.HomePageModel;
import com.zb.bilateral.model.MuseumModel;
import com.zb.bilateral.model.SearchListModel;
import com.zb.bilateral.model.SearchModel;
import com.zb.bilateral.model.ShowModel;
import com.zb.bilateral.mvp.SearchPresenter;
import com.zb.bilateral.mvp.SearchView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.CommitDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SearchActivity extends BaseNewActivity<SearchPresenter> implements SearchView {
    @Bind(R.id.search_recy)
    RecyclerView mRecyclerView;
    @Bind(R.id.search_edit)
    EditText searchEdit;
    @Bind(R.id.search_museum_recy)
    RecyclerView mLocalRecyclerView;
    @Bind(R.id.search_show_recy)
    RecyclerView mExhibitionRecyclerView;
    @Bind(R.id.search_collect_recy)
    RecyclerView mCollectionRecyclerView;
    @Bind(R.id.search_museum_lin)
    LinearLayout searchMuseumLin;
    @Bind(R.id.search_show_lin)
    LinearLayout searchShowLin;
    @Bind(R.id.search_collect_lin)
    LinearLayout searchCollectLin;
    @Bind(R.id.search_lin)
    LinearLayout searchLin;
    @Bind(R.id.search_data_list)
    View searchDataList;
    @Bind(R.id.search_no_data_lin)
    LinearLayout searchNoDataLin;

    List<SearchModel> searchModels;

    private CommonAdapter commonAdapter;//搜索历史
    private CommonAdapter commonMuseumAdapter;//博物馆历史
    private CommonAdapter commonShowAdapter;//展览历史
    private CommonAdapter commonCollectAdapter;//文物历史

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        //避免首次进入弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchLin.getVisibility() == View.GONE) {
                    searchLin.setVisibility(View.VISIBLE);
                    searchDataList.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        //获取最新的十条数据
        searchModels = getSearchDao().queryBuilder().orderDesc(SearchModelDao.Properties.Id).limit(10).list();

        set_recy(searchModels);
    }

    public void set_recy(List<SearchModel> list) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        commonAdapter = new CommonAdapter<SearchModel>(R.layout.list_search_item, list) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, final SearchModel item) {
                TextView search_text = baseViewHolder.getView(R.id.search_text);
                RelativeLayout search_item_delete = baseViewHolder.getView(R.id.search_item_delete);

                search_text.setText(item.getName());
                search_item_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletedata(item);

                        searchModels.clear();
                        searchModels.addAll(getSearchDao().queryBuilder().orderDesc(SearchModelDao.Properties.Id).limit(10).list());
                        notifyDataSetChanged();
                    }
                });
            }
        };

        mRecyclerView.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String text = searchModels.get(position).getName();

                if (!TextUtils.isEmpty(text)) {
                    searchEdit.setText(text);
                    searchEdit.setSelection(text.length());
                    //搜索
                    search(text);
                }

            }
        });
    }

    public void set_recy(final SearchListModel searchListModel) {
        mLocalRecyclerView.setHasFixedSize(true);
        mLocalRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity, LinearLayoutManager.HORIZONTAL, false);
        mLocalRecyclerView.setLayoutManager(linearLayoutManager);
        //是否显示博物馆
        if (searchListModel.getMusemuList() != null && searchListModel.getMusemuList().size() == 0) {
            searchMuseumLin.setVisibility(View.GONE);
        } else {
            searchMuseumLin.setVisibility(View.VISIBLE);
        }
        //是否显示展览
        if (searchListModel.getExhList() != null && searchListModel.getExhList().size() == 0) {
            searchShowLin.setVisibility(View.GONE);
        } else {
            searchShowLin.setVisibility(View.VISIBLE);
        }
        //是否显示文物
        if (searchListModel.getAntList() != null && searchListModel.getAntList().size() == 0) {
            searchCollectLin.setVisibility(View.GONE);
        } else {
            searchCollectLin.setVisibility(View.VISIBLE);
        }

        if (searchCollectLin.getVisibility() == View.GONE &&
                searchShowLin.getVisibility() == View.GONE &&
                searchMuseumLin.getVisibility() == View.GONE) {
            searchNoDataLin.setVisibility(View.VISIBLE);
        }else{
            searchNoDataLin.setVisibility(View.GONE);
        }

        commonMuseumAdapter = new CommonAdapter<MuseumModel>(R.layout.list_local_museum_item, searchListModel.getMusemuList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, MuseumModel item) {
                TextView museum_text = baseViewHolder.getView(R.id.museum_text);
                ImageView museum_img = baseViewHolder.getView(R.id.museum_img);

                museum_text.setText(item.getMsName());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getCover())
                        .apply(options)
                        .into(museum_img);
            }
        };

        mLocalRecyclerView.setAdapter(commonMuseumAdapter);

        commonMuseumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCctivity, MuseumDetailActivity.class);
                intent.putExtra("museum_id", searchListModel.getMusemuList().get(position).getMsId());
                intent.putExtra("museum_name", searchListModel.getMusemuList().get(position).getMsName());
                startActivity(intent);
            }
        });
        //展览
        mExhibitionRecyclerView.setHasFixedSize(true);
        mExhibitionRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mCctivity, LinearLayoutManager.HORIZONTAL, false);
        mExhibitionRecyclerView.setLayoutManager(linearLayoutManager2);

        commonShowAdapter = new CommonAdapter<ShowModel>(R.layout.list_exhibition_item, searchListModel.getExhList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ShowModel item) {
                TextView exhibition_name = baseViewHolder.getView(R.id.exhibition_name);
                ImageView exhibition_img = baseViewHolder.getView(R.id.exhibition_img);

                exhibition_name.setText(item.getTitle());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getCover())
                        .apply(options)
                        .into(exhibition_img);
            }
        };

        mExhibitionRecyclerView.setAdapter(commonShowAdapter);

        commonShowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCctivity, ShowDetailActivity.class);
                intent.putExtra("show_id", searchListModel.getExhList().get(position).getExhId());
                startActivity(intent);
            }
        });

        //馆藏
        mCollectionRecyclerView.setHasFixedSize(true);
        mCollectionRecyclerView.setNestedScrollingEnabled(false);

        /*GridLayoutManager linearLayoutManager3 = new GridLayoutManager(mCctivity, 2);
        mCollectionRecyclerView.setLayoutManager(linearLayoutManager3);*/

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(mCctivity, LinearLayoutManager.HORIZONTAL, false);
        mCollectionRecyclerView.setLayoutManager(linearLayoutManager3);

        commonCollectAdapter = new CommonAdapter<CollectModel>(R.layout.list_collection_recy, searchListModel.getAntList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, CollectModel item) {
                TextView collect_name = baseViewHolder.getView(R.id.collect_name);
                ImageView collect_img = baseViewHolder.getView(R.id.collect_img);
                TextView collect_city = baseViewHolder.getView(R.id.collect_city);

                collect_name.setText(item.getAnName());
                collect_city.setText(item.getYears());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getCover())
                        .apply(options)
                        .into(collect_img);
            }
        };

        mCollectionRecyclerView.setAdapter(commonCollectAdapter);

        commonCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCctivity, TreasureDetailActivity.class);
                intent.putExtra("collect_id", searchListModel.getAntList().get(position).getAnId());
                startActivity(intent);
            }
        });
    }

    /**
     * 搜索
     */
    public void search(String text) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.search(text);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @OnClick({R.id.search_cancel, R.id.search_rel, R.id.search_clear_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                finish();
                break;
            case R.id.search_rel://搜索
                String searchText = searchEdit.getText().toString();

                if (TextUtils.isEmpty(searchText)) {
                    AppToast.makeToast(mCctivity, "请输入搜索条件");
                    return;
                }

                insertdata(searchText);

                search(searchText);
                break;
            case R.id.search_clear_text:
                //清空全部数据
                new CommitDialog((Activity) mCctivity, searchDataList, handler, "是否清空搜索历史");
                break;
        }
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this, mCctivity);
    }

    private void insertdata(String text) {
        //插入数据
        SearchModel insertData = new SearchModel(null, text);
        try {
            getSearchDao().insert(insertData);

            searchModels.add(0, insertData);
            //如果超出10个则删掉最后一个
            if (searchModels.size() > 10) {
                searchModels.remove(searchModels.size() - 1);
            }

            commonAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletedata(SearchModel searchModel) {
        //删除数据
        getSearchDao().delete(searchModel);
    }

    private SearchModelDao getSearchDao() {
        return GreenDaoManager.getInstance().getSession().getSearchModelDao();
    }

    @Override
    public void SendResultSuccess(SearchListModel searchListModel) {
        searchLin.setVisibility(View.GONE);
        searchDataList.setVisibility(View.VISIBLE);

        set_recy(searchListModel);
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.COMMIT_DIALOG:
                    getSearchDao().deleteAll();

                    searchModels.clear();

                    commonAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}

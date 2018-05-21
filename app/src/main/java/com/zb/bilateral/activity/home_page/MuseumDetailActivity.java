package com.zb.bilateral.activity.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.example.mycommon.util.AppToast;
import com.zb.bilateral.Constants;
import com.zb.bilateral.fragment.AppointmentFragment;
import com.zb.bilateral.fragment.CollectFragment;
import com.zb.bilateral.fragment.DynamicFragment;
import com.zb.bilateral.fragment.ShowFragment;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.view.GlideRoundImage;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.dynamic.AnnouncementDetailActivity;
import com.zb.bilateral.activity.dynamic.InformateDetailActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.MuseumDetailModel;
import com.zb.bilateral.mvp.MuseumDetailPresenter;
import com.zb.bilateral.mvp.MuseumDetailView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.util.GlideImageLoader;
import com.zb.bilateral.view.CommitDialog;
import com.zb.bilateral.view.LooperTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MuseumDetailActivity extends BaseNewActivity<MuseumDetailPresenter> implements MuseumDetailView {
    @Bind(R.id.museum_detail_news_recy)
    RecyclerView mNewsRecyclerView;
    @Bind(R.id.museum_detail_activity_recy)
    RecyclerView mActivityRecyclerView;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.museum_detail_open_img)
    ImageView mOpenImg;
    @Bind(R.id.museum_detail_content)
    TextView mContentText;
    @Bind(R.id.museum_banner)
    Banner mBanner;
    @Bind(R.id.looper_text)
    LooperTextView looperText;
    @Bind(R.id.museum_announcement_lin)
    LinearLayout museumAnnouncementLin;
    @Bind(R.id.museum_activity_lin)
    LinearLayout museumActivityLin;
    @Bind(R.id.museum_info_lin)
    LinearLayout museumInfoLin;
    @Bind(R.id.museum_detail_no_data)
    TextView museumDetailNoData;


    DynamicFragment mDynamicFragment;
    ShowFragment mShowFragment;
    CollectFragment mCollectFragment;
    AppointmentFragment mAppointmentFragment;
    FragmentManager fragmentManager;

    @Bind(R.id.fragment_dynamic)
    LinearLayout fragmentDynamic;
    @Bind(R.id.fragment_show)
    LinearLayout fragmentShow;
    @Bind(R.id.fragment_collect)
    LinearLayout fragmentCollect;
    @Bind(R.id.fragment_appointment)
    LinearLayout fragmentAppointment;
    @Bind(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;
    @Bind(R.id.fragment_dynamic_text)
    TextView fragmentDynamicText;
    @Bind(R.id.fragment_show_text)
    TextView fragmentShowText;
    @Bind(R.id.fragment_collect_text)
    TextView fragmentCollectText;
    @Bind(R.id.fragment_appointment_text)
    TextView fragmentAppointmentText;
    @Bind(R.id.fragment_dynamic_img)
    ImageView fragmentDynamicImg;
    @Bind(R.id.fragment_show_img)
    ImageView fragmentShowImg;
    @Bind(R.id.fragment_collect_img)
    ImageView fragmentCollectImg;
    @Bind(R.id.fragment_appointment_img)
    ImageView fragmentAppointmentImg;
    @Bind(R.id.museum_detail_lin)
    LinearLayout museumDetailLin;
    @Bind(R.id.museum_content_lin)
    LinearLayout museumContentLin;

    private int item;

    private CommonAdapter commonNewsAdapter, commonActivityAdapter;
    private boolean isOpen = false;

    private String museum_id, museum_name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_museum_detail;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        museum_name = getIntent().getStringExtra("museum_name");
        museum_id = getIntent().getStringExtra("museum_id");

        topCenterText.setText(museum_name);
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
    }

    public void set_cycler(final MuseumDetailModel museumDetailModel) {
        //资讯
        if (museumDetailModel.getInfoList() != null && museumDetailModel.getInfoList().size() > 0) {
            museumInfoLin.setVisibility(View.VISIBLE);
        }
        mNewsRecyclerView.setHasFixedSize(true);
        mNewsRecyclerView.setNestedScrollingEnabled(false);
        mNewsRecyclerView.setFocusable(false);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(mCctivity, LinearLayoutManager.VERTICAL, false);
        mNewsRecyclerView.setLayoutManager(linearLayoutManager4);

        commonNewsAdapter = new CommonAdapter<ActivityModel>(R.layout.list_cultrue_item, museumDetailModel.getInfoList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ActivityModel item) {
                TextView cultrue_content = baseViewHolder.getView(R.id.cultrue_content);
                TextView cultrue_title = baseViewHolder.getView(R.id.cultrue_title);
                ImageView cultrue_img = baseViewHolder.getView(R.id.cultrue_img);

                cultrue_title.setText(item.getTitle());
                cultrue_content.setText(Html.fromHtml(item.getContent()));

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mCctivity));

                Glide.with(mCctivity)
                        .load(ApiStores.IMG_URL_TOP + item.getCover())
                        .apply(options)
                        .into(cultrue_img);
            }
        };

        mNewsRecyclerView.setAdapter(commonNewsAdapter);

        commonNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCctivity, InformateDetailActivity.class);
                intent.putExtra("activity_id", museumDetailModel.getInfoList().get(position).getId());
                startActivity(intent);
            }
        });

        //活动
        if (museumDetailModel.getActList() != null && museumDetailModel.getActList().size() > 0) {
            museumActivityLin.setVisibility(View.VISIBLE);
        }
        mActivityRecyclerView.setHasFixedSize(true);
        mActivityRecyclerView.setNestedScrollingEnabled(false);
        mActivityRecyclerView.setFocusable(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity, LinearLayoutManager.HORIZONTAL, false);
        mActivityRecyclerView.setLayoutManager(linearLayoutManager);

        commonActivityAdapter = new CommonAdapter<ActivityModel>(R.layout.list_activity_item, museumDetailModel.getActList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ActivityModel item) {
                ImageView museum_acticity_img = baseViewHolder.getView(R.id.museum_acticity_img);
                TextView museum_activity_name = baseViewHolder.getView(R.id.museum_activity_name);
                TextView activity_status_text = baseViewHolder.getView(R.id.activity_status_text);

                museum_activity_name.setText(Html.fromHtml(item.getTitle()));
                if (item.getStatus() != null && "0".equals(item.getStatus())) {
                    activity_status_text.setText("未开始");
                } else if (item.getStatus() != null && "1".equals(item.getStatus())) {
                    activity_status_text.setText("进行中");
                } else {
                    activity_status_text.setText("已结束");
                }

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mCctivity));

                Glide.with(mCctivity)
                        .load(ApiStores.IMG_URL_TOP + item.getCover())
                        .apply(options)
                        .into(museum_acticity_img);
            }
        };

        mActivityRecyclerView.setAdapter(commonActivityAdapter);

        commonActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCctivity, ActivityDetailActivity.class);
                intent.putExtra("activity_id", museumDetailModel.getActList().get(position).getId());
                intent.putExtra("status", museumDetailModel.getActList().get(position).getStatus());
                startActivity(intent);
            }
        });
    }

    /**
     * 博物馆详情
     */
    public void museum() {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.museum(museum_id, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    /**
     * 博物馆收藏/取消收藏
     */
    public void museumColl() {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.museumColl(museum_id, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @Override
    protected void initData() {
        museum();
    }

    @OnClick({R.id.top_left_img, R.id.museum_detail_open_rel, R.id.fragment_show, R.id.fragment_dynamic,
            R.id.fragment_collect, R.id.fragment_appointment, R.id.museum_tour_img, R.id.museum_entry_tour,
            R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.museum_detail_open_rel:
                if (isOpen) {
                    mContentText.setMaxLines(4);

                    mOpenImg.setBackgroundResource(R.mipmap.museum_arrow_down);

                    isOpen = false;
                } else {
                    mContentText.setMaxLines(100);

                    mOpenImg.setBackgroundResource(R.mipmap.museum_arrow_up);

                    isOpen = true;
                }
                break;
            case R.id.fragment_dynamic:
                clickButtomTag(fragmentDynamic);
                break;
            case R.id.fragment_show:
                clickButtomTag(fragmentShow);
                break;
            case R.id.fragment_collect:
                clickButtomTag(fragmentCollect);
                break;
            case R.id.fragment_appointment:
                clickButtomTag(fragmentAppointment);
                break;
            case R.id.museum_tour_img:
                Intent intent = new Intent(mCctivity, GalleryTourActivity.class);
                intent.putExtra("museum_id", museum_id);
                intent.putExtra("museum_name", museum_name);
                startActivity(intent);
                break;
            case R.id.museum_entry_tour:
                intent = new Intent(mCctivity, GalleryTourActivity.class);
                intent.putExtra("museum_id", museum_id);
                intent.putExtra("museum_name", museum_name);
                startActivity(intent);
                break;
            case R.id.top_right_rel://收藏取消收藏
                museumColl();
                break;
        }
    }

    @Override
    protected MuseumDetailPresenter createPresenter() {
        return new MuseumDetailPresenter(this, mCctivity);
    }

    boolean isCollect = false;

    @Override
    public void SendResultSuccess(final MuseumDetailModel museumDetailModel) {
        String content = museumDetailModel.getMuseum().getIntroduce();
        if (content != null && content.length() > 0) {
            museumContentLin.setVisibility(View.VISIBLE);
            mContentText.setText(museumDetailModel.getMuseum().getIntroduce());
        }

        if ("1".equals(museumDetailModel.getMuseum().getIsColl())) {
            isCollect = true;
            topRightImg.setBackgroundResource(R.mipmap.cultrue_collect_true);
        } else {
            isCollect = false;
            topRightImg.setBackgroundResource(R.mipmap.cultrue_collect);
        }

        if (museumDetailModel.getMuseum().getImgList() != null) {
            setBanner(museumDetailModel.getMuseum().getImgList());
        }

        set_cycler(museumDetailModel);
        //公告
        if (museumDetailModel.getNoticeList().size() > 0) {
            looperText.setVisibility(View.VISIBLE);
            museumDetailNoData.setVisibility(View.GONE);
        } else {
            museumDetailNoData.setVisibility(View.VISIBLE);
            looperText.setVisibility(View.GONE);
        }
        looperText.setTipList(generateTips(museumDetailModel.getNoticeList()));

        looperText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (museumDetailModel.getNoticeList().size() > 0) {
                    int currentItem = looperText.getCurrentitem();
                    if (currentItem > 0) {
                        Intent intent = new Intent(mCctivity, AnnouncementDetailActivity.class);
                        intent.putExtra("activity_id", museumDetailModel.getNoticeList().get(currentItem - 1).getId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void SendCollectSuccess(String msg) {
        if ("1".equals(msg)) {
            isCollect = true;
            topRightImg.setBackgroundResource(R.mipmap.cultrue_collect_true);
        } else {
            isCollect = false;
            topRightImg.setBackgroundResource(R.mipmap.cultrue_collect);
        }
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    private List<String> generateTips(List<ActivityModel> activityModels) {
        List<String> tips = new ArrayList<>();
        for (int i = 0; i < activityModels.size(); i++) {
            tips.add(activityModels.get(i).getTitle());
        }
        return tips;
    }

    public void setBanner(final List<BannerModel> listBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP + listBanner.get(i).getPath());
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片集合
        mBanner.setImages(images);
        //设置图片加载器
        mBanner.setImageLoader(new GlideCircleImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }


    public void clickButtomTag(View view) {
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }

        mainFrameLayout.setVisibility(View.VISIBLE);
        museumDetailLin.setVisibility(View.GONE);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidenFragments(transaction);
        switch (view.getId()) {
            case R.id.fragment_dynamic:
                click_HomePage();
                if (mDynamicFragment == null) {
                    mDynamicFragment = new DynamicFragment();

                    Bundle args = new Bundle();
                    args.putString("museum_id", museum_id);
                    mDynamicFragment.setArguments(args);

                    transaction.add(R.id.main_frameLayout, mDynamicFragment);
                } else {
                    transaction.show(mDynamicFragment);
                }

                break;
            case R.id.fragment_show:
                click_show();
                if (mShowFragment == null) {
                    mShowFragment = new ShowFragment();
                    Bundle args = new Bundle();
                    args.putString("museum_id", museum_id);
                    mShowFragment.setArguments(args);
                    transaction.add(R.id.main_frameLayout, mShowFragment);
                } else {
                    transaction.show(mShowFragment);
                }
                break;
            case R.id.fragment_collect:
                click_collect();
                if (mCollectFragment == null) {
                    mCollectFragment = new CollectFragment();
                    Bundle args = new Bundle();
                    args.putString("museum_id", museum_id);
                    mCollectFragment.setArguments(args);
                    transaction.add(R.id.main_frameLayout, mCollectFragment);
                } else {
                    transaction.show(mCollectFragment);
                }
                break;
            case R.id.fragment_appointment:
                click_appointment();
                if (mAppointmentFragment == null) {
                    mAppointmentFragment = new AppointmentFragment();
                    Bundle args = new Bundle();
                    args.putString("museum_id", museum_id);
                    mAppointmentFragment.setArguments(args);
                    transaction.add(R.id.main_frameLayout, mAppointmentFragment);
                } else {
                    transaction.show(mAppointmentFragment);
                }
                break;
            default:
                break;
        }

        transaction.commit();
    }

    private void hidenFragments(FragmentTransaction transaction) {
        if (mDynamicFragment != null) {
            transaction.hide(mDynamicFragment);
        }
        if (mShowFragment != null) {
            transaction.hide(mShowFragment);
        }
        if (mCollectFragment != null) {
            transaction.hide(mCollectFragment);
        }
        if (mAppointmentFragment != null) {
            transaction.hide(mAppointmentFragment);
        }
    }

    public void click_HomePage() {
        fragmentDynamicImg.setBackgroundResource(R.mipmap.dynamic_true);
        fragmentShowImg.setBackgroundResource(R.mipmap.show_false);
        fragmentCollectImg.setBackgroundResource(R.mipmap.collect_false);
        fragmentAppointmentImg.setBackgroundResource(R.mipmap.appointment_false);
        fragmentDynamicText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentShowText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCollectText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentAppointmentText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_show() {
        fragmentDynamicImg.setBackgroundResource(R.mipmap.dynamic_false);
        fragmentShowImg.setBackgroundResource(R.mipmap.show_true);
        fragmentCollectImg.setBackgroundResource(R.mipmap.collect_false);
        fragmentAppointmentImg.setBackgroundResource(R.mipmap.appointment_false);
        fragmentDynamicText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentShowText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentCollectText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentAppointmentText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_collect() {
        fragmentDynamicImg.setBackgroundResource(R.mipmap.dynamic_false);
        fragmentShowImg.setBackgroundResource(R.mipmap.show_false);
        fragmentCollectImg.setBackgroundResource(R.mipmap.collect_true);
        fragmentAppointmentImg.setBackgroundResource(R.mipmap.appointment_false);
        fragmentDynamicText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentShowText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCollectText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentAppointmentText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_appointment() {
        fragmentDynamicImg.setBackgroundResource(R.mipmap.dynamic_false);
        fragmentShowImg.setBackgroundResource(R.mipmap.show_false);
        fragmentCollectImg.setBackgroundResource(R.mipmap.collect_false);
        fragmentAppointmentImg.setBackgroundResource(R.mipmap.appointment_true);
        fragmentDynamicText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentShowText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCollectText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentAppointmentText.setTextColor(getResources().getColor(R.color.main_color));
    }

    public void close() {
        mainFrameLayout.setVisibility(View.GONE);
        museumDetailLin.setVisibility(View.VISIBLE);

        fragmentDynamicImg.setBackgroundResource(R.mipmap.dynamic_false);
        fragmentShowImg.setBackgroundResource(R.mipmap.show_false);
        fragmentCollectImg.setBackgroundResource(R.mipmap.collect_false);
        fragmentAppointmentImg.setBackgroundResource(R.mipmap.appointment_false);
        fragmentDynamicText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentShowText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCollectText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentAppointmentText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mainFrameLayout.getVisibility() == View.VISIBLE) {
                close();
            } else {
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

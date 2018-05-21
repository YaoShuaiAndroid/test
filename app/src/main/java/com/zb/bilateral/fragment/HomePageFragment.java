package com.zb.bilateral.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.DownloadUtil;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.util.LogUtil;
import com.example.mycommon.util.PermissionUtils;
import com.zb.bilateral.Constants;
import com.zb.bilateral.model.CultrueHomeModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.view.GlideRoundImage;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.cultrue.CultrueDetailActivity;
import com.zb.bilateral.activity.home_page.LocationActivity;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.activity.home_page.SearchActivity;
import com.zb.bilateral.activity.home_page.ShowDetailActivity;
import com.zb.bilateral.activity.home_page.TreasureDetailActivity;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CollectModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.HomePageModel;
import com.zb.bilateral.model.MuseumModel;
import com.zb.bilateral.model.ShowModel;
import com.zb.bilateral.mvp.HomePagePresenter;
import com.zb.bilateral.mvp.HomePageView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.GlideCatchUtil;
import com.zb.bilateral.util.GlideImageLoader;
import com.zb.bilateral.util.LocationUtils;
import com.zb.bilateral.view.CommitDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class HomePageFragment extends BaseNewFragment<HomePagePresenter>
        implements SwipeRefreshLayout.OnRefreshListener, HomePageView, EasyPermissions.PermissionCallbacks {
    @Bind(R.id.homt_local_museum_recy)
    RecyclerView mLocalRecyclerView;//当地博物馆
    @Bind(R.id.home_recommend_recy)
    RecyclerView mRecommendRecyclerView;//推荐博物馆
    @Bind(R.id.home_exhibition_recy)
    RecyclerView mExhibitionRecyclerView;//展览
    @Bind(R.id.home_collection_recy)
    RecyclerView mCollectionRecyclerView;//馆藏
    @Bind(R.id.home_cultrue_recy)
    RecyclerView mCultrueRecyclerView;
    @Bind(R.id.top_left_text)
    TextView topLeftText;
    @Bind(R.id.banner)
    Banner mBanner;
    @Bind(R.id.home_page_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.home_local_museum_lin)
    LinearLayout homeLocalMuseumLin;//当地博物馆布局

    private CommonAdapter commonLocalAdapter, commonRecommendAdapter, commonExhibitionAdapter, commonCollectionAdapter,
            commonCultrueAdapter;

    private MyApplication myApplication;
    private MainActivity mainActivity;

    private HomePageModel homePageModel = null;
    private PublicModel publicModel = null;

    private String city_id = "-1";//选择的城市id，默认-1为获取的定位
    private final static int LOCATION_REQUEST = 1000;
    private final static int LOCATION_RESULT = 100;
    private final int WRITE_EXTERNAL_STORAGE = 2;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    HomePageFragment fragment;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.COMMIT_DIALOG:
                    //下载
                    LogUtil.i("loadApk");
                    showDownloadProgressDialog();

                    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !EasyPermissions.hasPermissions(mContext, perms)) {
                        EasyPermissions.requestPermissions(fragment, "中博讲解需要打开操作文件存储权限", WRITE_EXTERNAL_STORAGE, perms);
                    } else {
                        loadApk();
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_page_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        myApplication = (MyApplication) getActivity().getApplicationContext();

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        //首先拿取缓存的数据填充
        homePageModel = (HomePageModel) AppUtil.load(FileUtils.homePath(mContext));
        if (homePageModel != null) {
            set_recy(homePageModel);
        }

        fragment=this;

        initPermission();
    }

    public void checkVersion() {
        publicModel = (PublicModel) AppUtil.load(FileUtils.publicPath(mContext));

        if (publicModel == null) {
            return;
        }

        int version=0;
        try{
            version=Integer.parseInt(publicModel.getVersion());
        }catch (Exception e){
            //AppToast.makeToast(mContext,"后台类型异常");
        };


        if(AppUtil.getVersionCode(mContext)<version){
            new CommitDialog((Activity) mContext, topLeftText, handler, "当前有新版本 V" + publicModel.getVersion() + ",是否更新?");
        }
    }

    @Override
    protected void initData() {
        getBanner();

        getData(myApplication.getAddress());
    }


    /**
     * 获取轮播图
     */
    public void getBanner() {
        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.banner("1");
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    /**
     * 首页数据
     */
    public void getData(String city) {
        if (AppUtil.checkNetWork(mContext)) {
            if (city_id.equals("-1")) {
                //获取定位
                mvpPresenter.getIndex(city);
            } else {
                mvpPresenter.getCityIdIndex(city_id);
            }
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    public void set_recy(final HomePageModel homePageModel) {
        mLocalRecyclerView.setHasFixedSize(true);
        mLocalRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLocalRecyclerView.setLayoutManager(linearLayoutManager);

        if (homePageModel.getLocalMuseumList() != null && homePageModel.getLocalMuseumList().size() == 0) {
            homeLocalMuseumLin.setVisibility(View.GONE);
        } else {
            homeLocalMuseumLin.setVisibility(View.VISIBLE);
        }

        commonLocalAdapter = new CommonAdapter<MuseumModel>(R.layout.list_local_museum_item, homePageModel.getLocalMuseumList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, MuseumModel item) {
                TextView museum_text = baseViewHolder.getView(R.id.museum_text);
                ImageView museum_img = baseViewHolder.getView(R.id.museum_img);

                museum_text.setText(item.getLmName());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getLmCover())
                        .apply(options)
                        .into(museum_img);
            }
        };

        mLocalRecyclerView.setAdapter(commonLocalAdapter);

        commonLocalAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MuseumDetailActivity.class);
                intent.putExtra("museum_id", homePageModel.getLocalMuseumList().get(position).getLmId());
                intent.putExtra("museum_name", homePageModel.getLocalMuseumList().get(position).getLmName());
                startActivity(intent);
            }
        });
        //推荐
        mRecommendRecyclerView.setHasFixedSize(true);
        mRecommendRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager1);

        commonRecommendAdapter = new CommonAdapter<MuseumModel>(R.layout.list_recommend_museum_recy1, homePageModel.getRecoMuseumList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, MuseumModel item) {
                TextView recom_museum_text = baseViewHolder.getView(R.id.recom_museum_text);
                ImageView recom_museum_img = baseViewHolder.getView(R.id.recom_museum_img);

                recom_museum_text.setText(item.getRmName());
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getRmLogo())
                        .apply(options)
                        .into(recom_museum_img);
            }
        };

        mRecommendRecyclerView.setAdapter(commonRecommendAdapter);

        commonRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MuseumDetailActivity.class);
                intent.putExtra("museum_id", homePageModel.getRecoMuseumList().get(position).getRmId());
                intent.putExtra("museum_name", homePageModel.getRecoMuseumList().get(position).getRmName());
                startActivity(intent);
            }
        });
        //展览
        mExhibitionRecyclerView.setHasFixedSize(true);
        mExhibitionRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mExhibitionRecyclerView.setLayoutManager(linearLayoutManager2);

        commonExhibitionAdapter = new CommonAdapter<ShowModel>(R.layout.list_exhibition_item, homePageModel.getRecoExhList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ShowModel item) {
                TextView exhibition_name = baseViewHolder.getView(R.id.exhibition_name);
                ImageView exhibition_img = baseViewHolder.getView(R.id.exhibition_img);

                exhibition_name.setText(item.getReTitle());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getRECOVER())
                        .apply(options)
                        .into(exhibition_img);
            }
        };

        mExhibitionRecyclerView.setAdapter(commonExhibitionAdapter);

        commonExhibitionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ShowDetailActivity.class);
                intent.putExtra("show_id", homePageModel.getRecoExhList().get(position).getReId());
                startActivity(intent);
            }
        });

        //馆藏
        mCollectionRecyclerView.setHasFixedSize(true);
        mCollectionRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mCollectionRecyclerView.setLayoutManager(linearLayoutManager3);

        commonCollectionAdapter = new CommonAdapter<CollectModel>(R.layout.list_collection_recy, homePageModel.getRecoTreaList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, CollectModel item) {
                TextView collect_name = baseViewHolder.getView(R.id.collect_name);
                ImageView collect_img = baseViewHolder.getView(R.id.collect_img);
                TextView collect_city = baseViewHolder.getView(R.id.collect_city);

                collect_name.setText(item.getRtName());
                collect_city.setText(item.getYears());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getRtCover())
                        .apply(options)
                        .into(collect_img);
            }
        };

        mCollectionRecyclerView.setAdapter(commonCollectionAdapter);

        commonCollectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, TreasureDetailActivity.class);
                intent.putExtra("collect_id", homePageModel.getRecoTreaList().get(position).getRtId());
                startActivity(intent);
            }
        });
        //文化拾遗
        mCultrueRecyclerView.setHasFixedSize(true);
        mCultrueRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mCultrueRecyclerView.setLayoutManager(linearLayoutManager4);

        commonCultrueAdapter = new CommonAdapter<CultrueHomeModel>(R.layout.list_cultrue_item, homePageModel.getGleaList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, CultrueHomeModel item) {
                TextView cultrue_content = baseViewHolder.getView(R.id.cultrue_content);
                TextView cultrue_title = baseViewHolder.getView(R.id.cultrue_title);
                ImageView cultrue_img = baseViewHolder.getView(R.id.cultrue_img);

                cultrue_content.setText(item.getDigest());
                cultrue_title.setText(item.getGleaTitle());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(ApiStores.IMG_URL_TOP + item.getGleaCover())
                        .apply(options)
                        .into(cultrue_img);
            }
        };

        mCultrueRecyclerView.setAdapter(commonCultrueAdapter);

        commonCultrueAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, CultrueDetailActivity.class);
                intent.putExtra("cultrue_id", homePageModel.getGleaList().get(position).getGleaId());
                startActivity(intent);
            }
        });
    }

    public void setBanner(final List<BannerModel> listBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP + listBanner.get(i).getPath());
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                try {
                    Uri uri = Uri.parse(listBanner.get(position).getLink());
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @OnClick({R.id.top_left_rel, R.id.top_right_rel, R.id.home_cultrue_rel, R.id.home_local_museum_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                Intent intent = new Intent(mContext, LocationActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST);
                break;
            case R.id.top_right_rel://搜索
                intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.home_cultrue_rel://文化拾遗
                mainActivity.changeCultrue();
                break;
            case R.id.home_local_museum_rel:
                mainActivity.changeMuseum();
                break;
        }
    }

    @Override
    protected HomePagePresenter createPresenter() {
        return new HomePagePresenter(this, mContext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST && resultCode == LOCATION_RESULT) {
            String cityName = data.getStringExtra("city_name");

            city_id = data.getStringExtra("city_id");
            topLeftText.setText(data.getStringExtra("city_name"));

            myApplication.setAddress(cityName);
            myApplication.setCityId(city_id);

            getData("" + data.getStringExtra("city_name"));
        }
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        setBanner(bannerModels);

        //判断当前是否有新版本
        //checkVersion();
    }

    @Override
    public void SendResultSuccess(HomePageModel homePageModel) {
        AppUtil.save(homePageModel, FileUtils.homePath(mContext));

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        set_recy(homePageModel);

        if (isCheck) {
            checkVersion();
        }
    }

    @Override
    public void SendResultFail(String msg) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        AppToast.makeToast(mContext, msg);
    }


    boolean flag;

    private final int PERMISSIONS_LOCATION = 1;

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (!EasyPermissions.hasPermissions(mContext, perms)) {
                EasyPermissions.requestPermissions(this, "中博讲解需要打开定位权限", PERMISSIONS_LOCATION, perms);
            } else {
                flag = true;

                getLocation();
            }
        } else {
            flag = true;

            getLocation();
        }
    }

    /**
     * 权限的结果回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 获取定位信息
     */
    public void getLocation() {
        mLocationClient = new LocationClient(mContext);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode==PERMISSIONS_LOCATION){
            getLocation();
        }else{
            loadApk();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        AppToast.showShortText(mContext, R.string.permission_denied_hint);
    }

    @Override
    public void onRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        getBanner();

        getData(topLeftText.getText().toString());
    }

    boolean isCheck = false;

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String city = location.getCity();    //获取城市

            if (!TextUtils.isEmpty(city)) {
                mLocationClient.stop();
                mLocationClient.unRegisterLocationListener(this);
            }

            myApplication.setAddress(city);
            myApplication.setCityId("-1");

            topLeftText.setText(city);

            getData(city);

            isCheck = true;
        }
    }

    public void loadApk() {
        progressDialog.show();

        DownloadUtil.get().download(publicModel.getApk(), mainActivity.getExternalCacheDir().getPath(),
                publicModel.getVersion() + ".apk", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String fileName = mainActivity.getExternalCacheDir().getPath() + "/" + publicModel.getVersion() + ".apk";
                                //打开下载成功的pdf文件
                                openFile(new File(fileName));

                                progressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        progressDialog.setProgress(progress);
                    }

                    @Override
                    public void onDownloadFailed() {

                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                AppToast.makeToast(mContext, "下载失败");
                            }
                        });

                        progressDialog.dismiss();
                    }
                });
    }

    private void openFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Uri contentUri = FileProvider.getUriForFile(mContext,"com.zb.bilateral.fileprovider",file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    ProgressDialog progressDialog = null;

    private void showDownloadProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);                    //设置不可点击界面之外的区域让对话框小时
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         //进度条类型
    }
}

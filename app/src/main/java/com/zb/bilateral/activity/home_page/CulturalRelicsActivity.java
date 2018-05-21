package com.zb.bilateral.activity.home_page;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.util.LogUtil;
import com.example.mycommon.util.StatusBarCompat;
import com.example.mycommon.view.GlideRoundImage;
import com.liji.imagezoom.util.ImageZoom;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.Constants;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.home_page.same_relics.SameRelicsActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.brocast.MyMusicBrocast;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.MuseumModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.model.ShowRoomModel;
import com.zb.bilateral.model.SongInfo;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.CultrueRelicsPresenter;
import com.zb.bilateral.mvp.CultrueRelicsView;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.service.MuiscService;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.util.MyConstant;
import com.zb.bilateral.util.ShareUtil;
import com.zb.bilateral.view.NearCultrueDialog;
import com.zb.bilateral.view.SetMoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class CulturalRelicsActivity extends BaseNewActivity<CultrueRelicsPresenter> implements CultrueRelicsView, EasyPermissions.PermissionCallbacks {
    private final static int NEAR_HALL = 11;
    private final int PERMISSIONS_CAMERA = 1;

    @Bind(R.id.cultural_relics_recy)
    RecyclerView mRecyclerView;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.cultrue_relics_content)
    TextView cultrueRelicsContent;
    @Bind(R.id.banner)
    Banner mBanner;
    @Bind(R.id.cultrue_song_play)
    ImageView cultrueSongPlay;
    @Bind(R.id.cultrue_song_pause)
    ImageView cultrueSongPause;
    @Bind(R.id.cultrue_relics_seekbar)
    SeekBar mSeekBar;
    @Bind(R.id.cultrue_relics_end_time)
    TextView songEndTime;
    @Bind(R.id.cultrue_relics_start_time)
    TextView songStartTime;
    @Bind(R.id.museum_detail_open_img)
    ImageView mOpenImg;
    @Bind(R.id.cultrue_relics_scoll)
    ScrollView cultrueRelicsScoll;

    private String cultrueId;//文物id
    //文物编号
    private String cultrueNo;
    /*
    * 当前播放的model
    * */
    private CultrueModel cultrueModels;
    /**
     * 当前播放进度
     */
    private long currentprogress=0;

    //展厅内所有文物
    private List<CultrueModel> allCultrues;
    //该文物下的附近文物
    List<CultrueModel> nearCultrues;

    //音效广播
    private MyMusicBrocast music_brocast;
    /**
     * 音效链接
     */
    private String songStr="";
    //当前文物是在展厅文物中的排序
    private int cultrueItem = 0;
    //当销毁下一页播放时为true
    private boolean isOnStop=false;

    //当在切换数据时默认不可点击
    private boolean isClick = true;
    //内容是否展开
    private boolean isOpen = false;

    private MyApplication myApplication;

    private final static int REQUEST_CODE = 1000;
    //跳转下一页是否继续播放播放不执行暂停操作
    private boolean isPause=false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cultural_relics;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        myApplication = (MyApplication) getApplicationContext();

        cultrueId = getIntent().getStringExtra("cultrue_id");
        cultrueNo=getIntent().getStringExtra("cultrue_no");

        //设置状态栏颜色
        StatusBarCompat.compat((Activity) mCctivity, 3);

        //进度条改变广播
        mSeekBar.setOnSeekBarChangeListener(seek_change);
    }

    @Override
    protected void initData() {
        antiques();
        //首次进入先暂停音频
        pause();

        nearbyAntiques();
    }

    /**
     * 注册音频改变广播
     */
    public void setMusicBrocast() {
        music_brocast = new MyMusicBrocast(handler);
        registerReceiver(music_brocast, Constants.getIntentFilter());
    }

    /**
     * 相似文物
     */
    public void antiques() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.antiques(cultrueId);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 展厅文物
     */
    public void nearbyAntiques() {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.nearbyAntiques(cultrueId, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 获取当前输入的文物编号是否存在
     */
    public void antiques1(String no) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.antiques1(no);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMusicBrocast();
        isPause=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!isPause){
            LogUtil.i("暂停");
            pause();
        }

        //停止广播
        unregisterReceiver(music_brocast);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnStop=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将音频回位到0
        SongInfo s = new SongInfo();
        s.setPlayProgress(0);
        myApplication.setPlaying(s);

        //退出停止音效
        pause();
    }

    @OnClick({R.id.top_left_rel, R.id.cultrue_relics_meun_rel, R.id.cultrue_song_pause, R.id.cultrue_song_play,
            R.id.cultrue_relics_next_img, R.id.cultrue_relics_in_img, R.id.museum_detail_open_rel, R.id.cultrue_scan_rel,
            R.id.cultrue_relics_message_rel, R.id.cultrue_more_rel})
    public void onClick(View v) {
        if (!isClick) {
            return;
        }

        switch (v.getId()) {
            case R.id.top_left_rel:
                finish();
                break;
            case R.id.cultrue_scan_rel://扫描文物
                //扫描进入文物
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] perms = {Manifest.permission.CAMERA};
                    if (!EasyPermissions.hasPermissions(mCctivity, perms)) {
                        EasyPermissions.requestPermissions(this, "APP需要打开相机权限", PERMISSIONS_CAMERA, perms);
                        return;
                    } else {
                        WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                    }
                } else {
                    WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                }
                break;
            case R.id.cultrue_relics_meun_rel:
                //附近文物
                if (allCultrues == null) {
                    nearbyAntiques();
                } else {
                    new NearCultrueDialog((Activity) mCctivity, topCenterText, handler, nearCultrues);
                }
                break;
            case R.id.cultrue_song_pause:
                pause();
                break;
            case R.id.cultrue_song_play:
                play();
                break;
            case R.id.cultrue_relics_next_img:
                //下一曲
                nextHall();

                isClick = false;
                break;
            case R.id.cultrue_relics_in_img:
                //上一曲
                inHall();

                isClick = false;
                break;
            case R.id.museum_detail_open_rel:
                if (isOpen) {
                    cultrueRelicsContent.setMaxLines(4);

                    mOpenImg.setBackgroundResource(R.mipmap.museum_arrow_down);

                    isOpen = false;
                } else {
                    cultrueRelicsContent.setMaxLines(100);

                    mOpenImg.setBackgroundResource(R.mipmap.museum_arrow_up);

                    isOpen = true;
                }
                break;
            case R.id.cultrue_relics_message_rel:
                //文物评论消息
                Intent intent = new Intent(mCctivity, CultrueCommentActivity.class);
                intent.putExtra("cultrue_id", cultrueId);
                startActivity(intent);
                break;
            case R.id.cultrue_more_rel:
                //更多
                new SetMoreDialog((Activity) mCctivity, topCenterText, handler,true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = data.getStringExtra("result_string");

                antiques1((String) result);
            }
        }
    }

    @Override
    protected CultrueRelicsPresenter createPresenter() {
        return new CultrueRelicsPresenter(this, mCctivity);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NEAR_HALL:
                    CultrueModel cultrueModel = (CultrueModel) msg.obj;

                    isClick = false;

                    setCurrentCultrue(cultrueModel);
                    break;
                case 0://播放
                    cultrueSongPause.setVisibility(View.VISIBLE);
                    cultrueSongPlay.setVisibility(View.GONE);
                    break;
                case 1://暂停、停止
                    cultrueSongPause.setVisibility(View.GONE);
                    cultrueSongPlay.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    //进度条更新
                    if (myApplication.getPlaying() != null&& songStr
                            .equals(myApplication.getPlaying().getSongPath())) {
                        long playProgress = myApplication.getPlaying().getPlayProgress();
                        long totalTime = myApplication.getPlaying().getTotal_time();

                        if (totalTime != 0) {
                            double size = playProgress * 100 / totalTime;

                            mSeekBar.setProgress((int) size);
                            songStartTime.setText(AppUtil.get_time((int) playProgress));
                            songEndTime.setText(AppUtil.get_time((int) totalTime));
                        }
                    }
                    break;
                case 3:
                    //播放完成
                    cultrueSongPlay.setVisibility(View.VISIBLE);
                    cultrueSongPause.setVisibility(View.GONE);
                    break;
                case Constants.MAIN_ACTIVITY:
                    //返回首页
                    Intent intent = new Intent(mCctivity, MainActivity.class);
                    intent.putExtra("item", 0);
                    startActivity(intent);
                    break;
                case Constants.MUSEUM_ACTIVITY:
                    //返回博物馆
                    intent = new Intent(mCctivity, MuseumDetailActivity.class);
                    startActivity(intent);
                    break;
                case Constants.SHOW_ACTIVITY:
                    //返回展厅导览
                    intent = new Intent(mCctivity, GalleryTourActivity.class);
                    startActivity(intent);
                    break;
                case Constants.PERSON_ACTIVITY:
                    //返回个人中心
                    intent = new Intent(mCctivity, MainActivity.class);
                    intent.putExtra("item", 3);
                    startActivity(intent);
                    break;
                case Constants.SHARE_ACTIVITY:
                    //分享
                    PublicModel publicMode= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));

                    if(cultrueModels==null||publicMode==null){
                        AppToast.makeToast(mCctivity,"数据异常");
                        return;
                    }

                    new ShareUtil().share(cultrueModels.getName(),cultrueModels.getIntroduce(),
                            publicMode,mCctivity);
                    break;
                default:
                    break;
            }
        }
    };

    CultrueDetailModel cultrueDetailModel;
    @Override
    public void SendResultSuccess(CultrueDetailModel cultrueDetail) {
        cultrueRelicsScoll.setVisibility(View.VISIBLE);
        cultrueDetailModel = cultrueDetail;
        //第一次进入
        if (isClick) {
            cultrueModels=cultrueDetailModel.getAntiques();

            cultrueRelicsContent.setText(cultrueDetailModel.getAntiques().getIntroduce());
            topCenterText.setText(cultrueDetailModel.getAntiques().getName());
            //设置轮播图
            setBanner(cultrueDetailModel.getAntiques().getImgList());

            int seconds=cultrueModels.getLs()%60;
            songEndTime.setText(cultrueModels.getLs()/60+":"+(seconds<10?"0"+seconds:seconds));
        }
        //获取完成数据默认可以点击
        isClick = true;
        //设置相似文物
        setRecy(cultrueDetailModel.getLikeAntiques());
    }

    @Override
    public void SubmitSuccess(CultrueDetailModel cultrueDetailModel) {
        String id = cultrueDetailModel.getAntiques().getId();
        if (TextUtils.isEmpty(id)) {
            AppToast.makeToast(mCctivity, "数据异常");
            return;
        }

        Intent intent = new Intent(mCctivity, CulturalRelicsActivity.class);
        intent.putExtra("cultrue_id", "" + id);
        startActivity(intent);
    }

    @Override
    public void SendResultFail(String msg) {
        isClick = true;
        AppToast.makeToast(mCctivity, msg);
    }

    @Override
    public void nearResultSuccess(CultrueListModel cultrueListModel) {
        allCultrues = cultrueListModel.getAntiquesList();

        getNearCultrues(allCultrues);
    }


    /**
     * 获取当前文物下的附近文物
     *
     * @param cultrueListModel 该展厅所有文物
     */
    public void getNearCultrues(List<CultrueModel> cultrueListModel) {
        nearCultrues = new ArrayList<>();
        for (int i = 0; i < cultrueListModel.size(); i++) {
            if (!cultrueListModel.get(i).getId().equals(cultrueId)) {
                nearCultrues.add(cultrueListModel.get(i));
            } else {
                cultrueItem = i;
            }
        }
    }

    public void setCurrentCultrue(CultrueModel cultrueModel) {
        cultrueId = cultrueModel.getId();
        cultrueModels=cultrueModel;
        //起始先暂停
        playOther();

        MuiscService.isPlaying = false;
        mSeekBar.setProgress(0);
        currentprogress = 0;
        songStartTime.setText("00:00");

        //play();

        cultrueRelicsContent.setText(cultrueModel.getIntroduce());
        topCenterText.setText(cultrueModel.getName());
        //设置轮播图
        setBanner(cultrueModel.getImgList());

        //获取新展现文物的相似文物
        antiques();
        //更新附近文物
        if (allCultrues != null) {
            getNearCultrues(allCultrues);
        }
    }

    //暂停
    public void playOther() {
        if (MuiscService.isPlaying) {
            cultrueSongPlay.setVisibility(View.VISIBLE);
            cultrueSongPause.setVisibility(View.GONE);

            Intent pause_boradcast = new Intent(MyConstant.PLAY_OTHER);
            sendBroadcast(pause_boradcast);
        }
    }

    //暂停
    public void pause() {
        if (MuiscService.isPlaying) {
            try {
                cultrueSongPlay.setVisibility(View.VISIBLE);
                cultrueSongPause.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent pause_boradcast = new Intent(MyConstant.PAUSE);
            sendBroadcast(pause_boradcast);
        }
    }

    //播放
    public void play() {
        //防止多次重复点击造成崩溃，所以控制只有在播放和暂停已经完成后才能继续点击
        if(TextUtils.isEmpty(cultrueModels.getVoice())){
            AppToast.makeToast(mCctivity,"暂无音频，无法播放");
            return;
        }

        if (myApplication.is_click()) {
            if (!MuiscService.isPlaying) {
                cultrueSongPlay.setVisibility(View.GONE);
                cultrueSongPause.setVisibility(View.VISIBLE);

                //重新赋值播放内容
                SongInfo s = new SongInfo();
                s.setId("" + cultrueModels.getId());
                s.setPlayProgress(currentprogress);
                s.setSongPath(ApiStores.IMG_URL_TOP + cultrueModels.getVoice());
                myApplication.setPlaying(s);
                myApplication.setIs_click(false);

                songStr=ApiStores.IMG_URL_TOP + cultrueModels.getVoice();

                //发送播放广播
                if (!MuiscService.isServiceRunning) {
                    Intent intent = new Intent(mCctivity, MuiscService.class);
                    startService(intent);
                } else if(isOnStop){
                    Intent play = new Intent(MyConstant.PAUSE_TO_PLAY);
                    sendBroadcast(play);
                }else {
                    Intent play = new Intent(MyConstant.PLAY);
                    sendBroadcast(play);
                }
            } else {
                playTo(0);
            }
        }
    }

    //播放拖动
    SeekBar.OnSeekBarChangeListener seek_change = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            currentprogress=myApplication.getPlaying().getPlayProgress();
        }

        public void onStartTrackingTouch(SeekBar arg0) {
        }

        public void onStopTrackingTouch(SeekBar arg0) {
            int progess = arg0.getProgress(); //getProgress( )获得当前的位置
            //根据总长度获得拖动的长度
            int current_posistion = (int) myApplication.getPlaying().getTotal_time() * progess / 100;
            playTo(current_posistion);

            songStartTime.setText(AppUtil.get_time(current_posistion));
        }
    };

    //拖动至
    public void playTo(int progress) {
        Intent play_boradcast = new Intent(MyConstant.PLAYTO);
        play_boradcast.putExtra("position", progress);
        sendBroadcast(play_boradcast);

        cultrueSongPlay.setVisibility(View.GONE);
        cultrueSongPause.setVisibility(View.VISIBLE);
    }

    private CommonAdapter commonAdapter;

    public void setRecy(final List<CultrueModel> cultrueModels) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        commonAdapter = new CommonAdapter<CultrueModel>(R.layout.list_recommend_museum_recy, cultrueModels) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, CultrueModel item) {
                TextView recom_museum_text = baseViewHolder.getView(R.id.recom_museum_text);
                ImageView recom_museum_img = baseViewHolder.getView(R.id.recom_museum_img);

                recom_museum_text.setText(item.getMuseumName());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mCctivity));

                Glide.with(mCctivity)
                        .load(ApiStores.IMG_URL_TOP + item.getCover())
                        .apply(options)
                        .into(recom_museum_img);
            }
        };

        mRecyclerView.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCctivity, SameRelicsActivity.class);
                intent.putParcelableArrayListExtra("cultrue", (ArrayList<? extends Parcelable>) cultrueDetailModel.getLikeAntiques());
                intent.putExtra("item", position);
                startActivity(intent);
            }
        });
    }

    public void setBanner(final List<BannerModel> listBanner) {
        final List<String> images = new ArrayList<>();

        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP + listBanner.get(i).getPath());
        }

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                isPause=true;
                ImageZoom.show(mCctivity, images.get(position), images);
            }
        });

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideCircleImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(false);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        if (listBanner != null && listBanner.size() > 0) {
            mBanner.start();
        }
    }

    /**
     * 显示下一文物的数据
     */
    public void nextHall() {
        if (nearCultrues == null|| nearCultrues.size() == 0) {
            AppToast.makeToast(mCctivity, "当前暂无附近文物");
            return;
        }


        if (allCultrues.size() - 1 > cultrueItem) {
            cultrueItem++;
        } else {
            cultrueItem = 0;
        }

        setCurrentCultrue(allCultrues.get(cultrueItem));

        cultrueRelicsScoll.setVisibility(View.GONE);
        cultrueRelicsScoll.setVisibility(View.VISIBLE);
        // 向右边移入
        cultrueRelicsScoll.setAnimation(AnimationUtils.makeInAnimation(this, true));
    }

    /**
     * 显示上一文物的数据
     */
    public void inHall() {
        if (nearCultrues == null || nearCultrues.size() == 0) {
            AppToast.makeToast(mCctivity, "当前暂无附近文物");
            return;
        }

        CultrueModel cultrueModel = null;

        if (cultrueItem != 0) {
            cultrueItem--;
        } else {
            cultrueItem = allCultrues.size() - 1;
        }

        cultrueModel = allCultrues.get(cultrueItem);

        setCurrentCultrue(cultrueModel);

        // 向左边移入动画
        cultrueRelicsScoll.setVisibility(View.GONE);
        cultrueRelicsScoll.setVisibility(View.VISIBLE);
        cultrueRelicsScoll.setAnimation(AnimationUtils.makeInAnimation(this, false));
    }

    /**
     * 权限的结果回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        AppToast.showShortText(mCctivity, "请打开相机权限否则无法操作");
    }
}

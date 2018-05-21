package com.zb.bilateral.activity.home_page;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.example.mycommon.util.StatusBarCompat;
import com.example.mycommon.view.GlideCircleTransform;
import com.example.mycommon.view.GlideRoundImage;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.Constants;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseActivityManager;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.brocast.MyMusicBrocast;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.ShowRoomListModel;
import com.zb.bilateral.model.ShowRoomModel;
import com.zb.bilateral.model.SongInfo;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.ExhibitionHallPresenter;
import com.zb.bilateral.mvp.ExhibitionHallView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.service.MuiscService;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.util.MyConstant;
import com.zb.bilateral.view.CommitDialog;
import com.zb.bilateral.view.NearHallDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 */
public class ExhibitionHallActivity extends BaseNewActivity<ExhibitionHallPresenter>
        implements ExhibitionHallView, EasyPermissions.PermissionCallbacks {
    private final static int NEAR_HALL = 11;
    private final static int REQUEST_CODE = 1000;
    private final static int REQUEST_QUESTION = 1001;
    private final int PERMISSIONS_CAMERA = 1;

    @Bind(R.id.museum_detail_open_img)
    ImageView mOpenImg;
    @Bind(R.id.cultrue_banner)
    Banner mBanner;
    @Bind(R.id.exhibition_content)
    TextView mContentText;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.exhibite_song_play)
    ImageView exhibiteSongPlay;
    @Bind(R.id.exhibite_song_pause)
    ImageView exhibiteSongPause;
    @Bind(R.id.seekBar)
    SeekBar mSeekBar;
    @Bind(R.id.song_start_time)
    TextView songStartTime;
    @Bind(R.id.song_end_time)
    TextView songEndTime;
    @Bind(R.id.exhibite_scroll)
    ScrollView mScrollView;
    @Bind(R.id.play_music_bar)
    ProgressBar playMusicBar;
    @Bind(R.id.exhibition_hall_answer)
    TextView exhibitionHallAnswer;

    private boolean isOpen = false;
    private String museum_id;//博物馆id
    private String hall_id;//展厅id
    //附近展厅数据
    private List<ShowRoomModel> nearbyShowroom;
    //音效广播
    private MyMusicBrocast music_brocast;
    //当前播放音频链接
    private String songStr;
    //当前播放进度
    private long currentprogress = 0;
    //当销毁下一页播放时为true
    private boolean isOnStop=false;

    private MyApplication myApplication;

    private ShowRoomModel showRoomModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exhibition_hall;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        museum_id = getIntent().getStringExtra("museum_id");
        hall_id = getIntent().getStringExtra("hall_id");

        myApplication = (MyApplication) getApplicationContext();

        //设置状态栏颜色
        StatusBarCompat.compat((Activity) mCctivity, 3);

        //进度条改变广播
        mSeekBar.setOnSeekBarChangeListener(seek_change);
    }

    /**
     * 注册音频改变广播
     */
    public void setMusicBrocast() {
        music_brocast = new MyMusicBrocast(handler);
        registerReceiver(music_brocast, Constants.getIntentFilter());
    }

    @Override
    protected void initData() {
        showroom();
        //获取附近博物馆可以直接播放下一首
        nearbyShowroom();
    }

    /**
     * 单个展厅导览
     */
    public void showroom() {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.showroom(museum_id, hall_id, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 附近展厅导览
     */
    public void nearbyShowroom() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.nearbyShowroom(hall_id);
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
    protected ExhibitionHallPresenter createPresenter() {
        return new ExhibitionHallPresenter(this, mCctivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMusicBrocast();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        //停止音频播放服务
        Intent intent = new Intent(ExhibitionHallActivity.this, MuiscService.class);
        stopService(intent);
    }

    @OnClick({R.id.top_left_rel, R.id.museum_detail_open_rel, R.id.exhibition_hall_answer, R.id.exhibite_hall, R.id.nearbyShowroom,
            R.id.exhibite_inSong_img, R.id.exhibite_nextSong_img, R.id.exhibite_song_play, R.id.exhibite_song_pause, R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                new CommitDialog((Activity) mCctivity, topCenterText, handler, Constants.SONG_CANCEL);
                break;
            case R.id.top_right_rel:
                //扫描进入文物
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] perms = {Manifest.permission.CAMERA};
                    if (!EasyPermissions.hasPermissions(mCctivity, perms)) {
                        EasyPermissions.requestPermissions(this, "APP需要打开相机权限", PERMISSIONS_CAMERA, perms);
                        return;
                    } else {
                        //调用具体方法
                        Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                        intent.putExtra("title", "ExhibitionHallActivity");
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                } else {
                    //调用具体方法
                    Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                    intent.putExtra("title", "ExhibitionHallActivity");
                    startActivityForResult(intent, REQUEST_CODE);
                }
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
            case R.id.exhibition_hall_answer://答题得分
                Intent intent = new Intent(mCctivity, AnswerQuestionActivity.class);
                intent.putExtra("museum_id", museum_id);
                startActivityForResult(intent, REQUEST_QUESTION);

                pause();
                break;
            case R.id.exhibite_hall:
                intent = new Intent(mCctivity, ExhibitionHallDetailActivity.class);
                intent.putExtra("hall_id", hall_id);
                intent.putExtra("hall_name", topCenterText.getText().toString());
                startActivity(intent);

                pause();
                break;
            case R.id.nearbyShowroom://附近展厅
                if (nearbyShowroom != null) {
                    if (nearbyShowroom.size() == 0) {
                        AppToast.makeToast(mCctivity, "暂无附近展厅");
                        return;
                    }

                    new NearHallDialog((Activity) mCctivity, mContentText, handler, nearbyShowroom);
                } else {
                    nearbyShowroom();
                }
                break;
            case R.id.exhibite_inSong_img://播放上一展厅
                inHall();

                //从新获取附近展厅排序
                nearbyShowroom();
                break;
            case R.id.exhibite_nextSong_img://播放下一展厅
                nextHall();

                //从新获取附近展厅排序
                nearbyShowroom();
                break;
            case R.id.exhibite_song_pause:
                pause();
                break;
            case R.id.exhibite_song_play:
                play();
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
        } else if (requestCode == REQUEST_QUESTION) {
            //从答题页面返回继续播放
            play();
        }
    }

    @Override
    public void SendResultSuccess(ShowRoomListModel showRoomListModel) {
        showRoomModel = showRoomListModel.getShowroom();

        //初始化数据
        mContentText.setText(showRoomListModel.getShowroom().getIntroduce());
        topCenterText.setText(showRoomListModel.getShowroom().getTitle());

        int seconds = showRoomListModel.getShowroom().getLs() % 60;
        songEndTime.setText(showRoomListModel.getShowroom().getLs() / 60 + ":" + (seconds < 10 ? "0" + seconds : seconds));

        //是否显示答题得分
        if (showRoomModel.getHasQuestion().equals("1")) {
            exhibitionHallAnswer.setVisibility(View.GONE);
        } else {
            exhibitionHallAnswer.setVisibility(View.VISIBLE);
        }

        /*RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mCctivity));

        Glide.with(mCctivity)
                .load(ApiStores.IMG_URL_TOP + showRoomListModel.getShowroom().getCover())
                .apply(options)
                .into(exhibition_img);*/

        if(showRoomListModel.getShowroom()!=null){
            setBanner(showRoomListModel.getShowroom().getImgList());
        }

        //显示主页面
        mScrollView.setVisibility(View.VISIBLE);
    }


    public void setBanner(final List<BannerModel> listBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP+listBanner.get(i).getPath());
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideCircleImageLoader());
        //设置图片集合
        mBanner.setImages(images);
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

    //附近展厅
    @Override
    public void nearbyShowroomSuccess(ShowRoomListModel showRoomListModel) {
        nearbyShowroom = showRoomListModel.getShowroomList();
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

        pause();
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
                case NEAR_HALL:
                    nearbyShowroom = null;
                    setShowRoom( (ShowRoomModel) msg.obj);
                    break;
                case 0://播放
                    exhibiteSongPause.setVisibility(View.VISIBLE);
                    exhibiteSongPlay.setVisibility(View.GONE);
                    break;
                case 1://暂停、停止
                    exhibiteSongPause.setVisibility(View.GONE);
                    exhibiteSongPlay.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    //进度条更新
                    if (myApplication.getPlaying() != null && songStr
                            .equals(myApplication.getPlaying().getSongPath())) {
                        long play_progress = myApplication.getPlaying().getPlayProgress();
                        long total_time = myApplication.getPlaying().getTotal_time();
                        //取消缓冲框
                        if (play_progress > 0) {
                            playMusicBar.setVisibility(View.INVISIBLE);
                        }

                        if (total_time != 0) {
                            double size = play_progress * 100 / total_time;

                            mSeekBar.setProgress((int) size);
                            songStartTime.setText(AppUtil.get_time((int) play_progress));
                            songEndTime.setText(AppUtil.get_time((int) total_time));
                        }
                    }
                    break;
                case 3:
                    //播放完成
                    exhibiteSongPlay.setVisibility(View.VISIBLE);
                    exhibiteSongPause.setVisibility(View.GONE);
                    break;
                case Constants.COMMIT_DIALOG:
                    finish();
                    break;
            }
        }
    };

    //播放拖动
    SeekBar.OnSeekBarChangeListener seek_change = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            currentprogress = myApplication.getPlaying().getPlayProgress();
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

        exhibiteSongPlay.setVisibility(View.GONE);
        exhibiteSongPause.setVisibility(View.VISIBLE);
    }

    //播放
    public void play() {
        //防止多次重复点击造成崩溃，所以控制只有在播放和暂停已经完成后才能继续点击
        if(TextUtils.isEmpty(showRoomModel.getVoice())){
            AppToast.makeToast(mCctivity,"暂无音频，无法播放");
            return;
        }
        if (myApplication.is_click()) {
            if (!MuiscService.isPlaying) {
                exhibiteSongPlay.setVisibility(View.GONE);
                exhibiteSongPause.setVisibility(View.VISIBLE);

                //重新赋值播放内容
                SongInfo songInfo = new SongInfo();
                songInfo.setId("" + showRoomModel.getId());
                songInfo.setPlayProgress(currentprogress);
                songInfo.setSongPath(ApiStores.IMG_URL_TOP + showRoomModel.getVoice());
                myApplication.setPlaying(songInfo);
                myApplication.setIs_click(false);

                songStr = ApiStores.IMG_URL_TOP + showRoomModel.getVoice();
                playMusicBar.setVisibility(View.VISIBLE);

                //发送播放广播
                if (!MuiscService.isServiceRunning) {
                    Intent intent = new Intent(mCctivity, MuiscService.class);
                    startService(intent);
                }else if(isOnStop){
                    Intent play = new Intent(MyConstant.PAUSE_TO_PLAY);
                    sendBroadcast(play);
                } else {
                    Intent play = new Intent(MyConstant.PLAY);
                    sendBroadcast(play);
                }
            } else {
                if (mSeekBar.getProgress() == 100) {
                    //当播放完成后重新播放
                    playTo(0);
                } else {
                    //否则从暂停处播放
                    playTo(mSeekBar.getProgress());
                }
            }
        }
    }

    //暂停
    public void pause() {
        LogUtil.i("tag","MuiscService.isPlaying-->"+MuiscService.isPlaying);
        if (MuiscService.isPlaying) {
            exhibiteSongPlay.setVisibility(View.VISIBLE);
            exhibiteSongPause.setVisibility(View.GONE);

            Intent pause_boradcast = new Intent(MyConstant.PAUSE);
            sendBroadcast(pause_boradcast);
        }
    }

    //暂停
    public void playOther() {
        if (MuiscService.isPlaying) {
            exhibiteSongPlay.setVisibility(View.VISIBLE);
            exhibiteSongPause.setVisibility(View.GONE);

            Intent pause_boradcast = new Intent(MyConstant.PLAY_OTHER);
            sendBroadcast(pause_boradcast);
        }
    }

    /**
     * 显示上一展厅的数据
     */
    public void inHall() {
        if (nearbyShowroom == null|| nearbyShowroom.size() == 0) {
            AppToast.makeToast(mCctivity, "当前暂无附近展厅");
            return;
        }

        int index=getIndex();
        if(index==0){
            showRoomModel = nearbyShowroom.get(nearbyShowroom.size()-1);
        }else{
            showRoomModel = nearbyShowroom.get(index-1);
        }

        setShowRoom(showRoomModel);

        // 向左边移入动画
        mScrollView.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        mScrollView.setAnimation(AnimationUtils.makeInAnimation(this, false));
    }

    /**
     * 显示下一展厅的数据
     */
    public void nextHall() {
        if (nearbyShowroom == null || nearbyShowroom.size() == 0) {
            AppToast.makeToast(mCctivity, "当前暂无附近展厅");
            return;
        }

        int index=getIndex();
        if(index==nearbyShowroom.size()-1){
            showRoomModel = nearbyShowroom.get(0);
        }else{
            showRoomModel = nearbyShowroom.get(index+1);
        }

        setShowRoom(showRoomModel);

        mScrollView.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        // 向右边移入
        mScrollView.setAnimation(AnimationUtils.makeInAnimation(this, true));
    }

    public int getIndex(){
        for (int i = 0; i <nearbyShowroom.size() ; i++) {
            if(nearbyShowroom.get(i).getId().equals(showRoomModel.getId())){
                return i;
            }
        }

        return 0;
    }

    /**
     * 设置展厅页面数据
     *
     * @param showRoom 展厅详情model
     */
    public void setShowRoom(ShowRoomModel showRoom) {
        showRoomModel = showRoom;
        //起始先暂停
        playOther();

        MuiscService.isPlaying = false;
        mSeekBar.setProgress(0);
        currentprogress = 0;
        songStartTime.setText("00:00");

        //play();

        hall_id = showRoom.getId();

        mContentText.setText(showRoom.getIntroduce());
        topCenterText.setText(showRoom.getTitle());

        if(showRoom.getImgList()!=null){
            setBanner(showRoom.getImgList());
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        WeChatCaptureActivity.launch((Activity) mCctivity, "ExhibitionHallDetailActivity", REQUEST_CODE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        AppToast.showShortText(mCctivity, "请打开相机权限否则无法操作");
    }

    @Override
    public void onBackPressed() {
        new CommitDialog((Activity) mCctivity, topCenterText,
                handler, Constants.SONG_CANCEL);
        return;
    }
}

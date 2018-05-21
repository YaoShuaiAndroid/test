package com.zb.bilateral.activity.home_page;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cat.cc.taglibrary.view.ImageDotLayout;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.BitmapUtils;
import com.example.mycommon.util.LogUtil;
import com.example.mycommon.util.StatusBarCompat;
import com.example.mycommon.view.GlideCircleTransform;
import com.example.mycommon.view.GlideRoundImage;
import com.zb.bilateral.Constants;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.brocast.MyMusicBrocast;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.ExhibitionListModel;
import com.zb.bilateral.model.ExhibitionModel;
import com.zb.bilateral.model.ShowRoomListModel;
import com.zb.bilateral.model.ShowRoomModel;
import com.zb.bilateral.model.SongInfo;
import com.zb.bilateral.mvp.ExhibitionDetailView;
import com.zb.bilateral.mvp.ExhibitionHallDetailPresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.service.MuiscService;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.MyConstant;
import com.zb.bilateral.view.HallNoDialog;
import com.zb.bilateral.view.NearCultrueDialog;
import com.zb.bilateral.view.SetMoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/*
* 展厅详情
* */
public class ExhibitionHallDetailActivity extends BaseNewActivity<ExhibitionHallDetailPresenter> implements ExhibitionDetailView {
    private final static int NEAR_HALL = 11;
    private final static int SET_CULTRUE = 12;
    private final static int REQUEST_CODE = 1000;
    private final static int EDIT_NO= 4;

    @Bind(R.id.gallery_photo)
    ImageDotLayout imageDotLayout;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.cultrue_img)
    ImageView cultrueImg;
    @Bind(R.id.cultrue_name)
    TextView cultrueName;
    @Bind(R.id.cultrue_song_pause)
    ImageView cultrueSongPause;
    @Bind(R.id.cultrue_song_play)
    ImageView cultrueSongPlay;
    @Bind(R.id.cultrue_seekbar)
    SeekBar mSeekBar;
    @Bind(R.id.song_end_time)
    TextView songEndTime;
    @Bind(R.id.song_start_time)
    TextView songStartTime;
    @Bind(R.id.exhibite_hall_detial_rel)
    RelativeLayout exhibiteHallDetialRel;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.exhibition_rel)
    RelativeLayout exhibitionRel;

    //音效广播
    private MyMusicBrocast music_brocast;
    //当前播放音频链接
    private String songStr="";
     //当前播放进度
    private long currentprogress=0;
    //当前播放的文物item
    private int cultrueItem = 0;
    //当前文物id
    private String cultrueId;

    private MyApplication myApplication;

    private CultrueListModel cultrueListModels = null;
    //展厅id
    private String hall_id;
    //展厅名称
    private String hall_name;
    //当前播放的model
    private CultrueModel cultrueModels;

    private ExhibitionListModel exhibitionListModels;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exhibition_hall_detail;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        hall_id = getIntent().getStringExtra("hall_id");
        hall_name = getIntent().getStringExtra("hall_name");

        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topRightImg.setBackgroundResource(R.mipmap.enery_more);
        topCenterText.setText(hall_name);

        myApplication = (MyApplication) getApplicationContext();

        //进度条改变广播
        mSeekBar.setOnSeekBarChangeListener(seek_change);
    }

    @Override
    protected void initData() {
        //首次进入先暂停
        pause();

        antiquesListForSmall();

        nearbyAntiques(hall_id);
    }

    /**
     * 注册音频改变广播
     */
    public void setMusicBrocast() {
        music_brocast = new MyMusicBrocast(handler);
        registerReceiver(music_brocast, Constants.getIntentFilter());
    }

    @Override
    protected ExhibitionHallDetailPresenter createPresenter() {
        return new ExhibitionHallDetailPresenter(this, mCctivity);
    }

    /**
     * 展厅文物
     */
    public void nearbyAntiques(String cultrueId) {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.nearbyAllAntiques(cultrueId, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 展厅文物标记
     */
    public void antiquesListForSmall() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.antiquesListForSmall(hall_id);
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.top_left_img, R.id.exhibite_muse, R.id.cultrue_next_rel, R.id.cultrue_in_rel,
            R.id.cultrue_song_play, R.id.cultrue_song_pause, R.id.cultrue_img, R.id.top_right_rel,
            R.id.museum_scan, R.id.museum_no})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.exhibite_muse:
                //附近文物
                if (cultrueListModels == null) {
                    nearbyAntiques(cultrueId);
                } else {
                    new NearCultrueDialog((Activity) mCctivity, exhibitionRel,
                            handler, nearCultrue());
                }
                break;
            case R.id.cultrue_next_rel:
                //播放下一曲文物
                nextHall();
                break;
            case R.id.cultrue_in_rel:
                //播放上一曲文物
                inHall();
                break;
            case R.id.cultrue_song_pause:
                pause();
                break;
            case R.id.cultrue_song_play:
                play();
                break;
            case R.id.cultrue_img:
                //进入文物
                Intent intent = new Intent(mCctivity, CulturalRelicsActivity.class);
                intent.putExtra("cultrue_id", cultrueId);
                startActivity(intent);
                break;
            case R.id.top_right_rel:
                //更多
                new SetMoreDialog((Activity) mCctivity, topCenterText, handler,false);
                break;
            case R.id.museum_scan:
                //扫描
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(mCctivity, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mCctivity, new String[]{Manifest.permission.CAMERA}, 15);
                        return;
                    } else {
                        WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                    }
                } else {
                    WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                }
                break;
            case R.id.museum_no:
                new HallNoDialog((Activity) mCctivity, topCenterText, handler);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMusicBrocast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
        //停止广播
        unregisterReceiver(music_brocast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将音频回位到0
        SongInfo s = new SongInfo();
        s.setPlayProgress(0);
        myApplication.setPlaying(s);
    }

    @Override
    public void SendResultSuccess(CultrueListModel cultrueListModel) {
        cultrueListModels = cultrueListModel;
        if (cultrueListModel.getAntiquesList().size() > 0) {
            cultrueId=cultrueListModels.getAntiquesList().get(0).getId();
            setCultrueData(cultrueListModels.getAntiquesList().get(0));

            exhibiteHallDetialRel.setVisibility(View.VISIBLE);

            int seconds=cultrueListModel.getAntiquesList().get(0).getLs()%60;
            songEndTime.setText(cultrueListModel.getAntiquesList().get(0).getLs()/60+":"+(seconds<10?"0"+seconds:seconds));
        }else{
            AppToast.makeToast(mCctivity,"当前展厅暂无文物");
        }
    }

    @Override
    public void SubmitResultSuccess(ExhibitionListModel exhibitionListModel) {
        exhibitionListModels = exhibitionListModel;
        setFloor(exhibitionListModels);
    }

    @Override
    public void SubmitSuccess(CultrueDetailModel cultrueDetailModel) {
        String id=cultrueDetailModel.getAntiques().getId();
        if(TextUtils.isEmpty(id)){
            AppToast.makeToast(mCctivity,"数据异常");
            return;
        }
        Intent intent = new Intent(mCctivity, CulturalRelicsActivity.class);
        intent.putExtra("cultrue_id", "" +id);
        startActivity(intent);
    }


    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    public void setFloor(final ExhibitionListModel exhibitionListModel) {
        Glide.with(mCctivity)
                .asBitmap()
                .load(ApiStores.IMG_URL_TOP + exhibitionListModel.getPlan())
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        //保存图片的原始宽高，防止后面图片拼接是变化了宽高而找不到标记点
                        exhibitionListModel.setImageHeight(bitmap.getHeight());
                        exhibitionListModel.setImageWidth(bitmap.getWidth());

                        setInit(bitmap);
                    }
                });
    }

    public void setInit(Bitmap bitmap) {
        //设置背景图片
        imageDotLayout.setImage(bitmap,1);

        initIcon();

        //监听图片是否加载完成
        imageDotLayout.setOnLayoutReadyListener(new ImageDotLayout.OnLayoutReadyListener() {
            @Override
            public void onLayoutReady() {
            }
        });

        imageDotLayout.setOnIconClickListener(new ImageDotLayout.OnIconClickListener() {
            @Override
            public void onIconClick(View v) {
                ImageDotLayout.IconBean bean = (ImageDotLayout.IconBean) v.getTag();
                if (bean != null) {
                    setCultrueLabel(bean.id);
                }
            }
        });
    }

    List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();
    /**
     * 添加标记点
     */
    private void initIcon() {
        float oneWidth = 0;
        float oneHeigth = 0;
        int item = 0;

        for (int i = 0; i < exhibitionListModels.getAntiquesList().size(); i++) {
            ExhibitionModel showRoomModel = exhibitionListModels.getAntiquesList().get(i);
            oneWidth = showRoomModel.getX() / (float) exhibitionListModels.getImageWidth();
            oneHeigth = showRoomModel.getY() / (float) exhibitionListModels.getImageHeight();
            String name = showRoomModel.getNo();

            if(name.length()>6){
                name=name.substring(0,5);
            }

            ImageDotLayout.IconBean bean = new ImageDotLayout
                    .IconBean(item, showRoomModel.getId(), name, oneWidth, oneHeigth, null);
            item++;
            iconBeanList.add(bean);
        }

        imageDotLayout.addIcons(iconBeanList);
    }

    public void setCultrueLabel(String id) {
        cultrueId = id;

        if (cultrueListModels != null) {
            for (int i = 0; i < cultrueListModels.getAntiquesList().size(); i++) {
                if (cultrueListModels.getAntiquesList().get(i).getId().equals(cultrueId)) {
                    cultrueItem = i;

                    setCultrueData(cultrueListModels.getAntiquesList().get(i));
                }
            }
        } else {
            //没有则重新获取
            nearbyAntiques(id);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NEAR_HALL:
                    CultrueModel cultrueModel = (CultrueModel) msg.obj;

                    Intent intent = new Intent(mCctivity, CulturalRelicsActivity.class);
                    intent.putExtra("cultrue_id", cultrueModel.getId());
                    intent.putExtra("hall_id", hall_id);
                    intent.putExtra("cultrue_name", cultrueModel.getName());
                    startActivity(intent);
                    break;
                case EDIT_NO:
                    antiques1((String) msg.obj);
                    break;
                case SET_CULTRUE:
                    CultrueModel cultrue = (CultrueModel) msg.obj;
                    setCultrueData(cultrue);
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
                        long play_progress = myApplication.getPlaying().getPlayProgress();
                        long total_time = myApplication.getPlaying().getTotal_time();

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
                    cultrueSongPlay.setVisibility(View.VISIBLE);
                    cultrueSongPause.setVisibility(View.GONE);
                    break;
                case Constants.MAIN_ACTIVITY:
                    //返回首页
                    intent = new Intent(mCctivity, MainActivity.class);
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
            }
        }
    };

    /**
     * 设置底部文物初始化数据
     *
     * @param cultrueModel
     */
    public void setCultrueData(CultrueModel cultrueModel) {
        if(cultrueModels!=null){
            cultrueModels=cultrueModel;
            //起始先暂停
            playOther();

            MuiscService.isPlaying = false;
            mSeekBar.setProgress(0);
            currentprogress = 0;
            songStartTime.setText("00:00");

            //play();
        }else{
            cultrueModels=cultrueModel;
        }

        cultrueName.setText(cultrueModel.getName());

        String path = "";
        if (cultrueModel.getImgList().size() > 0) {
            path = cultrueModel.getImgList().get(0).getPath();
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideCircleTransform(mCctivity));

        Glide.with(mCctivity)
                .load(ApiStores.IMG_URL_TOP + path)
                .apply(options)
                .into(cultrueImg);

        //播放音效
        if (cultrueModel.getVoice() == null) {
            AppToast.makeToast(mCctivity, "当前文物暂无音频");
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

                songStr = ApiStores.IMG_URL_TOP + cultrueModels.getVoice();

                //发送播放广播
                if (!MuiscService.isServiceRunning) {
                    Intent intent = new Intent(mCctivity, MuiscService.class);
                    startService(intent);
                } else {
                    Intent play = new Intent(MyConstant.PLAY);
                    sendBroadcast(play);
                }
            } else {
                playTo(0);
            }
        }
    }


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

    /**
     * 显示下一文物的数据
     */
    public void nextHall() {
        if (cultrueListModels == null ||cultrueListModels.getAntiquesList().size() == 0) {
            AppToast.makeToast(mCctivity, "当前暂无附近文物");
            return;
        }

        int cultrueSize=cultrueListModels.getAntiquesList().size() - 1;
        if ( cultrueSize> cultrueItem) {
            cultrueItem++;
        } else {
            cultrueItem = 0;
        }

        setCultrueData(cultrueListModels
                .getAntiquesList().get(cultrueItem));
    }

    /**
     * 显示上一文物的数据
     */
    public void inHall() {
        if (cultrueListModels == null || cultrueListModels.getAntiquesList().size() == 0) {
            AppToast.makeToast(mCctivity, "当前暂无附近文物");
            return;
        }


        if (cultrueItem != 0) {
            cultrueItem--;
        } else {
            cultrueItem = cultrueListModels.getAntiquesList().size() - 1;
        }

        setCultrueData(cultrueListModels.getAntiquesList().get(cultrueItem));
    }

    /**
     * 附近文物
     *
     * @return
     */
    public List<CultrueModel> nearCultrue() {
        List<CultrueModel> cultrueModels = new ArrayList<>();
        for (int i = 0; i < cultrueListModels.getAntiquesList().size(); i++) {
            if (i != cultrueItem) {
                cultrueModels.add(cultrueListModels.getAntiquesList().get(i));
            }
        }

        return cultrueModels;
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
                antiques1((String) data.getStringExtra("result_string"));
            }
        }
    }

    //请求权限返回
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 5:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                } else {
                    AppToast.makeToast(mCctivity, "很遗憾你把文件权限禁用了。请务必开启文件权限享受我们提供的服务吧");
                }
                break;
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                } else {
                    AppToast.makeToast(mCctivity, "很遗憾你把文件权限禁用了。请务必开启文件权限享受我们提供的服务吧");
                }
                break;
            case 15:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WeChatCaptureActivity.launch((Activity) mCctivity,"ExhibitionHallDetailActivity",REQUEST_CODE);
                } else {
                    AppToast.makeToast(mCctivity, "很遗憾你把文件权限禁用了。请务必开启文件权限享受我们提供的服务吧");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

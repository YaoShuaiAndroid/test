package com.zb.bilateral.activity.home_page.same_relics;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.SPUtils;
import com.zb.bilateral.Constants;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.GuideActivity;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.home_page.CultrueCommentActivity;
import com.zb.bilateral.activity.home_page.CulturalRelicsActivity;
import com.zb.bilateral.activity.home_page.ExhibitionHallDetailActivity;
import com.zb.bilateral.activity.home_page.GalleryTourActivity;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.activity.home_page.WeChatCaptureActivity;
import com.zb.bilateral.activity.person.collect.ActivityFragment;
import com.zb.bilateral.activity.person.collect.MuseumFragment;
import com.zb.bilateral.activity.person.collect.MyCollectActivity;
import com.zb.bilateral.activity.person.collect.MyCollectAdapter;
import com.zb.bilateral.activity.person.collect.MyCollectFragment;
import com.zb.bilateral.activity.person.collect.MyCultrueFragment;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.config.MyCollectDataType;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.SongInfo;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.CultrueRelicsPresenter;
import com.zb.bilateral.mvp.CultrueRelicsView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.service.MuiscService;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.MyConstant;
import com.zb.bilateral.view.SetMoreDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SameRelicsActivity extends BaseNewActivity<CultrueRelicsPresenter> implements CultrueRelicsView,EasyPermissions.PermissionCallbacks {
    @Bind(R.id.same_relics_view_pager)
    ViewPager viewPager;
    @Bind(R.id.top_center_text)
    TextView mTitleText;

    private final static int REQUEST_CODE = 1000;

    private SameRelicsAdapter sameRelicsAdapter;
    private int item;//当前选中第几个

    private MyApplication myApplication;

    private List<CultrueModel> cultrueModels;

    private String cultrue_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_same_relics;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        myApplication= (MyApplication) getApplicationContext();

        cultrueModels=getIntent().getParcelableArrayListExtra("cultrue");
        item=getIntent().getIntExtra("item",0);
        if(cultrueModels.size()>0){
            cultrue_id=cultrueModels.get(0).getId();
            mTitleText.setText(cultrueModels.get(0).getName());
            playSong(cultrueModels.get(0));
        }

        initData();
    }


    public void playSong(CultrueModel cultrueModel){
        //设置音频播放
        SongInfo s = new SongInfo();
        s.setPlayProgress(0);
        s.setId("" + cultrueModel.getId());
        s.setSongPath(ApiStores.IMG_URL_TOP + cultrueModel.getVoice());
        s.setSongName("" + cultrueModel.getName());
        myApplication.setPlaying(s);
        //发送播放广播
        boolean isrun = MuiscService.isServiceRunning;
        if (!isrun) {
            Intent intent = new Intent(mCctivity, MuiscService.class);
            startService(intent);
        }else{
            Intent play = new Intent(MyConstant.PLAY);
            sendBroadcast(play);
        }
        //mMusicProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        sameRelicsAdapter = new SameRelicsAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(sameRelicsAdapter.getCount() - 1);
        viewPager.setAdapter(sameRelicsAdapter);
        viewPager.setCurrentItem(item);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleText.setText(cultrueModels.get(position).getName());
                cultrue_id=cultrueModels.get(position).getId();

                playSong(cultrueModels.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    protected CultrueRelicsPresenter createPresenter() {
        return new CultrueRelicsPresenter(this,mCctivity);
    }

    @OnClick({R.id.top_left_rel,R.id.same_relicy_right,R.id.same_relicy_left, R.id.cultrue_scan_rel,R.id.cultrue_relics_more_rel})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                finish();
                break;
            case R.id.same_relicy_right:
                if(viewPager.getCurrentItem()<cultrueModels.size()){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
                break;
            case R.id.same_relicy_left:
                if(viewPager.getCurrentItem()!=0){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                }
                break;
            case R.id.cultrue_scan_rel://扫描文物
                //扫描进入文物
                if (Build.VERSION.SDK_INT >= 23){
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(mCctivity, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mCctivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
                        return;
                    } else {
                        //调用具体方法
                        Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                        intent.putExtra("title","SameRelicsActivity");
                        startActivityForResult(intent,REQUEST_CODE);
                    }
                } else {
                    //调用具体方法
                    Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                    intent.putExtra("title","SameRelicsActivity");
                    startActivityForResult(intent,REQUEST_CODE);
                }
                break;
            case R.id.cultrue_relics_more_rel:
                //更多
                new SetMoreDialog((Activity) mCctivity,mTitleText,handler,false);
                break;
        }
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

    //暂停
    public void pause() {
        if (MuiscService.isPlaying) {
            Intent pause_boradcast = new Intent(MyConstant.PAUSE);
            sendBroadcast(pause_boradcast);
        }
    }

    @Override
    public void SendResultSuccess(CultrueDetailModel cultrueDetailModel) {

    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
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
    public void nearResultSuccess(CultrueListModel cultrueListModel) {

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
        Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
        intent.putExtra("title","SameRelicsActivity");
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        AppToast.showShortText(mCctivity,"请打开相机权限否则无法操作");
    }


    class SameRelicsAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;

        public SameRelicsAdapter(FragmentManager fm) {
            super(fm);
            mFragmentList = new ArrayList<>();
            for (int i = 0; i <cultrueModels.size() ; i++) {
                mFragmentList.add(SameRelicsFragment.newInstance(cultrueModels.get(i)));
            }
        }

        @Override
        public int getCount() {
            return cultrueModels.size();
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MAIN_ACTIVITY:
                    //返回首页
                    Intent intent=new Intent(mCctivity, MainActivity.class);
                    startActivity(intent);
                    break;
                case Constants.MUSEUM_ACTIVITY:
                    //返回博物馆
                     intent=new Intent(mCctivity, MuseumDetailActivity.class);
                    intent.putExtra("item",0);
                    startActivity(intent);
                    break;
                case Constants.SHOW_ACTIVITY:
                    //返回展厅导览
                     intent=new Intent(mCctivity, GalleryTourActivity.class);
                    startActivity(intent);
                    break;
                case Constants.PERSON_ACTIVITY:
                    //返回个人中心
                    intent=new Intent(mCctivity, MainActivity.class);
                    intent.putExtra("item",3);
                    startActivity(intent);
                    break;
            }
        }
    };
}
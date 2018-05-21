package com.zb.bilateral.activity.home_page;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cat.cc.taglibrary.util.FileUtil;
import com.cat.cc.taglibrary.view.ImageDotLayout;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.BitmapUtils;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.util.LogUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.FloorModel;
import com.zb.bilateral.model.ShowRoomListModel;
import com.zb.bilateral.model.ShowRoomModel;
import com.zb.bilateral.mvp.GalleryTourPresenter;
import com.zb.bilateral.mvp.GalleryTourView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.GalleryTourDialog;
import com.zb.bilateral.view.HallNoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
/*
* 展厅列表
* */

public class GalleryTourActivity extends BaseNewActivity<GalleryTourPresenter> implements GalleryTourView {
    @Bind(R.id.gallery_center_text)
    TextView gallertCenterText;
    @Bind(R.id.gallery_center_img)
    ImageView galleryCenterImg;
    @Bind(R.id.gallery_photo)
    ImageDotLayout imageDotLayout;
    @Bind(R.id.gallery_tour_scroll)
    ScrollView mScrollView;

    private final static int CHANGE_DOWN = 1;
    private final static int CHANGE_FLOOR = 2;
    private final static int CLICK_HALL = 3;
    private final static int EDIT_NO = 4;
    private final static int REQUEST_CODE = 1000;

    private boolean isDown = true;//箭头方向
    private String museum_id, museum_name;

    List<FloorModel> floorModels;
    List<ShowRoomModel> showRoomModels = new ArrayList<>();

    private GalleryTourDialog galleryTourDialog = null;

    private int imageSize=0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery_tour;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        museum_id = getIntent().getStringExtra("museum_id");
        museum_name = getIntent().getStringExtra("museum_name");

        gallertCenterText.setText(museum_name);
    }

    @Override
    protected void initData() {
        showroomsForSmall();

        voice();
    }

    /**
     * 显示博物馆所有楼层、展厅（讲解）
     */
    public void voice() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.voice(museum_id);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 获取博物馆展厅图片和标记
     */
    public void showroomsForSmall() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.showroomsForSmall(museum_id);
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


    @OnClick({R.id.gallery_cancel, R.id.gallery_center_lin, R.id.museum_scan, R.id.museum_no})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_cancel:
                finish();
                break;
            case R.id.gallery_center_lin:
                if (floorModels == null) {
                    voice();
                    return;
                }

                if (floorModels.size() == 0) {
                    AppToast.makeToast(mCctivity, "暂无展厅");
                    return;
                }

                if(galleryTourDialog==null){
                    galleryTourDialog = new GalleryTourDialog((Activity) mCctivity, gallertCenterText,
                            handler, floorModels, showRoomModels);
                }else {
                    galleryTourDialog.show(gallertCenterText);
                }

                galleryCenterImg.setBackgroundResource(R.mipmap.white_arrow_up);
                break;
            case R.id.museum_scan://扫描
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(mCctivity, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mCctivity, new String[]{Manifest.permission.CAMERA}, 15);
                        return;
                    } else {
                        //调用具体方法
                        Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                        intent.putExtra("title","GalleryTourActivity");
                        startActivityForResult(intent,REQUEST_CODE);
                    }
                } else {
                    //调用具体方法
                    Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                    intent.putExtra("title","GalleryTourActivity");
                    startActivityForResult(intent,REQUEST_CODE);
                }
                break;
            case R.id.museum_no:
                new HallNoDialog((Activity) mCctivity, gallertCenterText, handler);
                break;
        }
    }

    @Override
    protected GalleryTourPresenter createPresenter() {
        return new GalleryTourPresenter(this, mCctivity);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGE_DOWN:
                    galleryCenterImg.setBackgroundResource(R.mipmap.white_arrow_down);
                    break;
                case CHANGE_FLOOR:
                    int position = msg.arg1;
                    //修改点击的数据
                    setData(position);
                    //更新弹窗
                    galleryTourDialog.notifyAdapter(floorModels, showRoomModels);
                    break;
                case CLICK_HALL://点击展厅
                    String hall_id = (String) msg.obj;

                    if (TextUtils.isEmpty(hall_id)) {
                        AppToast.makeToast(mCctivity, "数据异常");
                        return;
                    }

                    Intent intent = new Intent(mCctivity, ExhibitionHallActivity.class);
                    intent.putExtra("museum_id", museum_id);
                    intent.putExtra("hall_id", hall_id);
                    startActivity(intent);
                    break;
                case EDIT_NO://进入文物详情
                    antiques1((String) msg.obj);
                    break;
            }
        }
    };

    public void setData(int position) {
        for (int i = 0; i < floorModels.size(); i++) {
            if (i == position) {
                floorModels.get(i).setIs_select(true);
                showRoomModels = floorList.getFloorList().get(i).getShowroomList();
            } else {
                floorModels.get(i).setIs_select(false);
            }
        }
    }

    FloorListModel floorList;
    @Override
    public void SendResultSuccess(FloorListModel floorListModel) {
        floorList = floorListModel;

        floorModels = floorListModel.getFloorList();
        if (floorListModel != null && floorListModel.getFloorList().size() > 0) {
            floorModels.get(0).setIs_select(true);

            showRoomModels.addAll(floorListModel.getFloorList().get(0).getShowroomList());
        }
    }


    List<ShowRoomListModel> showRoomListModels;

    @Override
    public void SendFloorSuccess(FloorModel floorModel) {
        showRoomListModels = floorModel.getFloorList();

        setFloor(showRoomListModels);
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

    private List<Bitmap> bitmaps = new ArrayList<>();//图片总地址

    public void setFloor(final List<ShowRoomListModel> showRoomListModels) {
        for (int i = 0; i < showRoomListModels.size(); i++) {
            bitmaps.add(null);
            final int finalI = i;
            Glide.with(mCctivity)
                    .asBitmap()
                    .load(ApiStores.IMG_URL_TOP + showRoomListModels.get(i).getPlan())
                    .into(new SimpleTarget<Bitmap>(1080, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            imageSize++;
                            //保存图片的原始宽高，防止后面图片拼接是变化了宽高而找不到标记点
                            showRoomListModels.get(finalI).setImageHeight(bitmap.getHeight());
                            showRoomListModels.get(finalI).setImageWidth(bitmap.getWidth());
                            //将图片放大或缩小到屏幕的宽度
                           //bitmap=BitmapUtils.compressScale(bitmap,500);
                            //因为图片因为大小有先后所以也需要按顺序放置
                            bitmaps.set(finalI,bitmap);

                            showRoomListModels.get(finalI).setImageFullHeight(bitmap.getHeight());
                            showRoomListModels.get(finalI).setImageFullWidth(bitmap.getWidth());

                            //当为最后一次循环获取图片后拼接图片
                            if (bitmaps != null && bitmaps.size() > 0 && imageSize == showRoomListModels.size()) {
                                newBitmap(bitmaps);
                            }
                        }
                    });
        }
    }

    private Bitmap bitmap;
    /**
     * 拼接图片
     *
     * @param bitmaps 图片数组
     * @return 返回拼接后的Bitmap
     */
    private void newBitmap(List<Bitmap> bitmaps) {
        int width = bitmaps.get(0).getWidth();
        int height =0;
        int currentHeight=0;

        //获取总高度
        for (int i = 0; i < bitmaps.size(); i++) {
            height = height + bitmaps.get(i).getHeight()+10;
        }
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于多张图片高度总和

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Log.i("tag",width+";"+height);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        //一张纸拼接图片
        for (int i = 0; i < bitmaps.size(); i++) {
            if (i == 0) {
                currentHeight=bitmaps.get(i).getHeight()+10;
                canvas.drawBitmap(bitmaps.get(i), 0, 0, null);
            } else {
                //设置拼接的位置坐标
                canvas.drawBitmap(bitmaps.get(i), 0, currentHeight, null);
                currentHeight=currentHeight+bitmaps.get(i).getHeight()+10;
            }
        }
        //设置背景图片
        imageDotLayout.setImage(bitmap,2);

        initIcon(height);

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

                Intent intent = new Intent(mCctivity, ExhibitionHallActivity.class);
                intent.putExtra("museum_id", museum_id);
                intent.putExtra("hall_id", bean.id);
                startActivity(intent);

                //Toast.makeText(mCctivity, "位置=" + bean.id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();
    /**
     * 添加标记点
     */
    private void initIcon(int height) {
        float oneWidth = 0;
        float oneHeigth = 0;
        int imageHeight=0;
        int item=0;

        for (int i = 0; i < showRoomListModels.size(); i++) {
            for (int j = 0; j <showRoomListModels.get(i).getShowroomList().size(); j++) {
                ShowRoomModel showRoomModel=showRoomListModels.get(i).getShowroomList().get(j);
                oneWidth = showRoomModel.getX() / (float)showRoomListModels.get(i).getImageWidth();
                oneHeigth = (showRoomModel.getY()*showRoomListModels.get(i).getImageFullHeight()/
                        showRoomListModels.get(i).getImageHeight()+imageHeight)/(float)height;
                String name=showRoomModel.getName();
                if(name.length()>4){
                    name=name.substring(0,4)+"..";
                }

                ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(item,showRoomModel.getId(),name, oneWidth, oneHeigth, null);
                item++;
                iconBeanList.add(bean);
            }
            //天假楼层
            ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(-1,"-1",showRoomListModels.get(i).getNo()+"F",0.05f, (float)(imageHeight+30)/(float)height, null);
            iconBeanList.add(bean);

            imageHeight=imageHeight+showRoomListModels.get(i).getImageFullHeight()+10;
        }

        imageDotLayout.addIcons(iconBeanList);
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

                antiques1(result);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
    }

    //请求权限返回
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 5:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                    intent.putExtra("title","GalleryTourActivity");
                    startActivityForResult(intent,REQUEST_CODE);
                } else {
                    Toast.makeText(mCctivity, "很遗憾你把文件权限禁用了。请务必开启文件权限享受我们提供的服务吧", Toast.LENGTH_LONG).show();
                }
                break;
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                    intent.putExtra("title","GalleryTourActivity");
                    startActivityForResult(intent,REQUEST_CODE);
                } else {
                    Toast.makeText(mCctivity, "很遗憾你把文件权限禁用了。请务必开启文件权限享受我们提供的服务吧", Toast.LENGTH_LONG).show();
                }
                break;
            case 15:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(mCctivity, WeChatCaptureActivity.class);
                    intent.putExtra("title","GalleryTourActivity");
                    startActivityForResult(intent,REQUEST_CODE);
                } else {
                    Toast.makeText(mCctivity, "很遗憾你把文件权限禁用了。请务必开启文件权限享受我们提供的服务吧", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

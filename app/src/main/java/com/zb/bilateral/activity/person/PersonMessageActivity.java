package com.zb.bilateral.activity.person;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.BitmapUtils;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.view.GlideCircleTransform;
import com.example.mycommon.view.GlideRoundImage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zb.bilateral.Constants;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.SelectImgDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class PersonMessageActivity extends BaseNewActivity<PublicPresenter<CommitModel>> implements PublicView<CommitModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.person_name)
    TextView person_name;
    @Bind(R.id.person_phone)
    TextView person_phone;
    @Bind(R.id.person_img)
    ImageView person_img;
    @Bind(R.id.person_message_lin)
    LinearLayout personMessageLin;

    private PersonModel personModel = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_message;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("个人资料");

        personModel = (PersonModel) AppUtil.load(FileUtils.configPath(mCctivity));
        if (personModel != null) {
            person_name.setText(personModel.getName());
            person_phone.setText(personModel.getPhone());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.no_img)
                    .transform(new GlideCircleTransform(mCctivity));

            Glide.with(mCctivity)
                    .load(personModel.getPhoto().contains("http")?""+personModel.getPhoto():
                            ApiStores.IMG_URL_TOP + personModel.getPhoto())
                    .apply(options)
                    .into(person_img);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.top_left_img, R.id.edit_message_name_rel, R.id.person_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.edit_message_name_rel://修改名称
                if (personModel != null) {
                    Intent intent = new Intent(mCctivity, EditNameActivity.class);
                    intent.putExtra("name", personModel.getName());
                    startActivityForResult(intent, 1000);
                }
                break;
            case R.id.person_img:
                clickHead();
                break;
        }
    }


    public void clickHead() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create((Activity) mCctivity)
                .openGallery(ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this, mCctivity);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                if (resultCode == 100) {
                    String name = data.getStringExtra("name");
                    if (!TextUtils.isEmpty(name)) {
                        personModel.setName(name);
                        person_name.setText(name);

                        AppUtil.save(personModel, FileUtils.configPath(mCctivity));
                    }
                }
                break;
            case PictureConfig.CHOOSE_REQUEST:
                if(resultCode!=RESULT_OK){
                    return;
                }

                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    String path;
                    if (media.isCompressed()) {
                        path = media.getCompressPath();
                    } else if (media.isCut()) {
                        path = media.getCutPath();
                    } else {
                        path = media.getPath();
                    }

                    startUpload(path);
                }
                break;
    }
    }

    public void startUpload(String path) {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }
        mvpPresenter.person_img(token, new File(path), mCctivity);
    }


    @Override
    public void SendResultSuccess(CommitModel commitModel) {
        if (commitModel.getImgPath() != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.no_img)
                    .transform(new GlideCircleTransform(mCctivity));
            Glide.with(mCctivity)
                    .load(commitModel.getImgPath().contains("http")?""+commitModel.getImgPath():ApiStores.IMG_URL_TOP + commitModel.getImgPath())
                    .apply(options)
                    .into(person_img);

            if (personModel != null) {
                personModel.setPhoto(commitModel.getImgPath());

                AppUtil.save(personModel, FileUtils.configPath(mCctivity));
            }
        }
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}

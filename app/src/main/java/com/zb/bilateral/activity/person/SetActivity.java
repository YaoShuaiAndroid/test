package com.zb.bilateral.activity.person;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.DownloadUtil;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.util.PermissionUtils;
import com.zb.bilateral.Constants;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.GlideCatchUtil;
import com.zb.bilateral.view.CommitDialog;
import com.zb.bilateral.view.PromptLoginDialog;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SetActivity extends BaseNewActivity<PublicPresenter>  implements PublicView{
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.set_version_name)
    TextView setVersionName;
    @Bind(R.id.set_cancel_size)
    TextView setCancelSize;
    @Bind(R.id.set_version_img)
    TextView setVersionImg;

    private PublicModel publicModel=null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("设置");

        setCancelSize.setText(GlideCatchUtil.getInstance().getCacheSize(mCctivity));

        setVersionName.setText("V "+AppUtil.getVersionName(mCctivity));

        checkVersion();
    }

    public void checkVersion(){
         publicModel = (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));

        if(publicModel==null){
            return;
        }

        int version=0;
        try{
            version=Integer.parseInt(publicModel.getVersion());
        }catch (Exception e){
            AppToast.makeToast(mCctivity,"后台类型异常");
        };


        if(AppUtil.getVersionCode(mCctivity)<version){
            setVersionImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    public void other( ) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.other(mCctivity);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img,R.id.set_edit_pwd,R.id.set_cancel,R.id.set_person_message,
    R.id.about_we_rel,R.id.set_help_rel,R.id.set_cancel_commit,R.id.set_version_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.set_edit_pwd:
                String token=AppUtil.getToken(mCctivity);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mCctivity,topCenterText);
                    return;
                }

                Intent intent=new Intent(mCctivity,EditPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.set_cancel://取消登录
                //取消上一个账号的缓存数据
                File file=new File(FileUtils.configPath(mCctivity));
                FileUtils.deleteFile(file);

                intent=new Intent(mCctivity, LoginActivity.class);
                startActivity(intent);

                finish();
                break;
            case R.id.set_person_message://个人资料
                token=AppUtil.getToken(mCctivity);
                if(TextUtils.isEmpty(token)){
                     intent=new Intent(mCctivity, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mCctivity,topCenterText);
                    return;
                }

                intent=new Intent(mCctivity, PersonMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.about_we_rel://关于我们
                if(publicModel!=null){
                    intent=new Intent(mCctivity, AboutWeActivity.class);
                    intent.putExtra("content",publicModel.getAbout());
                    intent.putExtra("title","关于我们");
                    startActivity(intent);
                }else{
                    other();
                }
                break;
            case R.id.set_help_rel:
                if(publicModel!=null){
                    intent=new Intent(mCctivity, AboutWeActivity.class);
                    intent.putExtra("content",publicModel.getHelp());
                    intent.putExtra("title","使用帮助");
                    startActivity(intent);
                }else{
                    other();
                }
                break;
            case R.id.set_cancel_commit:
                GlideCatchUtil.getInstance().clearImageAllCache(mCctivity);

                AppToast.makeToast(mCctivity,"正在清除中...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                },3000);
                break;
            case R.id.set_version_rel:
                if(publicModel==null){
                    return;
                }

                int version=0;
                try{
                    version=Integer.parseInt(publicModel.getVersion());
                }catch (Exception e){
                    AppToast.makeToast(mCctivity,"后台类型异常");
                };

                if(AppUtil.getVersionCode(mCctivity)<version){
                    new CommitDialog((Activity) mCctivity,setVersionName,handler,"当前有新版本 V"+publicModel.getVersion()+",是否更新?");
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(Object o) {

    }

    @Override
    public void SendResultFail(String msg) {

    }

    @Override
    public void SendBannerSuccess(List list) {

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    setCancelSize.setText(GlideCatchUtil.getInstance().getCacheSize(mCctivity));
                    break;
                case Constants.COMMIT_DIALOG:
                    //下载
                    showDownloadProgressDialog();

                    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&!EasyPermissions.hasPermissions(mCctivity, perms)) {
                        PermissionUtils.requestPermission((Activity) mCctivity, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, grantListener);
                    } else {
                        loadApk();
                    }
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, grantListener);
    }

    private final PermissionUtils.PermissionGrantListener grantListener = new PermissionUtils.PermissionGrantListener() {
        @Override
        public void onPermissionGranted(int requestCode) {
            if (requestCode == PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE) {
                loadApk();
            }
        }

        @Override
        public void onPermissionDenied(int requestCode) {
            progressDialog.dismiss();
            Toast.makeText(mCctivity,R.string.permission_denied_hint,Toast.LENGTH_SHORT).show();
            PermissionUtils.openPermissionSettingActivity((Activity) mCctivity);
        }
    };

    public void loadApk(){
        progressDialog.show();

        DownloadUtil.get().download(publicModel.getApk(),  getExternalCacheDir().getPath(),
                publicModel.getVersion() + ".apk", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String fileName = getExternalCacheDir().getPath() + "/" + publicModel.getVersion() + ".apk";
                                //打开下载成功的pdf文件
                                openFile( new File(fileName));

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                AppToast.makeToast(mCctivity,"下载失败");
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
            Uri contentUri = FileProvider.getUriForFile(mCctivity,"com.zb.bilateral.fileprovider",file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    ProgressDialog progressDialog=null;
    private void showDownloadProgressDialog() {
         progressDialog = new ProgressDialog(mCctivity);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);                    //设置不可点击界面之外的区域让对话框小时
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         //进度条类型
    }
}

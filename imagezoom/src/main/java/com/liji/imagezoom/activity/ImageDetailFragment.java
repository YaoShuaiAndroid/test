package com.liji.imagezoom.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.DownloadUtil;
import com.example.mycommon.util.PermissionUtils;
import com.liji.imagezoom.R;
import com.liji.imagezoom.widget.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;


public class ImageDetailFragment extends Fragment implements View.OnLongClickListener {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    private Activity mContext;

    //系统相册目录
    File galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);

        mAttacher.setOnLongClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });


    }

    @Override
    public boolean onLongClick(View view) {
        showNormalDialog();
        return false;
    }

    private void showNormalDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("是否保存本张图片至相册?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !EasyPermissions.hasPermissions(mContext, perms)) {
                            PermissionUtils.requestPermission((Activity) mContext, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, grantListener);
                        } else {
                            loadApk();
                        }
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(mContext, requestCode, permissions, grantResults, grantListener);
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
            Toast.makeText(mContext, R.string.permission_denied_hint, Toast.LENGTH_SHORT).show();
            PermissionUtils.openPermissionSettingActivity((Activity) mContext);
        }
    };

    public void loadApk() {
        // 首先保存图片
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        final File appDir = new File(pictureFolder ,"Beauty");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        final String fileNameDir=System.currentTimeMillis()+".png";

        DownloadUtil.get().download(mImageUrl, appDir.getAbsolutePath(),
                fileNameDir , new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //打开下载成功的pdf文件
                                AppToast.makeToast(mContext, "下载完成");

                                String fileName = appDir.getAbsolutePath()+"/" +fileNameDir;
                                //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri uri = Uri.fromFile(new File(fileName));
                                intent.setData(uri);
                                mContext.sendBroadcast(intent);

                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        //Log.i("tag", "" + progress);
                    }

                    @Override
                    public void onDownloadFailed() {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppToast.makeToast(mContext, "下载失败");
                            }
                        });
                    }
                });
    }
}

package com.zb.bilateral.activity.home_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mycommon.util.StatusBarCompat;
import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.home_page.same_relics.SameRelicsActivity;


/**
 * 模仿微信的扫描界面
 */
public class WeChatCaptureActivity extends BaseCaptureActivity {
    private static final String TAG = WeChatCaptureActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    private RelativeLayout top_left_rel;

    /**
     * 需要跳转的activity
     */
    private String title;

    public static void launch(Activity context,String title,int requestCode) {
        Intent intent = new Intent(context, WeChatCaptureActivity.class);
        intent.putExtra("title",title);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarCompat.compat(WeChatCaptureActivity.this,1);

        setContentView(R.layout.activity_wechat_capture);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        top_left_rel= (RelativeLayout) findViewById(R.id.top_left_rel);

        top_left_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=getIntent().getStringExtra("title");
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
    }



    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        playBeepSoundAndVibrate(true, false);


        Intent intent=null;
        if(title!=null&&title.equals("ExhibitionHallActivity")){
            intent= new Intent(WeChatCaptureActivity.this, ExhibitionHallActivity.class);
        }else if(title!=null&&title.equals("ExhibitionHallDetailActivity")){
            intent= new Intent(WeChatCaptureActivity.this, ExhibitionHallDetailActivity.class);
        }else if(title!=null&&title.equals("GalleryTourActivity")){
            intent= new Intent(WeChatCaptureActivity.this, GalleryTourActivity.class);
        }else if(title!=null&&title.equals("CulturalRelicsActivity")){
            intent= new Intent(WeChatCaptureActivity.this, CulturalRelicsActivity.class);
        }else if(title!=null&&title.equals("SameRelicsActivity")){
            intent= new Intent(WeChatCaptureActivity.this, SameRelicsActivity.class);
        }
        intent.putExtra("result_string", rawResult.getText());
        setResult(100,intent);

        finish();

//        对此次扫描结果不满意可以调用
//        reScan();
    }
}

package com.zb.bilateral.activity.person;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.mvp.BasePresenter;

import org.w3c.dom.Text;

import butterknife.Bind;

public class AboutWeActivity extends BaseNewActivity {
    @Bind(R.id.about_we_text)
    TextView about_we_text;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;

    private String title,content;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_we;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");

        topLeftImg.setBackgroundResource(R.mipmap.left_back);

        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    public void init(){
        if(!TextUtils.isEmpty(title)){
            topCenterText.setText(title);
        }
        if(!TextUtils.isEmpty(content)){
            about_we_text.setText(content);
        }

        topLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

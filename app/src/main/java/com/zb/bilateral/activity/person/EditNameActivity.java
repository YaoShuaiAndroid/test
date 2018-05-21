package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.AppUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class EditNameActivity extends BaseNewActivity<PublicPresenter<CommitModel>> implements PublicView<CommitModel> {
    @Bind(R.id.edit_name)
    EditText edit_name;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_right_text)
    TextView top_right_text;

    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_name;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        name = getIntent().getStringExtra("name");
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("修改名称");
        top_right_text.setText("保存");


        if (name != null) {
            edit_name.setText(name);
            edit_name.setSelection(name.length());
        }
    }

    @Override
    protected void initData() {

    }

    public void updateName( ) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.updateName(name,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.top_right_rel:
                name=edit_name.getText().toString();

                if(TextUtils.isEmpty(name)){
                    AppToast.makeToast(mCctivity,"请输入修改的名称");
                    return;
                }

                updateName();
                break;
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this, mCctivity);
    }

    @Override
    public void SendResultSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity,"修改成功");

        Intent intent=new Intent();
        intent.putExtra("name",name);
        setResult(100,intent);

        finish();
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {

    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }
}

package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.AddressDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MuseumEentranceActivity extends BaseNewActivity<PublicPresenter<CommitModel>> implements PublicView<CommitModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.museum_entry_address_text)
    TextView museumEntryAddressText;
    @Bind(R.id.museum_entry_name)
    EditText museumEntryName;
    @Bind(R.id.museum_entry_detail_adresss)
    EditText museumEntryDetailAdresss;
    @Bind(R.id.museum_entry_mannage_name)
    EditText museumEntryMannageName;
    @Bind(R.id.museum_entry_mannage_phone)
    EditText museumE_entryMannagePhone;
    @Bind(R.id.museum_entry_link_phone)
    EditText museumEntryLinkPhone;
    @Bind(R.id.museum_entry_link_name)
    EditText museumEntryLinkName;
    @Bind(R.id.museum_phone_text)
    TextView museumPhoneText;

    private String provice,city;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_museum_eentrance;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("新馆入驻");

        PublicModel publicModel= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));
        if(publicModel!=null){
            museumPhoneText.setText(publicModel.getPhone());
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.top_left_img,R.id.museum_entry_address_text,R.id.museum_entry_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.museum_entry_address_text://博物馆详细地址
                AddressDialog mChangeAddressPopwindow = new AddressDialog(mCctivity);
                mChangeAddressPopwindow.setClippingEnabled(false);
                mChangeAddressPopwindow.setAddress("广东", "深圳");
                mChangeAddressPopwindow.showAtLocation(topCenterText, Gravity.BOTTOM, 0, 0);
                mChangeAddressPopwindow
                        .setAddresskListener(new AddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String provinceText, String cityText) {
                                provice=provinceText;
                                city=cityText;
                                museumEntryAddressText.setText(provice+"-"+city);
                            }
                        });
                break;
            case R.id.museum_entry_commit:
                String name=museumEntryName.getText().toString();
                String detailedAddress=museumEntryDetailAdresss.getText().toString();
                String curatorName=museumEntryMannageName.getText().toString();
                String curatorPhone=museumE_entryMannagePhone.getText().toString();
                String linkmanName=museumEntryLinkName.getText().toString();
                String linkmanPhone=museumEntryLinkPhone.getText().toString();

                if(TextUtils.isEmpty(name)){
                    AppToast.makeToast(mCctivity,"请输入博物馆名称");
                    return;
                }

                if(TextUtils.isEmpty(detailedAddress)){
                    AppToast.makeToast(mCctivity,"请输入博物馆详细地址");
                    return;
                }

                if(TextUtils.isEmpty(curatorName)){
                    AppToast.makeToast(mCctivity,"请输入馆长姓名");
                    return;
                }

                if(TextUtils.isEmpty(curatorPhone)){
                    AppToast.makeToast(mCctivity,"请输入馆长手机号");
                    return;
                }

                if(TextUtils.isEmpty(linkmanName)){
                    AppToast.makeToast(mCctivity,"请输入联系人名称");
                    return;
                }

                if(TextUtils.isEmpty(linkmanPhone)){
                    AppToast.makeToast(mCctivity,"请输入联系人电话");
                    return;
                }

                if(TextUtils.isEmpty(provice)){
                    AppToast.makeToast(mCctivity,"请选择博物馆地址");
                    return;
                }

                newMu( name, provice, city, detailedAddress, curatorName, curatorPhone,
                         linkmanName, linkmanPhone);
                break;
        }
    }

    /**
     * 场馆入驻
     * @param name 博物馆名称
     * @param province
     * @param city
     * @param detailedAddress 详细地址
     * @param curatorName 馆长姓名
     * @param curatorPhone 馆长电话
     * @param linkmanName 	联系人姓名
     * @param linkmanPhone 联系人电话
     */
    public void newMu(String name,String province,String city,String detailedAddress,String curatorName,String curatorPhone,
                        String linkmanName,String linkmanPhone) {
        String token= AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.newMu(token,name, province, city, detailedAddress, curatorName, curatorPhone,
                     linkmanName, linkmanPhone);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity,"您的入驻请求已提交，请等待管理员审核");
        finish();
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}

package com.zb.bilateral.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.DateDialog;
import com.zb.bilateral.view.PromptLoginDialog;
import com.zb.bilateral.view.TimeDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AppointmentFragment extends BaseNewFragment<PublicPresenter<CommitModel>> implements PublicView<CommitModel> {
@Bind(R.id.appointment_date)
    TextView appointmentDate;
    @Bind(R.id.appointment_time_text)
    TextView appointmentimeText;
    @Bind(R.id.appointment_remark)
    EditText appointmentRemark;
    @Bind(R.id.appointment_num)
    EditText appointmentNum;
    @Bind(R.id.appointment_phone)
    EditText appointmentPhone;
    @Bind(R.id.appointment_name)
    EditText appointmentName;

    private final static int STATUS_DATE=11;
    private final static int STATUS_TIME=12;

    private MuseumDetailActivity museumActivity;

    private String museum_id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        museumActivity= (MuseumDetailActivity) activity;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        Bundle args=getArguments();
        museum_id=args.getString("museum_id");

        appointmentDate.setText(AppUtil.getStrTime());
        appointmentimeText.setText(AppUtil.getStrTime_mm());
    }

    @Override
    protected void initData() {

    }

    /**
     *  展览详情
     */
    public void commentary(String name,String phone,String bookingTime,String number,String other) {
        String token=AppUtil.getToken(mContext);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.commentary( museum_id , name, phone, bookingTime, number, other,token);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }


    @OnClick({R.id.top_left_img,R.id.appointment_year_rel,R.id.appointment_time_rel,R.id.appointment_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                museumActivity.close();
                break;
            case R.id.appointment_year_rel:
                new DateDialog(mContext,appointmentDate,appointmentDate.getText().toString(),handler,"选择时间");
                break;
            case R.id.appointment_time_rel://选择时间
                new TimeDialog(mContext,appointmentDate,appointmentimeText.getText().toString(),handler,"选择时间");
                break;
            case R.id.appointment_commit:
                if(AppUtil.getToken(mContext).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,appointmentName);
                    return;
                }

                String name= appointmentName.getText().toString();
                String phone=appointmentPhone.getText().toString();
                String remark=appointmentRemark.getText().toString();
                String num=appointmentNum.getText().toString();
                String date=appointmentDate.getText().toString()+" "+appointmentimeText.getText().toString();

                if(!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mContext,"   请输入正确的手机号码");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    AppToast.makeToast(mContext,"请输入姓名");
                    return;
                }

                if(TextUtils.isEmpty(num)){
                    AppToast.makeToast(mContext,"请输入听讲人数");
                    return;
                }

                commentary(name,phone,date,num,remark);
                break;
            default:
                break;
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STATUS_DATE:
                    appointmentDate.setText(""+msg.obj);
                    break;
                case STATUS_TIME:
                    LogUtil.i(""+msg.obj);
                    appointmentimeText.setText(""+msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void SendResultSuccess(CommitModel commitModel) {
        AppToast.makeToast(mContext,"提交成功");

        appointmentName.setText("");
        appointmentPhone.setText("");
        appointmentRemark.setText("");
        appointmentNum.setText("");
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {

    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mContext,msg);
    }
}

package com.zb.bilateral;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.fragment.CultureFragment;
import com.zb.bilateral.fragment.HomePageFragment;
import com.zb.bilateral.fragment.PersonFragment;
import com.zb.bilateral.fragment.PolicyFragment;
import com.zb.bilateral.fragment.VoiceFragment;
import com.zb.bilateral.mvp.BasePresenter;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class MainActivity extends BaseNewActivity {
    HomePageFragment mHomePageFragment;
    CultureFragment mCultureFragment;
    VoiceFragment mVoiceFragment;
    PolicyFragment mPolicyFragment;
    PersonFragment mPersonFragment;
    FragmentManager fragmentManager;

    @Bind(R.id.fragment_home_page)
    LinearLayout fragmentHomePage;
    @Bind(R.id.fragment_culture)
    LinearLayout fragmentCulture;
    @Bind(R.id.fragment_voice)
    LinearLayout fragmentVoice;
    @Bind(R.id.fragment_policy)
    LinearLayout fragmentPolicy;
    @Bind(R.id.fragment_person)
    LinearLayout fragmentPerson;
    @Bind(R.id.main_frameLayout)
    FrameLayout mainFrameLayout;
    @Bind(R.id.fragment_home_page_text)
    TextView fragmentHomePageText;
    @Bind(R.id.fragment_culture_text)
    TextView fragmentCultureText;
    @Bind(R.id.fragment_voice_text)
    TextView fragmentVoiceText;
    @Bind(R.id.fragment_policy_text)
    TextView fragmentPolicyText;
    @Bind(R.id.fragment_person_text)
    TextView fragmentPersonText;
    @Bind(R.id.fragment_home_page_img)
    ImageView fragmentHomePageImg;
    @Bind(R.id.fragment_culture_img)
    ImageView fragmentCultureImg;
    @Bind(R.id.fragment_policy_img)
    ImageView fragmentPolicyImg;
    @Bind(R.id.fragment_person_img)
    ImageView fragmentPersonImg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        clickButtomTag(fragmentHomePage);

        //开启极光页面流统计
        JAnalyticsInterface.onPageStart(this,this.getClass().getCanonicalName());
    }

    /**
     * launchMode为singleTask的时候，通过Intent启到一个Activity,如果系统已经存在一个实例，
     * 系统就会将请求发送到这个实例上，但这个时候，
     * 系统就不会再调用通常情况下我们处理请求数据的onCreate方法，而是调用onNewIntent方法
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int item=intent.getIntExtra("item",-1);
        if(item==0){
            //跳转首页
            changeHome();
        }else if(item==3){
            //跳转个人中心
            changePerson();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.fragment_home_page, R.id.fragment_culture, R.id.fragment_voice, R.id.fragment_policy, R.id.fragment_person})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_home_page:
                clickButtomTag(fragmentHomePage);
                break;
            case R.id.fragment_culture:
                clickButtomTag(fragmentCulture);
                break;
            case R.id.fragment_voice:
                clickButtomTag(fragmentVoice);
                break;
            case R.id.fragment_policy:
                clickButtomTag(fragmentPolicy);
                break;
            case R.id.fragment_person:
                clickButtomTag(fragmentPerson);
                break;
        }
    }

    public void clickButtomTag(View view) {
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidenFragments(transaction);
        switch (view.getId()) {
            case R.id.fragment_home_page:
                click_HomePage();
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                    transaction.add(R.id.main_frameLayout, mHomePageFragment);
                } else {
                    transaction.show(mHomePageFragment);
                }

                break;
            case R.id.fragment_culture:
                click_Culture();
                if (mCultureFragment == null) {
                    mCultureFragment = new CultureFragment();
                    transaction.add(R.id.main_frameLayout, mCultureFragment);
                } else {
                    transaction.show(mCultureFragment);
                }
                break;
            case R.id.fragment_voice:
                click_Voice();
                if (mVoiceFragment == null) {
                    mVoiceFragment = new VoiceFragment();
                    transaction.add(R.id.main_frameLayout, mVoiceFragment);
                } else {
                    transaction.show(mVoiceFragment);
                }
                break;
            case R.id.fragment_policy:
                click_Policy();
                if (mPolicyFragment == null) {
                    mPolicyFragment = new PolicyFragment();
                    transaction.add(R.id.main_frameLayout, mPolicyFragment);
                } else {
                    transaction.show(mPolicyFragment);
                }
                break;
            case R.id.fragment_person:
                click_Person();
                if (mPersonFragment == null) {
                    mPersonFragment = new PersonFragment();
                    transaction.add(R.id.main_frameLayout, mPersonFragment);
                } else {
                    transaction.show(mPersonFragment);
                }
                break;
            default:
                break;
        }

        transaction.commit();
    }
    //跳转到文化拾遗
    public void changeCultrue(){
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidenFragments(transaction);

        click_Culture();
        if (mCultureFragment == null) {
            mCultureFragment = new CultureFragment();
            transaction.add(R.id.main_frameLayout, mCultureFragment);
        } else {
            transaction.show(mCultureFragment);
        }

        transaction.commit();
    }

    //跳转到首页
    public void changeHome(){
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidenFragments(transaction);

        click_HomePage();
        if (mHomePageFragment == null) {
            mHomePageFragment = new HomePageFragment();
            transaction.add(R.id.main_frameLayout, mHomePageFragment);
        } else {
            transaction.show(mHomePageFragment);
        }

        transaction.commit();
    }

    //跳转到文化语音导览
    public void changeMuseum(){
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidenFragments(transaction);

        click_Voice();
        if (mVoiceFragment == null) {
            mVoiceFragment = new VoiceFragment();
            transaction.add(R.id.main_frameLayout, mVoiceFragment);
        } else {
            transaction.show(mVoiceFragment);
        }

        transaction.commit();
    }

    //跳转到个人中心
    public void changePerson(){
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidenFragments(transaction);

        click_Person();
        if (mPersonFragment == null) {
            mPersonFragment = new PersonFragment();
            transaction.add(R.id.main_frameLayout, mPersonFragment);
        } else {
            transaction.show(mPersonFragment);
        }

        transaction.commit();
    }

    private void hidenFragments(FragmentTransaction transaction) {
        if (mHomePageFragment != null) {
            transaction.hide(mHomePageFragment);
        }
        if (mCultureFragment != null) {
            transaction.hide(mCultureFragment);
        }
        if (mVoiceFragment != null) {
            transaction.hide(mVoiceFragment);
        }
        if (mPolicyFragment != null) {
            transaction.hide(mPolicyFragment);
        }
        if (mPersonFragment != null) {
            transaction.hide(mPersonFragment);
        }
    }

    public void click_HomePage() {
        fragmentHomePageImg.setBackgroundResource(R.mipmap.home_page_true);
        fragmentCultureImg.setBackgroundResource(R.mipmap.cultrue_false);
        fragmentPolicyImg.setBackgroundResource(R.mipmap.policy_false);
        fragmentPersonImg.setBackgroundResource(R.mipmap.person_false);
        fragmentHomePageText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentCultureText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentVoiceText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPolicyText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPersonText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_Culture() {
        fragmentHomePageImg.setBackgroundResource(R.mipmap.home_page_false);
        fragmentCultureImg.setBackgroundResource(R.mipmap.cultrue_true);
        fragmentPolicyImg.setBackgroundResource(R.mipmap.policy_false);
        fragmentPersonImg.setBackgroundResource(R.mipmap.person_false);
        fragmentHomePageText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCultureText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentVoiceText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPolicyText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPersonText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_Voice() {
        fragmentHomePageImg.setBackgroundResource(R.mipmap.home_page_false);
        fragmentCultureImg.setBackgroundResource(R.mipmap.cultrue_false);
        fragmentPolicyImg.setBackgroundResource(R.mipmap.policy_false);
        fragmentPersonImg.setBackgroundResource(R.mipmap.person_false);
        fragmentHomePageText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCultureText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentVoiceText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentPolicyText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPersonText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_Policy() {
        fragmentHomePageImg.setBackgroundResource(R.mipmap.home_page_false);
        fragmentCultureImg.setBackgroundResource(R.mipmap.cultrue_false);
        fragmentPolicyImg.setBackgroundResource(R.mipmap.policy_true);
        fragmentPersonImg.setBackgroundResource(R.mipmap.person_false);
        fragmentHomePageText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCultureText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentVoiceText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPolicyText.setTextColor(getResources().getColor(R.color.main_color));
        fragmentPersonText.setTextColor(getResources().getColor(R.color.gray_color));
    }

    public void click_Person() {
        fragmentHomePageImg.setBackgroundResource(R.mipmap.home_page_false);
        fragmentCultureImg.setBackgroundResource(R.mipmap.cultrue_false);
        fragmentPolicyImg.setBackgroundResource(R.mipmap.policy_false);
        fragmentPersonImg.setBackgroundResource(R.mipmap.person_true);
        fragmentHomePageText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentCultureText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentVoiceText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPolicyText.setTextColor(getResources().getColor(R.color.gray_color));
        fragmentPersonText.setTextColor(getResources().getColor(R.color.main_color));
    }


    //连按二次返回键退出应用
    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //极光页面结束接口
        JAnalyticsInterface.onPageEnd(this,
                this.getClass().getCanonicalName());
    }
}


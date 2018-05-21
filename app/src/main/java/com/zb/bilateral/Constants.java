package com.zb.bilateral;

import android.content.IntentFilter;

import com.zb.bilateral.util.MyConstant;

/**
 * Created by pengl on 2016/12/1.
 */

public class Constants {

    public final static int COMMIT_DIALOG=10;
    public final static int MAIN_ACTIVITY=101;//返回首页
    public final static int MUSEUM_ACTIVITY=102;//返回博物馆
    public final static int SHOW_ACTIVITY=103;//返回展厅导览
    public final static int PERSON_ACTIVITY=104;//返回个人中心
    public final static int SHARE_ACTIVITY=105;//分享

    public final static boolean HAS_GUIDE_PAGE = true;
    public final static boolean DEBUG = true;

    public final static String WEIXIN_APPID="wx0d2f2203ca3d6aea";
    public final static String WEIXIN_APPSCRET="fad6ab85645c01c1f099d3cb4532c6d2";
    public final static String QQ_APPID="101459683";
    public final static String QQ_APPSCRET="13a0f6942ec4990ec33f00bdc17ccf2f";

    public final static String NET_EXCEPTION_STR = "请求异常";
    public final static String NET_FAIL_STR = "请求失败,";
    public final static String SONG_CANCEL="退出语音导览将取消语音播报服务，确定退出吗？";

    public static final String JSON = "application/json;charset=utf-8";

    //个人中心图片路径
    public static final String TMP_PATH = "clip_temp.jpg";

    public static IntentFilter getIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstant.PLAY);
        intentFilter.addAction(MyConstant.PAUSE);
        intentFilter.addAction(MyConstant.PLAYTO);
        intentFilter.addAction(MyConstant.PAUSE_TO_PLAY);
        intentFilter.addAction(MyConstant.PLAY_OTHER);
        intentFilter.addAction(MyConstant.STOP);
        intentFilter.addAction(MyConstant.END);
        intentFilter.addAction(MyConstant.UPDATE_POSITION);

        return intentFilter;
    }
}

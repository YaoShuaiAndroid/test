package com.zb.bilateral.brocast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.zb.bilateral.util.MyConstant;

/**
 * Created by yaos on 2018/4/3.
 */

public class MyMusicBrocast extends BroadcastReceiver {
    Handler handler;

    public MyMusicBrocast(Handler handler){
        this.handler=handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Message msg = new Message();
        if (MyConstant.PLAY.equals(action)) {
            msg.what = 0;
            msg.obj = action;
            handler.sendMessage(msg);
        }
        if (MyConstant.PAUSE.equals(action)) {
            //播放暂停或重播
            msg.what = 1;
            msg.obj = action;
            handler.sendMessage(msg);
        } else if (MyConstant.UPDATE_POSITION.equals(action)) {
            //滑动播放位置
            msg.what = 2;
            handler.sendMessage(msg);
        } else if (MyConstant.END.equals(action)) {
            //播放完成
            msg.what = 3;
            handler.sendMessage(msg);
        }}
}

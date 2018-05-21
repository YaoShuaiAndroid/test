package com.zb.bilateral.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.Constants;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.base.BaseActivityManager;
import com.zb.bilateral.model.SongInfo;
import com.zb.bilateral.util.MyConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class MuiscService extends Service {
    // 服务是否在运行
    public static Boolean isServiceRunning = false;
    // 是否在播放
    public static Boolean isPlaying = false;
    MyMusicBrocast musicBrocast;
    private Thread playerThread = null;
    private MediaPlayer player;
    private Context context;
    private MyApplication application;
    private SongInfo songInfo;
    //是否暂停
    public static boolean is_pause = false;
    //是否开始播放
    public static boolean is_start = true;
    /**
     * 音频管理
     */
    private AudioManager audioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;

        musicBrocast = new MyMusicBrocast();
        registerReceiver(musicBrocast, Constants.getIntentFilter());

        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        filter.addAction("pause");
        filter.addAction("close");
        registerReceiver(onClickReceiver, filter);

        context = MuiscService.this.getBaseContext();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        application = (MyApplication) getApplication();

        songInfo = application.getPlaying();

        if (songInfo != null) {
            Message msg2 = new Message();
            msg2.what = 1;
            msg2.obj = MyConstant.PLAY;
            play(songInfo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioManager.abandonAudioFocus(afChangeListener);
        isServiceRunning = false;

        stop();

        if (musicBrocast != null) {
            unregisterReceiver(musicBrocast);
        }
        if (onClickReceiver != null) {
            unregisterReceiver(onClickReceiver);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class MyMusicBrocast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MyConstant.PLAY.equals(action)) {
                if (null != player && player.isPlaying()) {
                    isPlaying = false;
                }
                songInfo = application.getPlaying();
                if (songInfo != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            play(songInfo);
                        }
                    }.start();
                }
            }else if (MyConstant.PAUSE_TO_PLAY.equals(action)) {
                if (null != player && player.isPlaying()) {
                    isPlaying = false;
                }
                is_pause=false;

                songInfo = application.getPlaying();
                if (songInfo != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            play(songInfo);
                        }
                    }.start();

                }
            }else if(MyConstant.PLAY_OTHER.equals(action)){
                pauseOther();
            } else if (MyConstant.PAUSE.equals(action)) {
                pause();
            } else if (MyConstant.STOP.equals(action)) {
                stop();
            } else if (MyConstant.PLAYTO.equals(action)) {
                int posintion = intent.getIntExtra("position", 0);
                if (songInfo != null) {
                    songInfo.setPlayProgress(posintion);
                    play(songInfo);
                }
            }
            if (MyConstant.PLAY.equals(action) || MyConstant.PAUSE.equals(action)) {
                Message msg2 = new Message();
                msg2.what = 1;
                msg2.obj = action;
            }
        }
    }

    BroadcastReceiver onClickReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("play")) {
                Intent play = new Intent(MyConstant.PLAY);
                sendBroadcast(play);
            } else if (intent.getAction().equals("pause")) {
                Intent pause = new Intent(MyConstant.PAUSE);
                sendBroadcast(pause);
            } else if (intent.getAction().equals("close")) {
                stopService(new Intent(context, MuiscService.class));
            }
        }

    };

    private void play(final SongInfo songInfo) {
        if (songInfo == null)
            return;
        try {
            if (player == null) {
                //最开始进入播放
                player = new MediaPlayer();

                player.setDataSource(songInfo.getSongPath());

                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (songInfo.getPlayProgress() != 0) {
                            final int play_progress = (int) songInfo.getPlayProgress();

                            player.seekTo(play_progress);
                        }

                        isPlaying = true;
                        player.start();
                    }
                });
                player.prepareAsync();
            } else {
                if (is_pause && songInfo.getPlayProgress() != 0) {
                    //从暂停时播放
                    is_pause = false;

                    isPlaying = true;

                    player.start();
                } else {
                    if (is_start) {
                        player.stop();
                        is_start = false;
                    }

                    player.reset();
                    player.setDataSource(songInfo.getSongPath());

                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (songInfo.getPlayProgress() != 0) {
                                final int play_progress = (int) songInfo.getPlayProgress();
                                player.seekTo(play_progress);
                            }
                            is_start = true;
                            isPlaying = true;

                            player.start();
                        }
                    });

                    player.prepareAsync();
                }
            }

            //监听播放完成
            player.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent pause = new Intent(MyConstant.END);
                    sendBroadcast(pause);
                }
            });

            // 请求播放的音频焦点
            int result = audioManager.requestAudioFocus(afChangeListener,
                    // 指定所使用的音频流
                    AudioManager.STREAM_MUSIC,
                    // 请求长时间的音频焦点
                    AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                //进度条线程
                application.setIs_click(true);
                if (playerThread == null) {
                    playerThread = new Thread(new PlayerRunable());
                    playerThread.start();
                }
            } else {
                Toast.makeText(context, "获取音频焦点失败!!", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppToast.showShortText(context, "获取音频有误请重新操作");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //暂停
    public void pause() {
        if (player != null) {
            if (player.isPlaying()) {
                isPlaying = false;
            }
            player.pause();

            if (!is_pause) {
                is_pause = true;

                sendPause();
            }
        }
    }

    //播放其他，首次先暂停
    public void pauseOther() {
        if (player != null) {
            if (player.isPlaying()) {
                isPlaying = false;
            }
            player.pause();
        }
    }

    public void stop() {
        if (songInfo != null) {
            songInfo.setPlayProgress(0);
            application.setPlaying(songInfo);
        }
        sendStop();
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                isPlaying = false;
            }
            player.reset();
            player.release();
            player = null;
        }
    }

    private class PlayerRunable implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300);
                    if (player != null && player.isPlaying()) {
                        if (songInfo != null) {
                            songInfo.setPlayProgress(player.getCurrentPosition());
                            songInfo.setTotal_time(player.getDuration());
                            application.setPlaying(songInfo);
                            sendUpatePosition();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新播放进度
     */
    private void sendUpatePosition() {
        Intent intent = new Intent(MyConstant.UPDATE_POSITION);
        sendBroadcast(intent);
    }

    /**
     * 发送停止广播
     */
    private void sendStop() {
        Intent intent = new Intent(MyConstant.STOP);
        sendBroadcast(intent);
    }

    /**
     * 暂停
     */
    private void sendPause() {
        Intent intent = new Intent(MyConstant.PAUSE);
        sendBroadcast(intent);
    }

    /**
     * AUDIOFOCUS_GAIN：获得音频焦点。
     * AUDIOFOCUS_LOSS：失去音频焦点，并且会持续很长时间。这是我们需要停止MediaPlayer的播放。
     * AUDIOFOCUS_LOSS_TRANSIENT
     * ：失去音频焦点，但并不会持续很长时间，需要暂停MediaPlayer的播放，等待重新获得音频焦点。
     * AUDIOFOCUS_REQUEST_GRANTED 永久获取媒体焦点（播放音乐）
     * AUDIOFOCUS_GAIN_TRANSIENT 暂时获取焦点 适用于短暂的音频
     * AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK Duck我们应用跟其他应用共用焦点
     * 我们播放的时候其他音频会降低音量
     */
    OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                stop();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                if (player != null) {
                    player.setVolume(0.5f, 0.5f);
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                if (player != null) {
                    player.setVolume(1.0f, 1.0f);
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                stop();
            }
        }
    };
}

package com.zb.bilateral.model;

public class SongInfo {
    private String id;
    private String songPath; // 歌曲路径 5
    private String songImage;//歌曲图片
    private String songName;//歌曲名
    private String songUserName;//唱的用户
    private long playProgress;//播放进度
    private long total_time;//时长

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPlayProgress() {
        return playProgress;
    }

    public void setPlayProgress(long playProgress) {
        this.playProgress = playProgress;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSongImage() {
        return songImage;
    }

    public void setSongImage(String songImage) {
        this.songImage = songImage;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUserName() {
        return songUserName;
    }

    public void setSongUserName(String songUserName) {
        this.songUserName = songUserName;
    }

    public long getTotal_time() {
        return total_time;
    }

    public void setTotal_time(long total_time) {
        this.total_time = total_time;
    }

    /**
     * 获取当前歌曲剩余的长度
     *
     * @return
     */
    public int getSurplusProgress() {
        int surplusProgress = (int) (total_time - playProgress);
        if (surplusProgress < 0) {
            surplusProgress = 0;
        }
        return surplusProgress;
    }

    @Override
    public String toString() {
        return "SongInfo{" +
                "id='" + id + '\'' +
                ", songPath='" + songPath + '\'' +
                ", songImage='" + songImage + '\'' +
                ", songName='" + songName + '\'' +
                ", songUserName='" + songUserName + '\'' +
                ", playProgress=" + playProgress +
                ", total_time=" + total_time +
                '}';
    }
}

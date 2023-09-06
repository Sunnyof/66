package com.game.net;

import android.util.Log;

public class DownloadInfo {

    /**
     * 下载状态
     */
    public static final String DOWNLOAD = "download";    // 下载中
    public static final String DOWNLOAD_PAUSE = "pause"; // 下载暂停

    public static final String DOWNLOAD_WAIT = "wait";  // 等待下载
    public static final String DOWNLOAD_CANCEL = "cancel"; // 下载取消
    public static final String DOWNLOAD_OVER = "over";    // 下载结束
    public static final String DOWNLOAD_ERROR = "error";  // 下载出错

    public static final String UNZIP_OVER = "finish";  //解压结束

    public static final long TOTAL_ERROR = -1;//获取进度失败

    private String url;
    private String fileName;
    private String downloadStatus;
    private long total;
    private long progress;

    private int percent;

    private String percentStr;

    private String id;

    public DownloadInfo(String url) {
        this.url = url;
    }

    public DownloadInfo(String url, String downloadStatus) {
        this.url = url;
        this.downloadStatus = downloadStatus;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        Log.i("TAG", "progress:" + progress + "----" + "total:" + total);
        if(total ==0){
            percent = 0;
        }else {
            percent = (int) (progress * 100 / total);
        }
        this.progress = progress;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getPercentStr() {
        return percent + "%";
    }

    public void setPercentStr(String percentStr) {
        this.percentStr = percentStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

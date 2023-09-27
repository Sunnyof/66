package com.game.net;

public interface DownloadListener {

    /**
     * 取消下载
     *
     * @param tag
     */
    public void cancelDownload(String tag);

    /**
     * 开始下载
     *
     * @param tag
     */
    public void startDownload(String tag);

    /**
     * 暂停下载
     *
     * @param tag
     */
    public void pauseDownload(String tag);

    /**
     * 创建下载信息
     *
     * @param url 下载URL
     */
    public void createDownload(String url);

}

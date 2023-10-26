package com.cocos.lib.utils;

import com.cocos.lib.exception.DownloadError;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/8/9
 *     desc   : 下载接口
 * </pre>
 */

public interface IListener
{
	/**
	 * 准备下载
	 *
	 * @param fileSize 下载文件长度
	 */
	void onPreExecute(long fileSize);

	/**
	 * 下载中进度刷新
	 *
	 * @param fileSize 文件长度
	 * @param downloadedSize 下载长度
	 */
	void onProgressChange(long fileSize, long downloadedSize);

	void onProgressChange(long fileSize, long downloadedSize, long speed);

	/**
	 * 暂停下载
	 */
	void onCancel();

	/**
	 * 下载失败
	 */
	void onError(DownloadError error);

	/**
	 * 下载成功
	 */
	void onSuccess();

}

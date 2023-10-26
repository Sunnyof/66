package com.cocos.lib.utils;

import com.cocos.lib.exception.DownloadError;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/8/10
 *     desc   : 文件信息回调
 * </pre>
 */

public interface OnFileInfoCallback
{
	void onComplete();

	void onCancel();

	void onError(DownloadError error);
}

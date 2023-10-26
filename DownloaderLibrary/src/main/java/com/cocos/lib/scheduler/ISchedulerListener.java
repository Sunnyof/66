package com.cocos.lib.scheduler;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/8/30
 *     desc   : 注解任务监听
 * </pre>
 */

public interface ISchedulerListener<TASK>
{
	void onPreExecute(TASK task);

	void onProgressChange(TASK task);

	void onProgressSpeedChange(TASK task);

	void onCancel(TASK task);

	void onError(TASK task);

	void onSuccess(TASK task);
}

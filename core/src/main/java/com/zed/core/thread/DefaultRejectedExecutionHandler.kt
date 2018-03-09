package com.zed.core.thread

import com.zed.common.util.LogUtils
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by zed on 2018/3/9.
 */
class DefaultRejectedExecutionHandler : RejectedExecutionHandler {
    var TAG = DefaultRejectedExecutionHandler::class.simpleName
    override fun rejectedExecution(r: Runnable?, executor: ThreadPoolExecutor?) {
        if (executor!!.isShutdown) {
            LogUtils.e(TAG, "移除堆积线程->".plus(r))
            //移除队头元素/
//            executor?.queue?.remove(r)
            executor?.queue?.poll()
            //再尝试入队
            executor?.execute(r)
            LogUtils.e(TAG, "将移除的堆积线程重新添加->".plus(r))
        }
    }
}
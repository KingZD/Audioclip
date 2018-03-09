package com.zed.core.thread

import java.util.concurrent.*


/**
 * Created by zed on 2018/3/9.
 */
open class ThreadPool : ThreadPoolExecutor {

    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit?, workQueue: BlockingQueue<Runnable>?) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue)
    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit?, workQueue: BlockingQueue<Runnable>?, threadFactory: ThreadFactory?) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory)
    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit?, workQueue: BlockingQueue<Runnable>?, handler: RejectedExecutionHandler?) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler)
    constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit?, workQueue: BlockingQueue<Runnable>?, threadFactory: ThreadFactory?, handler: RejectedExecutionHandler?) : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler)

    companion object {
        private var NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
        private var KEEP_ALIVE_TIME = 1L
        private var KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
        private var taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
        private var threadPool: ThreadPool? = null

        fun getPool(): ThreadPool {
            threadPool ?: ThreadPool(NUMBER_OF_CORES,
                    NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                    taskQueue, BackgroundThreadFactory(), DefaultRejectedExecutionHandler())
            return threadPool!!
        }

    }

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        super.beforeExecute(t, r)
    }

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        super.afterExecute(r, t)
    }

    override fun terminated() {
        super.terminated()
    }
}
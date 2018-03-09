package com.zed.core.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by zed on 2018/3/9.
 */
class BackgroundThreadFactory : ThreadFactory {
    val poolNumber = AtomicInteger(1)
    var group: ThreadGroup? = null
    /**
     *原子操作保证每个线程都有唯一的
     */
    val threadNumber = AtomicInteger(1)
    var namePrefix: String? = null

    constructor() {
        val s = System.getSecurityManager()
        group = if (s != null)
            s.threadGroup
        else
            Thread.currentThread().threadGroup
        namePrefix = "pool-"
                .plus(poolNumber.getAndIncrement())
                .plus("-thread-")

    }

    constructor(threadName:String){
        val s = System.getSecurityManager()
        group = if (s != null)
            s.threadGroup
        else
            Thread.currentThread().threadGroup
        namePrefix = threadName
                .plus(poolNumber.getAndIncrement())
                .plus("-thread-")
    }

    override fun newThread(r: Runnable?): Thread {
        val t = Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0)
        //true 守护线程 false用户线程 只要任何非守护线程还在运行，程序就不会终止
        if (t.isDaemon)
            t.isDaemon = false
        if (t.priority != Thread.NORM_PRIORITY)
            t.priority = Thread.NORM_PRIORITY
        return t
    }
}
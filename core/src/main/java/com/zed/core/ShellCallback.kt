package com.zed.core

/**
 * Created by zed on 2018/3/9.
 */
interface ShellCallback {
    fun shellOut(shellLine: String)

    fun processComplete(exitValue: Int)
}
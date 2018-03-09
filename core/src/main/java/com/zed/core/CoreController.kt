package com.zed.core

import android.content.Context
import java.io.*
import java.util.*

/**
 * Created by zed on 2018/3/9.
 */
class CoreController(context: Context) : Core(context) {

    /**
     * 获取ffmpeg信息
     */
    fun help(sc: ShellCallback) {
        val cmd = ArrayList<String>()
        cmd.add(mFfmpegBin)
        cmd.add("-h")
        execFFMPEG(cmd, sc)
    }

    fun getInfo(clipIn: Clip): Clip {
        var cmd: List<String>

        cmd = ArrayList()

        cmd.add(mFfmpegBin)
        cmd.add("-y")
        cmd.add("-i")

        cmd.add(File(clipIn.path).canonicalPath)

        val ip = InfoParser(clipIn)
        execFFMPEG(cmd, ip, null)

        try {
            Thread.sleep(200)
        } catch (e: Exception) {
        }
        return clipIn

    }
}
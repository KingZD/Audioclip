package com.zed.core

import java.lang.Cloneable

/**
 * Created by zed on 2018/3/9.
 */
class Clip : Cloneable {
    var cellId = -1
    var width = -1
    var height = -1
    /**视频编码**/
    var videoCodec: String? = null
    /**视频每秒传输帧数**/
    var videoFps: String? = null
    /**视频比特率**/
    var videoBitrate = -1
    /**视频质量**/
    var videoQuality = -1

    var videoBitStreamFilter: String? = null

    /**音频编码**/
    var audioCodec: String? = null
    var audioChannels = -1
    /**音频比特率**/
    var audioBitrate = -1
    /**音频质量**/
    var audioQuality: String? = null
    /**音频音量**/
    var audioVolume = -1f
    var audioBitStreamFilter: String? = null

    var path: String? = null
    var format: String? = null
    var mimeType: String? = null

    var startTime: String? = null //00:00:00 or seconds format
    var duration = -1.0 //00:00:00 or seconds format

    var videoFilter: String? = null
    var audioFilter: String? = null

    var qscale: String? = null
    var aspect: String? = null
    var passCount = 1 //default

    constructor()

    constructor(path: String) {
        this.path = path
    }

    fun isImage(): Boolean {
        return if (mimeType != null)
            mimeType!!.startsWith("image")
        else
            false
    }

    fun isVideo(): Boolean {
        return if (mimeType != null)
            mimeType!!.startsWith("video")
        else
            false
    }

    fun isAudio(): Boolean {
        return if (mimeType != null)
            mimeType!!.startsWith("audio")
        else
            false
    }
}
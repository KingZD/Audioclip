package com.zed.audioclip.net.bean

/**
 * Created by zed on 2018/3/28.
 */
class SongBean : BaseBean {

    constructor() : super()

    /**
     * 歌手
     */
    var singer: String? = null

    /**
     * 后缀名
     */
    var prefix: String? = null
    /**
     * 歌曲名
     */
    var song: String? = null
    /**
     * 歌曲的地址
     */
    var path: String? = null
    /**
     * 歌曲长度
     * ms毫秒
     */
    var duration: Int = 0
    /**
     * 歌曲的大小
     */
    var size: Long = 0
}
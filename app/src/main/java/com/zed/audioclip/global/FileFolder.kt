package com.zed.audioclip.global

import com.zed.common.util.LogUtils
import java.io.File

/**
 * Created by zed on 2018/3/28.
 */
class FileFolder {

    companion object {
        //初始目录
        private val audioClipRoot: String = "/audioClip"
        //剪辑的小音频文件存放目录
        private val audioClipCell: String = audioClipRoot.plus("/audioClipCell")

        //剪辑的小音频文件合成之后的存放目录
        private val audioClipMerge: String = audioClipRoot.plus("/audioClipMerge")

        //创建音频初始化目录
        fun createAudioClipRoot() {
            createFolder(audioClipRoot)
        }

        //创建剪辑之后的小音频的目录
        fun createAudioClipCellFile(name: String): String {
            val sb = StringBuffer(audioClipCell)
            sb.append(File.separatorChar)
            sb.append(name)
            createFile(sb.toString())
            return sb.toString()
        }

        fun createAudioClipMergeFile(name: String): String {
            val sb = StringBuffer(audioClipMerge)
            sb.append(File.separatorChar)
            sb.append(name)
            createFile(sb.toString())
            return sb.toString()
        }

        fun createFolder(path: String) {
            LogUtils.i("create folder $path")
            val file = File(path)
            if (!file.exists())
                file.mkdirs()
        }

        fun createFile(path: String) {
            LogUtils.i("create file $path")
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
                file.createNewFile()
            }
        }
    }
}
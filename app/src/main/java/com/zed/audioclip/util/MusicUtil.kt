package com.zed.audioclip.util

import android.provider.MediaStore
import android.content.Context
import com.zed.audioclip.net.bean.SongBean
import android.media.MediaMetadataRetriever
import android.graphics.Bitmap
import com.zed.common.util.ImageUtils


/**
 * Created by zed on 2018/3/28.
 */
class MusicUtil {
    companion object {
        fun getMusicData(context: Context?): List<SongBean> {
            return getMusicData(context, 0)
        }

        /**
         * 扫描系统里面的音频文件，返回一个list集合
         * @param musicSize 过滤小于musicSize的音乐文件
         */
        fun getMusicData(context: Context?, musicSize: Long): List<SongBean> {
            val list = ArrayList<SongBean>()
            // 媒体库查询语句（写一个工具类MusicUtils）
            val cursor = context?.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val song = SongBean()
                    song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    song.prefix = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
                    song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    if (song.size < musicSize && musicSize > 0) {
                        continue
                    }
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
//                    if (song.song.contains("-")) {
//                        val str = song.song.split("-")
//                        song.singer = str[0]
//                        song.song = str[1]
//                    }
                    // 歌曲格式
                    when {
                        "audio/mpeg" == song.prefix -> song.prefix = "mp3"
                        "audio/x-ms-wma" == song.prefix -> song.prefix = "wma"
                        else -> song.prefix = song.path!!.split(".")[1]
                    }

                    list.add(song)
                }
                // 释放资源
                cursor.close()
            }
            return list
        }

        /**
         * 根据歌曲路径获得专辑封面
         * @Description 获取专辑封面
         * @param filePath 文件路径，like XXX/XXX/XX.mp3
         * @return 专辑封面bitmap
         */
        fun createAlbumArt(filePath: String?): Bitmap? {
            var bitmap: Bitmap? = null
            //能够获取多媒体文件元数据的类
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(filePath) //设置数据源
                val embedPic = retriever.embeddedPicture //得到字节型数据
                //bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
                //要优化后再加载
                bitmap = ImageUtils.getBitmap(embedPic, 0, 80, 80)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    retriever.release()
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }

            }
            return bitmap
        }
    }

}
package com.zed.core

import android.content.Context
import android.text.TextUtils
import com.zed.common.util.LogUtils
import com.zed.core.thread.ThreadPool
import java.io.*
import java.util.*

/**
 * Created by zed on 2018/3/9.
 */
abstract class Core {
    val TAG = javaClass.simpleName
    var mFfmpegBin: String = ""

    constructor(context: Context) {
        installBinaries(context, false)
    }

    /**
     * 安装文件
     */
    fun installBinaries(context: Context, overwrite: Boolean) {
        if (TextUtils.isEmpty(mFfmpegBin))
            mFfmpegBin = installBinary(context, R.raw.ffmpeg, "ffmpeg", overwrite)
    }

    /**
     * 安装文件(执行)
     * @param ctx 上下文
     * @param resId 资源id
     * @param filename 文件名称
     * @param upgrade 是否更新
     */
    fun installBinary(ctx: Context, resId: Int, filename: String, upgrade: Boolean): String {
        return try {
            val f = File(ctx.getDir("bin", 0), filename)
            if (f.exists()) {
                if (!upgrade) return f.canonicalPath
                f.delete()
            }
            copyRawFile(ctx, resId, f, "0755")
            f.canonicalPath
        } catch (e: Exception) {
            LogUtils.e(TAG, "installBinary failed: ".plus(e.localizedMessage))
            ""
        }
    }

    /**
     * 拷贝文件
     *
     * @param ctx   context
     * @param resid 资源文件id
     * @param file  拷贝之后的目的文件
     * @param mode  文件权限 (E.g.: "0755")
     * @throws IOException
     * @throws InterruptedException
     */
    fun copyRawFile(ctx: Context, resid: Int, file: File, mode: String) {
        val abash = file.absolutePath
        // Write the iptables binary
        val out = FileOutputStream(file)
        val source = ctx.resources.openRawResource(resid)
        val buf = ByteArray(1024)
        var len = 0
        while ({ len = source.read(buf); len }() > 0) {
            out.write(buf, 0, len)
        }
        out.close()
        source.close()
        // Change the permissions
        Runtime.getRuntime().exec("chmod $mode $abash").waitFor()
        // Runtime.getRuntime().exec("chmod 777 /storage/emulated/0/Android/data/co.akka/frei0r-1").waitFor();
    }

    /**
     * 提供外部使用
     */
    fun execFFMPEG(cmd: List<String>, sc: ShellCallback) {
        execFFMPEG(cmd, sc, File(mFfmpegBin).parentFile)
    }


    /**
     * 执行命令
     * @param cmd 命令
     * @param sc 回掉
     * @param fileExec 文件
     */
    fun execFFMPEG(cmd: List<String>, sc: ShellCallback, fileExec: File?) {

        enablePermissions()

        execProcess(cmd, sc, fileExec)
    }

    fun getBinaryPath(): String {
        return mFfmpegBin
    }

    /**
     * 赋予文件控制权限
     */
    fun enablePermissions() {
        Runtime.getRuntime().exec("chmod 700 " + mFfmpegBin)
    }

    /**
     * 处理拼接cmd命令
     * @param cmds 命令集合
     * @param sc 回掉
     * @param fileExec 执行文件
     */
    fun execProcess(cmds: List<String>, sc: ShellCallback, fileExec: File?): Int {

        //ensure that the arguments are in the correct Locale format
        cmds.forEach {
            String.format(Locale.CHINA, "%s", it)
        }

        val pb = ProcessBuilder(cmds)
        pb.directory(fileExec)

        val cmdlog = StringBuffer()

        for (cmd in cmds) {
            cmdlog.append(cmd)
            cmdlog.append(' ')
        }
        sc.shellOut(cmdlog.toString())
        //pb.redirectErrorStream(true);
        val process = pb.start()

        // any error message?
        val errorGobbler = StreamGobbler(
                process.errorStream, "ERROR", sc)

        // any output?
        val outputGobbler = StreamGobbler(process.inputStream, "OUTPUT", sc)

        ThreadPool.getPool().execute(errorGobbler)
        ThreadPool.getPool().execute(outputGobbler)

        val exitVal = process.waitFor()

        sc.processComplete(exitVal)

        return exitVal

    }

    /**
     * 解析info信息
     */
    inner class InfoParser(private val mMedia: Clip) : ShellCallback {
        private var retValue: Int = 0

        override fun shellOut(shellLine: String) {
            when {
                shellLine.contains("Duration:") -> {
                    // Duration: 00:01:01.75, start: 0.000000, bitrate: 8184 kb/s
                    val timecode = shellLine.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var duration = java.lang.Double.parseDouble(timecode[1].trim { it <= ' ' }) * 60.0 * 60.0 //hours
                    duration += java.lang.Double.parseDouble(timecode[2].trim { it <= ' ' }) * 60 //minutes
                    duration += java.lang.Double.parseDouble(timecode[3].trim { it <= ' ' }) //seconds
                    mMedia.duration = duration
                }
                shellLine.contains(": Video:") -> {
                    val line = shellLine.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val videoInfo = line[3].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    mMedia.videoCodec = videoInfo[0]
                }
                shellLine.contains(": Audio:") -> {
                    val line = shellLine.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val audioInfo = line[3].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    mMedia.audioCodec = audioInfo[0]
                }
            //Stream #0:1(eng): Audio: aac (mp4a / 0x6134706D), 48000 Hz, stereo, s16, 121 kb/s
            //Stream #0:0(eng): Video: h264 (High) (avc1 / 0x31637661), yuv420p, 1920x1080, 16939 kb/s, 30.02 fps, 30 tbr, 90k tbn, 180k tbc

            //
            //Stream #0.0(und): Video: h264 (Baseline), yuv420p, 1280x720, 8052 kb/s, 29.97 fps, 90k tbr, 90k tbn, 180k tbc
            //Stream #0.1(und): Audio: mp2, 22050 Hz, 2 channels, s16, 127 kb/s
            }//Stream #0:1(eng): Audio: aac (mp4a / 0x6134706D), 48000 Hz, stereo, s16, 121 kb/s
            //   Stream #0:0(eng): Video: h264 (High) (avc1 / 0x31637661), yuv420p, 1920x1080, 16939 kb/s, 30.02 fps, 30 tbr, 90k tbn, 180k tbc

            //
            //Stream #0.0(und): Video: h264 (Baseline), yuv420p, 1280x720, 8052 kb/s, 29.97 fps, 90k tbr, 90k tbn, 180k tbc
            //Stream #0.1(und): Audio: mp2, 22050 Hz, 2 channels, s16, 127 kb/s
        }

        override fun processComplete(exitValue: Int) {
            retValue = exitValue
        }
    }

    /**
     * 打印输出信息
     * @param source 输入流
     * @param type 流文件类型
     * @param sc 回掉
     */
    inner class StreamGobbler internal constructor(internal var source: InputStream, internal var type: String, internal var sc: ShellCallback?) : Runnable {

        override fun run() {
            try {
                val isr = InputStreamReader(source)
                val br = BufferedReader(isr)
                br.readLine().forEach {
                    if (sc != null)
                        sc!!.shellOut(it.toString())
                }
                isr.close()
                br.close()
            } catch (ioe: IOException) {
                LogUtils.e(TAG, "StreamGobbler->".plus(ioe.localizedMessage))
            }
        }
    }

}
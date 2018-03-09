package com.zed.core

import android.content.Context
import com.zed.common.util.LogUtils
import java.io.*
import java.util.*

/**
 * Created by zed on 2018/3/9.
 */
class ShellUtils {

    val TAG = this::class.simpleName

    //various console cmds
    var SHELL_CMD_CHMOD = "chmod"
    val SHELL_CMD_KILL = "kill -9"
    val SHELL_CMD_RM = "rm"
    val SHELL_CMD_PS = "ps"
    val SHELL_CMD_PIDOF = "pidof"

    val CHMOD_EXE_VALUE = "700"

    fun isRootPossible(): Boolean {
        val log = StringBuilder()
        try {
            // Check if Superuser.apk exists
            var fileSU = File("/system/app/Superuser.apk")
            if (fileSU.exists())
                return true
            fileSU = File("/system/bin/su")
            if (fileSU.exists())
                return true

            //Check for 'su' binary
            val cmd = arrayOf("which su")
            val exitCode = doShellCommand(null, cmd, object : ShellCallback {
                override fun shellOut(msg: String) {
                    //System.out.print(msg);
                }

                override fun processComplete(exitValue: Int) {
                }

            }, false, true).exitValue()

            if (exitCode == 0) {
                logMessage("Can acquire root permissions")
                return true
            }

        } catch (e: IOException) {
            //this means that there is no root to be had (normally) so we won't log anything
            logException("Error checking for root access", e)

        } catch (e: Exception) {
            logException("Error checking for root access", e)
            //this means that there is no root to be had (normally)
        }
        logMessage("Could not acquire root permissions")

        return false
    }

    /**
     * 查找进程id
     * @param command 文件路劲
     */
    fun findProcessId(command: String): Int {
        var procId = -1

        try {
            procId = findProcessIdWithPidOf(command)

            if (procId == -1)
                procId = findProcessIdWithPS(command)
        } catch (e: Exception) {
            try {
                procId = findProcessIdWithPS(command)
            } catch (e2: Exception) {
                logException("Unable to get proc id for: " + command, e2)
            }
        }
        return procId
    }

    /**
     * 查找进程id
     * @param command 文件路劲
     */
    @Throws(Exception::class)
    fun findProcessIdWithPidOf(command: String): Int {

        var procId = -1

        val r = Runtime.getRuntime()

        var procPs: Process? = null

        val baseName = File(command).name
        //fix contributed my mikos on 2010.12.10
        procPs = r.exec(arrayOf(SHELL_CMD_PIDOF, baseName))
        //procPs = r.exec(SHELL_CMD_PIDOF);

        val reader = BufferedReader(InputStreamReader(procPs!!.inputStream))

        while (true) {
            val line = reader.readLine()
            try {
                //this line should just be the process id
                procId = Integer.parseInt(line.trim { it <= ' ' })
                break
            } catch (e: NumberFormatException) {
                logException("unable to parse process pid: " + line, e)
            }
        }


        return procId

    }

    //use 'ps' command
    @Throws(Exception::class)
    fun findProcessIdWithPS(command: String): Int {

        var procId = -1

        val r = Runtime.getRuntime()

        var procPs: Process? = null

        procPs = r.exec(SHELL_CMD_PS)

        val reader = BufferedReader(InputStreamReader(procPs!!.inputStream))
        while (true) {
            val line = reader.readLine() ?: break
            if (line!!.indexOf(' ' + command) != -1) {
                val st = StringTokenizer(line, " ")
                st.nextToken() //proc owner
                procId = Integer.parseInt(st.nextToken().trim { it <= ' ' })
                break
            }
        }



        return procId

    }

    @Throws(Exception::class)
    fun doShellCommand(cmds: Array<String>, sc: ShellCallback, runAsRoot: Boolean, waitFor: Boolean): Int {
        return doShellCommand(null, cmds, sc, runAsRoot, waitFor).exitValue()

    }

    @Throws(Exception::class)
    fun doShellCommand(proc: Process?, cmds: Array<String>, sc: ShellCallback?, runAsRoot: Boolean, waitFor: Boolean): Process {
        var proc = proc

        if (proc == null) {
            if (runAsRoot)
                proc = Runtime.getRuntime().exec("su")
            else
                proc = Runtime.getRuntime().exec("sh")
        }

        val out = OutputStreamWriter(proc!!.outputStream)

        for (i in cmds.indices) {
            logMessage("executing shell cmd: " + cmds[i] + "; runAsRoot=" + runAsRoot + ";waitFor=" + waitFor)

            out.write(cmds[i])
            out.write("\n")
        }

        out.flush()
        out.write("exit\n")
        out.flush()

        if (waitFor) {

            val buf = CharArray(20)

            // Consume the "stdout"
            var reader = InputStreamReader(proc.inputStream)
            var read = 0
            while ({ read = reader.read(buf);read }() != -1) {
                sc?.shellOut(String(buf))
            }

            // Consume the "stderr"
            reader = InputStreamReader(proc.errorStream)
            read = 0
            while ({ read = reader.read(buf);read }() != -1) {
                sc?.shellOut(String(buf))
            }

            proc.waitFor()

        }
        sc!!.processComplete(proc.exitValue())
        return proc
    }

    fun logMessage(msg: String) {
        LogUtils.e(TAG, "msg:".plus(msg))
    }

    fun logException(msg: String, e: Exception) {
        LogUtils.e(TAG, "msg:".plus(msg).plus("\r\n").plus("Exception:") + e.localizedMessage)
    }
}
package utils

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Nguyen Y Nguyen on 2020/01/15.
 */

object Tools {
    fun cmd(cmd: String, params: String) {
        val cmdStr = "$cmd $params"
        if (cmdStr.isBlank()) return
        outputMessage(cmdStr)
    }

    fun isLinux(): Boolean {
        val system = System.getProperty("os.name")
        return system.startsWith("Linux")
    }

    fun isMac(): Boolean {
        val system = System.getProperty("os.name")
        return system.startsWith("Mac OS")
    }

    fun isWindows(): Boolean {
        val system = System.getProperty("os.name")
        return system.startsWith("Windows")
    }

    fun chmod(path: String) {
        outputMessage("chmod 755 -R $path")
    }

    private fun outputMessage(cmd: String) {
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
    }

    private fun isCmdExist(cmd: String): Boolean {
        val result = if (isMac() || isLinux()) {
            executeCmd("which $cmd")
        } else {
            executeCmd("where $cmd")
        }
        return !result.isNullOrEmpty()
    }

    private fun executeCmd(cmd: String): String? {
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
        val bufferReader = BufferedReader(InputStreamReader(process.inputStream))
        return try {
            bufferReader.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
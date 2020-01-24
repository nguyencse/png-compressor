package utils

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Nguyen Y Nguyen on 2020/01/16.
 */

object FileUtils {

    fun copyFile(src: File, dst: File): Boolean {
        var result = true
        var fis: InputStream? = null
        var fos: OutputStream? = null
        try {
            fis = FileInputStream(src)
            fos = FileOutputStream(dst)
            val buffer = ByteArray(1024)
            var length: Int
            while (fis.read(buffer).also { length = it } > 0) {
                fos.write(buffer, 0, length)
            }
        } catch (e: Exception) {
            result = false
        } finally {
            fis?.close()
            fos?.close()
        }
        return result
    }

    fun copyFile(srcStream: InputStream, dst: File): Boolean {
        var result = true
        var fis: InputStream? = null
        var fos: OutputStream? = null
        try {
            fis = srcStream
            fos = FileOutputStream(dst)
            val buffer = ByteArray(1024)
            var length: Int
            while (fis.read(buffer).also { length = it } > 0) {
                fos.write(buffer, 0, length)
            }
        } catch (e: Exception) {
            result = false
        } finally {
            fis?.close()
            fos?.close()
        }
        return result
    }

    fun moveFile(srcPath: String, dstPath: String) : Boolean {
        val res = Files.move(Paths.get(srcPath), Paths.get(dstPath))
        return res != null
    }
}
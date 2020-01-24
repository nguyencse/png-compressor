package helpers

import com.intellij.openapi.project.Project
import utils.Tools.isMac
import utils.Tools.isWindows

class LibHelper(private var project: Project?) {

    fun getPngQuantPath(): String {
        val rootDir = project?.basePath
        return when {
            isWindows() -> "$rootDir/pngquant.exe"
            isMac() -> "$rootDir/pngquant"
            else -> ""
        }
    }

    fun getPngQuantCmd(): String {
        return project?.basePath + "/pngquant"
    }
}
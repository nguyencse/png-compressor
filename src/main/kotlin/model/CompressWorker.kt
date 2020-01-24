package model

import commons.Constants
import helpers.LibHelper
import ui.ChooseImagePathDialog
import utils.FileUtils
import utils.Tools
import java.io.File
import javax.swing.SwingWorker

class CompressWorker() : SwingWorker<Void, Float>() {
    private lateinit var settings: Settings
    private lateinit var helper: LibHelper
    private lateinit var dialog: ChooseImagePathDialog

    constructor(helper: LibHelper, settings: Settings, dialog: ChooseImagePathDialog) : this() {
        this.helper = helper
        this.settings = settings
        this.dialog = dialog
    }

    override fun doInBackground(): Void? {
        val ins = this.javaClass.getResourceAsStream(if (Tools.isWindows()) Constants.LIB_PATH_WINDOWS else Constants.LIB_PATH_MAC)
        val libFile = File(helper.getPngQuantPath())
        val copyFileResult = FileUtils.copyFile(ins, libFile)
        ins.close()
        if (copyFileResult) {
            if (Tools.isMac() || Tools.isLinux()) {
                Tools.chmod(helper.getPngQuantPath())
            }

            for (i in 0 until settings.inputPngFiles.size) {
                val png = settings.inputPngFiles[i]
                var params = "${png.filePath} --force --skip-if-larger"
                if (settings.isCustom) {
                    params += if (settings.isDithered) "" else " --nofs"
                    params += if (settings.isIE6Support) " --iebug" else ""
                    params += " --speed ${settings.speed}"
                    params += " --quality ${settings.minColor}-${settings.maxColor}"
                }

                params += " --ext ${settings.fileNameOutput}.png"

                Tools.cmd(helper.getPngQuantCmd(), params)

                val currFilePath = "${png.fileDir}/${png.fileNameWithoutExt}${settings.fileNameOutput}.png"
                val newFile = File(currFilePath)
                png.result = "Successful"
                png.compressedSize = newFile.length() / 1024f
                png.savedSize = png.size - png.compressedSize
                png.savedSizePercent = png.savedSize / png.size * 100

                if (png.compressedSize > png.size || png.compressedSize == 0f) {
                    png.result = "Failed"
                    png.compressedSize = png.size
                    png.savedSize = 0f
                    png.savedSizePercent = 0f
                    newFile.delete()
                } else if (settings.isOtherOutputDir) {
                    val newFilePath = "${settings.otherOutputDir}/${png.fileNameWithoutExt}${settings.fileNameOutput}.png"
                    FileUtils.moveFile(currFilePath, newFilePath)
                }

                dialog.updateFileInfoAtRow(png, i)
                publish((i + 1) * 1f / settings.inputPngFiles.size)
            }
        }
        libFile.delete()
        return null
    }

    override fun process(chunks: MutableList<Float>?) {
        chunks?.let { dialog.updateProgress(it[it.size - 1]) }
    }

    override fun done() {
        dialog.compressDone()
    }
}
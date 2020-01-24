package actions

import callbacks.CompressCallback
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import helpers.LibHelper
import model.CompressWorker
import model.Settings
import ui.ChooseImagePathDialog

class PngAction : AnAction(), CompressCallback {
    private var project: Project? = null
    private lateinit var helper: LibHelper
    private lateinit var dialog: ChooseImagePathDialog

    override fun actionPerformed(e: AnActionEvent) {
        this.project = e.project
        this.helper = LibHelper(e.project)
        openPathDialog()
    }

    private fun openPathDialog() {
        dialog = ChooseImagePathDialog(this)
        dialog.pack()
        dialog.setLocationRelativeTo(null)
        dialog.isVisible = true
    }

    override fun onCompress(settings: Settings) {
        CompressWorker(helper, settings, dialog).execute()
    }
}
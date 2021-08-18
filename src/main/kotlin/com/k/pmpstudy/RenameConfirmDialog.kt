package com.k.pmpstudy

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent

class RenameConfirmDialog(private val targetFilesSize: Int) : DialogWrapper(true) {

    init {
        title = "Rename Files..."
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("$targetFilesSize Files found.") {}
        row("This refactoring may change a lot of files.") {}
    }
}
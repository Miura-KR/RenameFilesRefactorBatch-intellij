package com.k.pmpstudy

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile

class RenameFilesRefactorBatchAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = isOnDirectoryInProjectView(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        showTestMessage()
    }

    private fun isOnDirectoryInProjectView(e: AnActionEvent): Boolean {
        if (e.place == ActionPlaces.PROJECT_VIEW_POPUP) {
            val selectedFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
            return (selectedFile != null) && selectedFile.isDirectory
        }
        return false
    }

    private fun showTestMessage(test: String = "Test") {
        Messages.showMessageDialog(test, "Title", Messages.getInformationIcon())
    }
}
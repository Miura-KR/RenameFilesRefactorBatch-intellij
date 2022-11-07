package com.k.pmpstudy

import com.intellij.openapi.actionSystem.*
import com.intellij.psi.PsiElement
import com.k.pmpstudy.service.RenameFilesRefactorBatchService

class RenameFilesRefactorBatchAction : AnAction() {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = isOnDirectoryInProjectView(e)
    }

    private fun isOnDirectoryInProjectView(e: AnActionEvent): Boolean =
        (e.place == ActionPlaces.PROJECT_VIEW_POPUP)
                && (e.getData(CommonDataKeys.VIRTUAL_FILE)?.isDirectory ?: false)

    override fun actionPerformed(e: AnActionEvent) {
        val targetDir: PsiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return

        e.project?.let { RenameFilesRefactorBatchService(it).run(targetDir) }
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
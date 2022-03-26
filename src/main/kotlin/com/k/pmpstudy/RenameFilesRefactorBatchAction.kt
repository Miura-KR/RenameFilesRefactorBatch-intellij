package com.k.pmpstudy

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
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
}
package com.k.pmpstudy

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.refactoring.RefactoringFactory

class RenameFilesRefactorBatchAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = (e.project != null) && isOnDirectoryInProjectView(e)
    }

    private fun isOnDirectoryInProjectView(e: AnActionEvent): Boolean {
        if (e.place == ActionPlaces.PROJECT_VIEW_POPUP) {
            val selectedFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
            return (selectedFile != null) && selectedFile.isDirectory
        }
        return false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project

        val selectedPsiElement: PsiElement = e.getData(CommonDataKeys.PSI_ELEMENT)!!
        val child: PsiElement = selectedPsiElement.children[0]
        if (child is PsiJavaFile) {
            if (project != null) {
                renameFilesRefactor(project, child.classes[0])
            }
        }
    }

    private fun inputReplaceWord(): Map<String, String> {
        return mapOf("search" to "word1", "replace" to "word2")
    }

    private fun renameFilesRefactor(project: Project, psiClass: PsiClass) {
        val renameRefactoring = RefactoringFactory.getInstance(project).createRename(psiClass, "newName")
        renameRefactoring.run()
    }

    private fun showTestMessage(test: String = "Test") {
        Messages.showMessageDialog(test, "Title", Messages.getInformationIcon())
    }
}
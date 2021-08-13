package com.k.pmpstudy

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.refactoring.RefactoringFactory
import com.intellij.refactoring.openapi.impl.RenameRefactoringImpl

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
        val selectedDir: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)!!
        val child: VirtualFile = selectedDir.children[0]

        if (project != null) {
            val app : Application = ApplicationManager.getApplication()
            val psiFile = PsiManager.getInstance(project).findFile(child)
            if (psiFile != null) {

//                psiFile.name = "newName.java"
                renameFilesRefactor(project, psiFile, child)
            }
        }
    }

    private fun inputReplaceWord(): Map<String, String> {
        return mapOf("search" to "word1", "replace" to "word2")
    }

    private fun renameFilesRefactor(project: Project, psiFile: PsiFile, path: VirtualFile) {
        if (path.isDirectory) {
            val children: Array<VirtualFile> = path.children
        }
//        val refactoringFactory = RefactoringFactory.getInstance(project)
//        val renameRefactoring = refactoringFactory.createRename(psiFile, "newName.java")
        val renameRefactoring = RenameRefactoringImpl(project, psiFile, "newName.java", false, false)
        renameRefactoring.run()
    }

    private fun showTestMessage(test: String = "Test") {
        Messages.showMessageDialog(test, "Title", Messages.getInformationIcon())
    }
}
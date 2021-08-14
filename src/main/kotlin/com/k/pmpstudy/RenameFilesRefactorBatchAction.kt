package com.k.pmpstudy

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassOwner
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileSystemItem
import com.intellij.refactoring.RefactoringFactory

class RenameFilesRefactorBatchAction : AnAction() {
    private lateinit var refactoringFactory: RefactoringFactory

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = isOnDirectoryInProjectView(e)
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
        refactoringFactory = RefactoringFactory.getInstance(project)

        val directoryPsi: PsiElement = e.getData(CommonDataKeys.PSI_ELEMENT)!!
        renameFilesRefactor(directoryPsi)
    }

    private fun inputReplaceWord(): Map<String, String> {
        return mapOf("search" to "word1", "replace" to "word2")
    }

    private fun renameFilesRefactor(pathPsi: PsiElement) {
        if (pathPsi !is PsiFileSystemItem) return
        if (pathPsi.isDirectory) {
            pathPsi.children.forEach { renameFilesRefactor(it) }
        }
        if (pathPsi is PsiClassOwner) {
            val classes: Array<PsiClass> = pathPsi.classes
            if (classes.size == 1) {
                val name: String = classes[0].name.toString()
                refactoringFactory.createRename(classes[0], name + "new").run()
            }
        }
    }

    private fun showTestMessage(test: String = "Test") {
        Messages.showMessageDialog(test, "Title", Messages.getInformationIcon())
    }
}
package com.k.pmpstudy

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.refactoring.RefactoringFactory
import com.k.pmpstudy.dialog.RenameConfirmDialog
import com.k.pmpstudy.dialog.ReplaceWordDialog

class RenameFilesRefactorBatchAction : AnAction() {

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
        val targetDir: PsiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return

        // 検索語、置換語の取得
        val replaceWord: ReplaceWord =
            ReplaceWordDialog(targetDir as PsiDirectory).showInputReplaceWordsDialog() ?: return

        // ディレクトリ下のファイルで検索語を含むファイルリストの取得
        val targetFiles: List<PsiFile> = getTargetFiles(targetDir, replaceWord.search)
        // 件数表示と現状の保存を促す
        if (RenameConfirmDialog(targetDir, replaceWord, targetFiles.size).showAndGet()) {
            // リネーム実行
            val refactoringFactory = RefactoringFactory.getInstance(e.project)
            targetFiles.forEach { renameFileRefactor(refactoringFactory, it, replaceWord) }
        }
    }

    private fun getTargetFiles(pathPsi: PsiElement, search: String): MutableList<PsiFile> {
        val files: MutableList<PsiFile> = mutableListOf()
        if (pathPsi !is PsiFileSystemItem) return files
        if (pathPsi.isDirectory) {
            files.addAll(pathPsi.children.flatMap { getTargetFiles(it, search) })
        } else if ((pathPsi is PsiFile) && pathPsi.name.contains(search)) {
            files.add(pathPsi)
        }
        return files
    }

    private fun renameFileRefactor(
        refactoringFactory: RefactoringFactory,
        pathPsi: PsiFile,
        replaceWord: ReplaceWord
    ) {
        var name: String = pathPsi.virtualFile.name
        var newName: String = name.replace(replaceWord.search, replaceWord.replace)

        // クラスが書いてない、または複数クラスが書いてあればファイル名だけのリネーム
        if (pathPsi !is PsiClassOwner || pathPsi.classes.size != 1) {
            refactoringFactory.createRename(pathPsi, newName).run()
            return
        }

        val classes: Array<PsiClass> = pathPsi.classes
        name = classes[0].name.toString()
        newName = name.replace(replaceWord.search, replaceWord.replace)
        refactoringFactory.createRename(classes[0], newName).run()
    }
}
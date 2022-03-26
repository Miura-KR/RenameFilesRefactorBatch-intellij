package com.k.pmpstudy.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.refactoring.RefactoringFactory
import com.k.pmpstudy.dialog.RenameConfirmDialog
import com.k.pmpstudy.dialog.ReplaceWordDialog
import com.k.pmpstudy.domain.ReplaceWord

class RenameFilesRefactorBatchService(private val project: Project) {
    fun run(dir: PsiElement) {
        // 検索語、置換語の取得
        val replaceWord: ReplaceWord =
            ReplaceWordDialog(dir as PsiDirectory).showInputReplaceWordsDialog() ?: return

        // ディレクトリ下のファイルで検索語を含むファイルリストの取得
        val targetFiles: List<PsiFile> = getTargetFiles(dir, replaceWord.search)
        if (targetFiles.isEmpty()) {
            Messages.showMessageDialog(
                "File not found",
                "Search word : ${replaceWord.search}",
                Messages.getInformationIcon()
            )
            return
        }

        // 件数表示と現状の保存を促す
        if (RenameConfirmDialog(dir, replaceWord, targetFiles.size).showAndGet()) {
            // リネーム実行
            val refactoringFactory = RefactoringFactory.getInstance(project)
            targetFiles.forEach { renameFileRefactor(refactoringFactory, it, replaceWord) }
        }
    }

    private fun getTargetFiles(pathPsi: PsiElement, search: String): List<PsiFile> =
        if (pathPsi !is PsiFileSystemItem)
            listOf()
        else if (pathPsi.isDirectory)
            pathPsi.children.flatMap { getTargetFiles(it, search) }
        else if ((pathPsi is PsiFile) && pathPsi.name.contains(search))
            listOf(pathPsi)
        else
            listOf()

    private fun renameFileRefactor(
        refactoringFactory: RefactoringFactory,
        pathPsi: PsiFile,
        replaceWord: ReplaceWord
    ) {
        var name: String = pathPsi.virtualFile.name
        var newName: String = name.replace(replaceWord.search, replaceWord.replace)

        if (isToRenameClassName(pathPsi, name)) {
            val classes: Array<PsiClass> = (pathPsi as PsiClassOwner).classes
            name = classes[0].name.toString()
            newName = name.replace(replaceWord.search, replaceWord.replace)
            refactoringFactory.createRename(classes[0], newName).run()
            return
        }

        refactoringFactory.createRename(pathPsi, newName).run()
    }

    private fun isToRenameClassName(pathPsi: PsiFile, fileName: String): Boolean =
        pathPsi is PsiClassOwner
                && pathPsi.classes.size == 1
                && fileName.split(".").dropLast(1).joinToString() == pathPsi.classes[0].name  // クラス名とファイル名が一致
}
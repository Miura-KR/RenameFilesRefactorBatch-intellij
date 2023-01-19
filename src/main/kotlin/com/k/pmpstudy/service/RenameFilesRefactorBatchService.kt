package com.k.pmpstudy.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.psi.xml.XmlFile
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
        val targetFiles: List<PsiFile> =
            if (replaceWord.isRegex) getTargetFilesRegex(dir, replaceWord.search)
            else getTargetFiles(dir, replaceWord.search)
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
        getTargetFilesByPsiFilePredicate(pathPsi, search) { it.name.contains(search) }

    private fun getTargetFilesRegex(pathPsi: PsiElement, search: String): List<PsiFile> =
        getTargetFilesByPsiFilePredicate(pathPsi, search) { it.name.contains(Regex(search)) }

    private fun getTargetFilesByPsiFilePredicate(
        pathPsi: PsiElement,
        search: String,
        predicate: (PsiFile) -> Boolean
    ): List<PsiFile> =
        if (pathPsi !is PsiFileSystemItem)
            listOf()
        else if (pathPsi.isDirectory)
            pathPsi.children.flatMap { getTargetFilesByPsiFilePredicate(it, search, predicate) }
        else if ((pathPsi is PsiFile) && predicate.invoke(pathPsi))
            listOf(pathPsi)
        else
            listOf()

    private fun renameFileRefactor(
        refactoringFactory: RefactoringFactory,
        pathPsi: PsiFile,
        replaceWord: ReplaceWord
    ) {
        val name: String = pathPsi.virtualFile.name

        if (isToRenameClassName(pathPsi, name)) {
            val classes: Array<PsiClass> = (pathPsi as PsiClassOwner).classes
            val className = classes[0].name.toString()
            val newClassName = getNewName(replaceWord, className)
            refactoringFactory.createRename(classes[0], newClassName).run()
            return
        }

        var newName: String = getNewName(replaceWord, name)

        if (pathPsi is XmlFile || pathPsi is PsiBinaryFile) {
            newName = newName.split(".").dropLast(1).joinToString()
        }
        refactoringFactory.createRename(pathPsi, newName).run()
    }

    private fun isToRenameClassName(pathPsi: PsiFile, fileName: String): Boolean =
        pathPsi is PsiClassOwner
                && pathPsi.classes.size == 1
                && fileName.split(".").dropLast(1).joinToString() == pathPsi.classes[0].name  // クラス名とファイル名が一致

    private fun getNewName(replaceWord: ReplaceWord, srcName: String): String =
        if (replaceWord.isRegex) srcName.replace(Regex(replaceWord.search), replaceWord.replace)
        else srcName.replace(replaceWord.search, replaceWord.replace)
}
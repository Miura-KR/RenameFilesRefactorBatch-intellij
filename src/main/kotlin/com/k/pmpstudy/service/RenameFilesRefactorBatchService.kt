package com.k.pmpstudy.service

import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.RefactoringFactory
import com.k.pmpstudy.dialog.RenameConfirmDialog
import com.k.pmpstudy.dialog.ReplaceWordDialog
import com.k.pmpstudy.domain.ReplaceInfo
import java.io.IOException

class RenameFilesRefactorBatchService(private val project: Project) {
    fun run(dir: PsiElement) {
        // 検索語、置換語の取得
        val replaceInfo: ReplaceInfo =
            ReplaceWordDialog(dir as PsiDirectory).showInputReplaceWordsDialog() ?: return

        // ディレクトリ下のファイルで検索語を含むファイルリストの取得
        val targetFiles: List<PsiFile> =
            if (replaceInfo.isRegex) getTargetFilesRegex(dir, replaceInfo.search)
            else getTargetFiles(dir, replaceInfo.search)
        if (targetFiles.isEmpty()) {
            Messages.showMessageDialog(
                "File not found",
                "Search word : ${replaceInfo.search}",
                Messages.getInformationIcon()
            )
            return
        }

        // 件数表示と現状の保存を促す
        if (!RenameConfirmDialog(dir, replaceInfo, targetFiles.size).showAndGet()) {
            return
        }

        if (replaceInfo.useRefactor) {
            // リネーム実行（1回のUndoで全件を元に戻せるよう、単一のコマンドにまとめる）
            val refactoringFactory = RefactoringFactory.getInstance(project)
            CommandProcessor.getInstance().executeCommand(
                project,
                { targetFiles.forEach { renameFileRefactor(refactoringFactory, it, replaceInfo) } },
                "Bulk Rename",
                null
            )
            return
        }

        // リネーム実行（1回のUndoで全件を元に戻せるよう、単一の書き込みコマンドにまとめる）
        val errorMessages = mutableListOf<String>()
        WriteCommandAction.runWriteCommandAction(
            project,
            "Bulk Rename",
            null,
            {
                targetFiles.forEach {
                    val vFile = it.virtualFile
                    val newFileName = getNewName(replaceInfo, it.name)
                    if (vFile.parent?.findChild(newFileName) != null) {
                        errorMessages += "$newFileName is already exist."
                        return@forEach
                    }
                    try {
                        vFile.rename(this, newFileName)
                    } catch (e: IOException) {
                        errorMessages += "Rename ${it.name} to $newFileName."
                    }
                }
            }
        )
        errorMessages.forEach {
            Messages.showMessageDialog(it, "Rename Error", Messages.getInformationIcon())
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
        replaceInfo: ReplaceInfo
    ) {
        val name: String = pathPsi.virtualFile.name

        if (isToRenameClassName(pathPsi, name)) {
            val classes: Array<PsiClass> = (pathPsi as PsiClassOwner).classes
            val className = classes[0].name.toString()
            val newClassName = getNewName(replaceInfo, className)
            refactoringFactory.createRename(classes[0], newClassName).run()
            return
        }

        var newName: String = getNewName(replaceInfo, name)

        if (pathPsi is XmlFile || pathPsi is PsiBinaryFile) {
            newName = newName.split(".").dropLast(1).joinToString()
        }
        refactoringFactory.createRename(pathPsi, newName).run()
    }

    private fun isToRenameClassName(pathPsi: PsiFile, fileName: String): Boolean =
        pathPsi is PsiClassOwner
                && pathPsi.classes.size == 1
                && fileName.split(".").dropLast(1).joinToString() == pathPsi.classes[0].name  // クラス名とファイル名が一致

    private fun getNewName(replaceInfo: ReplaceInfo, srcName: String): String =
        if (replaceInfo.isRegex) srcName.replace(Regex(replaceInfo.search), replaceInfo.replace)
        else srcName.replace(replaceInfo.search, replaceInfo.replace)
}
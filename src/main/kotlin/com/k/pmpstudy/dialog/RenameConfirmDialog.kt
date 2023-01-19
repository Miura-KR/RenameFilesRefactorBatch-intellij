package com.k.pmpstudy.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel
import com.k.pmpstudy.domain.ReplaceWord
import javax.swing.JComponent

class RenameConfirmDialog(
    private val targetDir: PsiDirectory,
    private val replaceWord: ReplaceWord,
    private val targetFilesSize: Int
) : DialogWrapper(true) {

    init {
        title = "Rename Files..."
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row {
            label("TargetDirectory")
            label(targetDir.virtualFile.path)
        }.layout(RowLayout.PARENT_GRID)
        row {
            label("Use refactor rename")
            label(if (replaceWord.isRegex) "Yes" else "No")
        }.layout(RowLayout.PARENT_GRID)
        row {
            label("Use regular expression")
            label(if (replaceWord.isRegex) "Yes" else "No")
        }.layout(RowLayout.PARENT_GRID)
        row {
            label("Search word")
            label(replaceWord.search)
        }.layout(RowLayout.PARENT_GRID)
        row {
            label("Replace word")
            label(replaceWord.replace)
        }.layout(RowLayout.PARENT_GRID)
        row {
            label("Found files count")
            label("$targetFilesSize")
        }.layout(RowLayout.PARENT_GRID)
        row("\nThis refactoring will probably change a lot of files.") {}.layout(RowLayout.PARENT_GRID)
    }
}
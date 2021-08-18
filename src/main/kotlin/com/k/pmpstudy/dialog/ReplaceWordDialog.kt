package com.k.pmpstudy.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.layout.panel
import com.k.pmpstudy.ReplaceWord
import javax.swing.JComponent

class ReplaceWordDialog(private val targetDir: PsiDirectory) : DialogWrapper(true) {
    private var searchWord: String = ""
    private var replaceWord: String = ""

    init {
        title = "Rename Files..."
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("TargetDirectory : ${targetDir.virtualFile.path}") {}
        row("Please input replace words.") {}
        row("Search word") {
            textField(::searchWord, { searchWord = it })
                .focused()
                .withValidationOnApply {
                    if (it.text.isEmpty()) error("Need a search word.")
                    else null
                }
        }
        row("Replace word") {
            textField(::replaceWord, { replaceWord = it })
        }
    }

    fun showInputReplaceWordsDialog(): ReplaceWord? =
        if (showAndGet()) ReplaceWord(searchWord, replaceWord)
        else null
}
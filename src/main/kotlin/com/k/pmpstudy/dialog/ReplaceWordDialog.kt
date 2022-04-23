package com.k.pmpstudy.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.k.pmpstudy.domain.ReplaceWord
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
            textField()
                .bindText(::searchWord) { searchWord = it }
                .focused()
                .validationOnApply {
                    if (it.text.isEmpty()) error("Specify the search word.")
                    else null
                }
        }
        row("Replace word") {
            textField().bindText(::replaceWord) { replaceWord = it }
        }
    }

    fun showInputReplaceWordsDialog(): ReplaceWord? =
        if (showAndGet()) ReplaceWord(searchWord, replaceWord)
        else null
}
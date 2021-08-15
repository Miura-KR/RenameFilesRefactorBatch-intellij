package com.k.pmpstudy

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent

class ReplaceWordDialog : DialogWrapper(true) {
    private var searchWord: String = ""
    private var replaceWord: String = ""

    init {
        title = "Rename Files..."
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Input replace words.") {}
        row("Search word") {
            textField(::searchWord, { searchWord = it })
                .focused()
                .withValidationOnApply {
                    if (it.text.isEmpty()) error("Need a search word.")
                    else if (it.text.indexOf(' ') != -1) error("Can't use spaces.")
                    else null
                }
        }
        row("Replace word") {
            textField(::replaceWord, { replaceWord = it })
                .withValidationOnApply {
                    if (it.text.indexOf(' ') != -1) error("Can't use spaces.")
                    else null
                }
        }
    }

    fun showInputReplaceWordsDialog(): ReplaceWord? =
        if (showAndGet()) ReplaceWord(searchWord, replaceWord)
        else null
}
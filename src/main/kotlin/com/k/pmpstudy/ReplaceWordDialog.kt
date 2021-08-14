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
            textField(::searchWord, { searchWord = it }).focused()
        }
        row("Replace word") {
            textField(::replaceWord, { replaceWord = it })
        }
    }

    fun showInputReplaceWordsDialog(): ReplaceWord? {
        if (showAndGet()) {
            return ReplaceWord(searchWord, replaceWord)
        }
        return null
    }
}
package com.k.pmpstudy.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.k.pmpstudy.domain.ReplaceInfo
import javax.swing.JComponent

class ReplaceWordDialog(private val targetDir: PsiDirectory) : DialogWrapper(true) {
    private var searchWord = ""
    private var replaceWord = ""
    private var regexCheck = false
    private var refactorCheck = true
    private lateinit var refactorCheckBox: Cell<JBCheckBox>
    private lateinit var regexCheckBox: Cell<JBCheckBox>

    init {
        title = "Rename Files..."
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("TargetDirectory : ${targetDir.virtualFile.path}") {}
        row("Please input replace words.") {}
        group {
            row { refactorCheckBox = checkBox("Use refactor").bindSelected(::refactorCheck) }
            row { regexCheckBox = checkBox("Use regular expression").bindSelected(::regexCheck) }
            row("Search word") {
                textField().bindText(::searchWord).focused().validationOnApply {
                    if (it.text.isEmpty()) error(EMPTY_ERROR_MSG)
                    else if (
                        refactorCheckBox.selected.invoke()
                        && ((if (regexCheckBox.selected.invoke()) """\.""" else ".") in it.text)
                    ) {
                        error(DOT_ERROR_MSG)
                    } else null
                }
            }
            row("Replace word") {
                textField().bindText(::replaceWord).validationOnApply {
                    if (refactorCheckBox.selected.invoke() && ("." in it.text)) error(DOT_ERROR_MSG)
                    else null
                }
            }
        }
    }

    fun showInputReplaceWordsDialog(): ReplaceInfo? =
        if (showAndGet()) ReplaceInfo(searchWord, replaceWord, refactorCheck, regexCheck)
        else null

    companion object {
        const val EMPTY_ERROR_MSG = "Specify the search word."
        const val DOT_ERROR_MSG = "Can't use dot character for rename refactor."
    }
}
package com.k.pmpstudy.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.k.pmpstudy.domain.ReplaceWord
import javax.swing.GroupLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class RenameConfirmDialog(
    private val targetDir: PsiDirectory,
    private val replaceWord: ReplaceWord,
    private val targetFilesSize: Int
) : DialogWrapper(true) {

    init {
        title = "Rename Files..."
        init()
    }

    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel()
        val dirLabel = JLabel("TargetDirectory")
        val dirPath = JLabel(targetDir.virtualFile.path)
        val searchLabel = JLabel("Search word")
        val search = JLabel(replaceWord.search)
        val replaceLabel = JLabel("Replace word")
        val replace = JLabel(replaceWord.replace)
        val fileSizeLabel = JLabel("Found files count")
        val fileSize = JLabel("$targetFilesSize")
        val confirm = JLabel("This refactoring may change a lot of files.")
        val layout = GroupLayout(dialogPanel)
        layout.autoCreateGaps = true
        layout.autoCreateContainerGaps = true

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(dirLabel)
                                .addComponent(searchLabel)
                                .addComponent(replaceLabel)
                                .addComponent(fileSizeLabel)
                        )
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(dirPath)
                                .addComponent(search)
                                .addComponent(replace)
                                .addComponent(fileSize)
                        )
                )
                .addComponent(confirm, GroupLayout.PREFERRED_SIZE, 500, 500)
        )
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dirLabel)
                        .addComponent(dirPath)
                )
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(searchLabel)
                        .addComponent(search)
                )
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(replaceLabel)
                        .addComponent(replace)
                )
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fileSizeLabel)
                        .addComponent(fileSize)
                )
                .addComponent(confirm, GroupLayout.PREFERRED_SIZE, 50, 100)
        )
        dialogPanel.layout = layout
        return dialogPanel
    }
}
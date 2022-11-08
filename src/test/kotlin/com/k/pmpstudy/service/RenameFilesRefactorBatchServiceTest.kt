package com.k.pmpstudy.service

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RenameFilesRefactorBatchServiceTest : BasePlatformTestCase() {
    private val TEST_SOURCE_PATH = "testdata"
//    private val sut = RenameFilesRefactorBatchService(project)

    @Test
    @DisplayName("クラス1つだけ書かれたファイル")
    fun renameTest() {
//        setUp()
//        val sut = RenameFilesRefactorBatchService(project)
//
        @Language("JAVA")
        val oneClass = "public class X{ int xxx; } //comment"
        val psiFile = myFixture.addFileToProject("${TEST_SOURCE_PATH}/X.java", oneClass)
//        val psiFile2 = createLightFile("${TEST_SOURCE_PATH}/X.java", JavaLanguage.INSTANCE, oneClass)
        assertEquals(2, 1 + 1)

    }
}
package com.k.pmpstudy.domain

data class ReplaceInfo(
    val search: String,
    val replace: String,
    val useRefactor: Boolean,
    val isRegex: Boolean
)
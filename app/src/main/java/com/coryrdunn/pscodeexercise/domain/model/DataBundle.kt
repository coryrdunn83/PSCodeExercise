package com.coryrdunn.pscodeexercise.domain.model

data class DataBundle(
    val drivers: List<String> = listOf(),
    val shipments: List<String> = listOf()
)
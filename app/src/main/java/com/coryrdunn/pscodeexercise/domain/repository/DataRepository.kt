package com.coryrdunn.pscodeexercise.domain.repository

import com.coryrdunn.pscodeexercise.domain.model.DataBundle
import kotlinx.coroutines.flow.StateFlow

interface DataRepository {
    val dataFlow: StateFlow<DataBundle?>

    suspend fun getData()
}
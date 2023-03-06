package com.coryrdunn.pscodeexercise.domain.repository

import com.coryrdunn.pscodeexercise.data.model.DataBundle

interface DataRepository {

    suspend fun getData(): DataBundle
}
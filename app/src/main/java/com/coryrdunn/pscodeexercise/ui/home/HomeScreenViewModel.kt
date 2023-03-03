package com.coryrdunn.pscodeexercise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coryrdunn.pscodeexercise.domain.model.DataBundle
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    dataRepository: DataRepository
): ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeScreenUiState> = dataRepository.dataFlow.mapLatest { bundle ->
        HomeScreenUiState(dataBundle = bundle)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = HomeScreenUiState()
        )

}

data class HomeScreenUiState(
    val dataBundle: DataBundle? = null
)
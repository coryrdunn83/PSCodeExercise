package com.coryrdunn.pscodeexercise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coryrdunn.pscodeexercise.domain.model.DataBundle
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    dataRepository: DataRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataRepository.dataFlow.collectLatest { data ->
                _uiState.update { currentState ->
                    currentState.copy(dataBundle = data)
                }
            }
        }
    }

    fun toggleDialog(driver: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                showDialog = !currentState.showDialog,
                selectedDriver = driver
            )
        }
    }

}

data class HomeScreenUiState(
    val dataBundle: DataBundle? = null,
    val showDialog: Boolean = false,
    val selectedDriver: String? = null
) {
    val driverList: List<String>
        get() = dataBundle?.drivers ?: emptyList()

    val shipmentList: List<String>
        get() = dataBundle?.shipments ?: emptyList()

    val selectedDriverShipment: String?
        get() {
            val ssScoreMap = mutableMapOf<Int, String>()

            shipmentList.forEach {
                
            }

            return dataBundle?.shipments?.firstOrNull()
        }
}
package com.coryrdunn.pscodeexercise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coryrdunn.pscodeexercise.domain.model.DriverWithShipment
import com.coryrdunn.pscodeexercise.domain.use_case.GetDriversWithShipmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getDriversWithShipmentsUseCase: GetDriversWithShipmentsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        getDriversWithShipments()
    }

    private fun getDriversWithShipments() {
        viewModelScope.launch {
            getDriversWithShipmentsUseCase().collectLatest { result ->
                _uiState.update { currentState ->
                    currentState.copy(driversWithShipmentsList = result)
                }
            }
        }
    }

    fun toggleDialog(driver: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                showDialog = !currentState.showDialog,
                selectedDriver = currentState.driversWithShipmentsList.firstOrNull { it.driver == driver }
            )
        }
    }

}

data class HomeScreenUiState(
    val driversWithShipmentsList: List<DriverWithShipment> = emptyList(),
    val showDialog: Boolean = false,
    val selectedDriver: DriverWithShipment? = null
)
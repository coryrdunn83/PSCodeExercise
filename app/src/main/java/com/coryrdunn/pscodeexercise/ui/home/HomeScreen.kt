package com.coryrdunn.pscodeexercise.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coryrdunn.pscodeexercise.R

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState: HomeScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleDialog() },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.home_dialog_accept_btm_text),
                    modifier = Modifier.clickable {
                        viewModel.toggleDialog()
                    }
                )
            },
            title = {
                Text(text = uiState.selectedDriver?.shipment ?: "")
            },
            text = {
                Text(text = stringResource(id = R.string.home_dialog_question_text))
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        content = {
            if (uiState.driverList.isNotEmpty()) {
                items(uiState.driverList) { driver ->
                    DriverCard(name = driver) {
                        viewModel.toggleDialog(driver)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    )
}
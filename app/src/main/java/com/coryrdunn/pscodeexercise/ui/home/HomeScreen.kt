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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
                    text = "Accept",
                    modifier = Modifier.clickable {
                        viewModel.toggleDialog()
                    }
                )
            },
            title = {
                Text(text = uiState.selectedDriverShipment ?: "Shipment")
            },
            text = {
                Text(text = "Would you like to accept this shipment?")
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        content = {
            uiState.dataBundle?.drivers?.let {
                items(it) { driver ->
                    DriverCard(name = driver) {
                        viewModel.toggleDialog()
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            } ?: item {
                Text(text = "No drivers available")
            }
        }
    )
}
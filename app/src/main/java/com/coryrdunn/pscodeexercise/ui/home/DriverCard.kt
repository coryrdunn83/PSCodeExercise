package com.coryrdunn.pscodeexercise.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coryrdunn.pscodeexercise.R
import com.coryrdunn.pscodeexercise.ui.theme.PSLightBlue
import com.coryrdunn.pscodeexercise.ui.theme.PSTextBlue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DriverCard(
    name: String = "",
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = stringResource(id = R.string.home_driver_card_img_desc),
                tint = PSLightBlue,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = name,
                color = PSTextBlue,
                fontSize = 18.sp
            )
        }
    }
}
package com.jagl.exchangeapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.jagl.exchangeapp.R

/**
 * Componente para ingresar el monto a convertir
 */
@Composable
fun AmountInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->

            val isValidNumber = newValue.matches(Regex("^\\d*\\.?\\d*$")) // Only allow numbers and one decimal point
            val hasValidLength = newValue.length <= 30 // Limit the length of the input to prevent overflow issues
            if (isValidNumber && hasValidLength) {
                onValueChange(newValue)
            }
        },
        label = { Text(stringResource(R.string.amount)) },
        placeholder = { Text(stringResource(R.string.enter_amount)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}
package com.jagl.exchangeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jagl.domain.model.Currency

@Composable
fun SearchableCurrencyDropdown(
    currencies: List<Currency>,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val filteredCurrencies = currencies.filter {
        it.code.contains(searchQuery.value, ignoreCase = true) ||
                it.name.contains(searchQuery.value, ignoreCase = true)
    }

    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = {
            searchQuery.value = it
            expanded.value = true
        },
        label = { Text("Buscar moneda") },
        placeholder = { Text("Ingrese código o nombre") },
        isError = searchQuery.value.isNotEmpty() && filteredCurrencies.isEmpty(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions.Default,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Red,
            unfocusedIndicatorColor = Color.Gray,
            errorIndicatorColor = Color.Red,
            errorLabelColor = Color.Red
        )
    )

    if (expanded.value && searchQuery.value.isNotEmpty() && filteredCurrencies.isNotEmpty()) {
        filteredCurrencies.forEach { currency ->
            Text(
                text = "${currency.code} - ${currency.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        searchQuery.value = currency.code
                        expanded.value = false
                        onCurrencySelected(currency)
                    }
            )
        }
    } else if (searchQuery.value.isNotEmpty() && filteredCurrencies.isEmpty()) {
        Text(
            text = "No se encontraron coincidencias",
            color = Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    }
}
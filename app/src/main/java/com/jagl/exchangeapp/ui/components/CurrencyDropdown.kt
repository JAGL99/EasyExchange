package com.jagl.exchangeapp.ui.components
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jagl.exchangeapp.data.model.Currency
import androidx.compose.foundation.clickable

@Composable
fun SearchableCurrencyDropdown(
    selectedCurrency: Currency?,
    currencies: List<Currency>,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery = remember { mutableStateOf("") }
    val filteredCurrencies = currencies.filter {
        it.code.contains(searchQuery.value, ignoreCase = true) ||
        it.name.contains(searchQuery.value, ignoreCase = true)
    }

    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { searchQuery.value = it },
        label = { Text("Buscar moneda") },
        placeholder = { Text("Ingrese código o nombre") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions.Default,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    filteredCurrencies.forEach { currency ->
        Text(
            text = "${currency.code} - ${currency.name}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onCurrencySelected(currency) }
        )
    }
}
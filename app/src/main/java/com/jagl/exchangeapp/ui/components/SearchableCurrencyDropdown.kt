package com.jagl.exchangeapp.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jagl.domain.model.Currency
import com.jagl.exchangeapp.R
import com.jagl.core.util.UiUtils

@Composable
fun SearchableCurrencyDropdown(
    avableCurrencies: List<Currency>,
    onCurrencySelected: (Currency) -> Unit,
    currencySelected: Currency? = null,
    modifier: Modifier = Modifier
) {
    val searchQuery = remember { mutableStateOf(currencySelected?.code.orEmpty()) }
    LaunchedEffect(currencySelected) {
        searchQuery.value = currencySelected?.code.orEmpty()
    }
    val expanded = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val filteredCurrencies = avableCurrencies.filter {
        it.code.contains(searchQuery.value, ignoreCase = true) ||
                it.name.contains(searchQuery.value, ignoreCase = true)
    }

    val isError = searchQuery.value.isNotEmpty() && filteredCurrencies.isEmpty()
    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = {
            UiUtils.requestFocusByContent(it, focusRequester)
            searchQuery.value = it
            expanded.value = true
        },
        singleLine = true,
        label = { Text("Buscar moneda") },
        placeholder = { Text("Ingrese código o nombre") },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions.Default,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .padding(bottom = 4.dp)
            .then(modifier),
        colors = TextFieldDefaults.colors(
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
                        focusRequester.freeFocus()
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


@Preview(showBackground = true)
@Composable
private fun PreviewSearchableCurrencyDropdown() {
    val avableCurrencies = listOf(
        Currency("USD", "Dólar estadounidense"),
        Currency("EUR", "Euro"),
        Currency("JPY", "Yen japonés"),
        Currency("GBP", "Libra esterlina"),
        Currency("AUD", "Dólar australiano"),
        Currency("CAD", "Dólar canadiense"),
        Currency("CHF", "Franco suizo"),
        Currency("CNY", "Yuan chino"),
        Currency("SEK", "Corona sueca"),
        Currency("NZD", "Dólar neozelandés")
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.from),
            style = MaterialTheme.typography.titleMedium
        )
        SearchableCurrencyDropdown(
            avableCurrencies = avableCurrencies,
            currencySelected = avableCurrencies.firstOrNull(),
            onCurrencySelected = {
                Log.d("JAGL", "currency selected:$it")
            }
        )

        Text(
            text = stringResource(R.string.to),
            style = MaterialTheme.typography.titleMedium
        )
        SearchableCurrencyDropdown(
            avableCurrencies = avableCurrencies,
            onCurrencySelected = {
                Log.d("JAGL", "currency selected:$it")
            }
        )
    }


}
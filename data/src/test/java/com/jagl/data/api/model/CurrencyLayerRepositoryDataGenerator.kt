package com.jagl.data.api.model

import com.jagl.data.api.model.GetLatestRates.Error
import com.jagl.domain.model.Currency
import java.time.Instant
import java.util.Date
import kotlin.random.Random


fun getCurrencies(): List<Currency> {
    return listOf(
        Currency("AED", "United Arab Emirates Dirham"),
        Currency("AFN", "Afghan Afghani"),
        Currency("ALL", "Albanian Lek"),
        Currency("AMD", "Armenian Dram"),
        Currency("ANG", "Netherlands Antillean Guilder"),
        Currency("AOA", "Angolan Kwanza"),
        Currency("ARS", "Argentine Peso"),
        Currency("AUD", "Australian Dollar"),
        Currency("AWG", "Aruban Florin"),
        Currency("AZN", "Azerbaijani Manat"),
        Currency("BAM", "Bosnia-Herzegovina Convertible Mark"),
        Currency("BBD", "Barbadian Dollar"),
        Currency("BDT", "Bangladeshi Taka"),
        Currency("BGN", "Bulgarian Lev"),
        Currency("BHD", "Bahraini Dinar"),
        Currency("BIF", "Burundian Franc"),
        Currency("BMD", "Bermudan Dollar"),
        Currency("BND", "Brunei Dollar"),
        Currency("BOB", "Bolivian Boliviano"),
        Currency("BRL", "Brazilian Real"),
        Currency("BSD", "Bahamian Dollar"),
        Currency("BTC", "Bitcoin"),
        Currency("BTN", "Bhutanese Ngultrum"),
        Currency("BWP", "Botswanan Pula"),
        Currency("BYN", "New Belarusian Ruble"),
        Currency("BYR", "Belarusian Ruble"),
        Currency("BZD", "Belize Dollar"),
        Currency("CAD", "Canadian Dollar"),
        Currency("CDF", "Congolese Franc"),
        Currency("CHF", "Swiss Franc"),
        Currency("CLF", "Chilean Unit of Account (UF)"),
        Currency("CLP", "Chilean Peso"),
        Currency("CNY", "Chinese Yuan"),
        Currency("CNH", "Chinese Yuan Offshore"),
        Currency("COP", "Colombian Peso"),
        Currency("CRC", "Costa Rican Colón"),
        Currency("CUC", "Cuban Convertible Peso"),
        Currency("CUP", "Cuban Peso"),
        Currency("CVE", "Cape Verdean Escudo"),
        Currency("CZK", "Czech Republic Koruna"),
        Currency("DJF", "Djiboutian Franc"),
        Currency("DKK", "Danish Krone"),
        Currency("DOP", "Dominican Peso"),
        Currency("DZD", "Algerian Dinar"),
        Currency("EGP", "Egyptian Pound"),
        Currency("ERN", "Eritrean Nakfa"),
        Currency("ETB", "Ethiopian Birr"),
        Currency("EUR", "Euro"),
        Currency("FJD", "Fijian Dollar"),
        Currency("FKP", "Falkland Islands Pound"),
        Currency("GBP", "British Pound Sterling"),
        Currency("GEL", "Georgian Lari"),
        Currency("GGP", "Guernsey Pound"),
        Currency("GHS", "Ghanaian Cedi"),
        Currency("GIP", "Gibraltar Pound"),
        Currency("GMD", "Gambian Dalasi"),
        Currency("GNF", "Guinean Franc"),
        Currency("GTQ", "Guatemalan Quetzal"),
        Currency("GYD", "Guyanaese Dollar"),
        Currency("HKD", "Hong Kong Dollar"),
        Currency("HNL", "Honduran Lempira"),
        Currency("HRK", "Croatian Kuna"),
        Currency("HTG", "Haitian Gourde"),
        Currency("HUF", "Hungarian Forint"),
        Currency("IDR", "Indonesian Rupiah"),
        Currency("ILS", "Israeli New Sheqel"),
        Currency("IMP", "Manx pound"),
        Currency("INR", "Indian Rupee"),
        Currency("IQD", "Iraqi Dinar"),
        Currency("IRR", "Iranian Rial"),
        Currency("ISK", "Icelandic Króna"),
        Currency("JEP", "Jersey Pound"),
        Currency("JMD", "Jamaican Dollar"),
        Currency("JOD", "Jordanian Dinar"),
        Currency("JPY", "Japanese Yen"),
        Currency("KES", "Kenyan Shilling"),
        Currency("KGS", "Kyrgystani Som"),
        Currency("KHR", "Cambodian Riel"),
        Currency("KMF", "Comorian Franc"),
        Currency("KPW", "North Korean Won"),
        Currency("KRW", "South Korean Won"),
        Currency("KWD", "Kuwaiti Dinar"),
        Currency("KYD", "Cayman Islands Dollar"),
        Currency("KZT", "Kazakhstani Tenge"),
        Currency("LAK", "Laotian Kip"),
        Currency("LBP", "Lebanese Pound"),
        Currency("LKR", "Sri Lankan Rupee"),
        Currency("LRD", "Liberian Dollar"),
        Currency("LSL", "Lesotho Loti"),
        Currency("LTL", "Lithuanian Litas"),
        Currency("LVL", "Latvian Lats"),
        Currency("LYD", "Libyan Dinar"),
        Currency("MAD", "Moroccan Dirham"),
        Currency("MDL", "Moldovan Leu"),
        Currency("MGA", "Malagasy Ariary"),
        Currency("MKD", "Macedonian Denar"),
        Currency("MMK", "Myanma Kyat"),
        Currency("MNT", "Mongolian Tugrik"),
        Currency("MOP", "Macanese Pataca"),
        Currency("MRU", "Mauritanian Ouguiya"),
        Currency("MUR", "Mauritian Rupee"),
        Currency("MVR", "Maldivian Rufiyaa"),
        Currency("MWK", "Malawian Kwacha"),
        Currency("MXN", "Mexican Peso"),
        Currency("MYR", "Malaysian Ringgit"),
        Currency("MZN", "Mozambican Metical"),
        Currency("NAD", "Namibian Dollar"),
        Currency("NGN", "Nigerian Naira"),
        Currency("NIO", "Nicaraguan Córdoba"),
        Currency("NOK", "Norwegian Krone"),
        Currency("NPR", "Nepalese Rupee"),
        Currency("NZD", "New Zealand Dollar"),
        Currency("OMR", "Omani Rial"),
        Currency("PAB", "Panamanian Balboa"),
        Currency("PEN", "Peruvian Nuevo Sol"),
        Currency("PGK", "Papua New Guinean Kina"),
        Currency("PHP", "Philippine Peso"),
        Currency("PKR", "Pakistani Rupee"),
        Currency("PLN", "Polish Zloty"),
        Currency("PYG", "Paraguayan Guarani"),
        Currency("QAR", "Qatari Rial"),
        Currency("RON", "Romanian Leu"),
        Currency("RSD", "Serbian Dinar"),
        Currency("RUB", "Russian Ruble"),
        Currency("RWF", "Rwandan Franc"),
        Currency("SAR", "Saudi Riyal"),
        Currency("SBD", "Solomon Islands Dollar"),
        Currency("SCR", "Seychellois Rupee"),
        Currency("SDG", "South Sudanese Pound"),
        Currency("SEK", "Swedish Krona"),
        Currency("SGD", "Singapore Dollar"),
        Currency("SHP", "Saint Helena Pound"),
        Currency("SLE", "Sierra Leonean Leone"),
        Currency("SLL", "Sierra Leonean Leone"),
        Currency("SOS", "Somali Shilling"),
        Currency("SRD", "Surinamese Dollar"),
        Currency("STD", "São Tomé and Príncipe Dobra"),
        Currency("STN", "São Tomé and Príncipe Dobra"),
        Currency("SVC", "Salvadoran Colón"),
        Currency("SYP", "Syrian Pound"),
        Currency("SZL", "Swazi Lilangeni"),
        Currency("THB", "Thai Baht"),
        Currency("TJS", "Tajikistani Somoni"),
        Currency("TMT", "Turkmenistani Manat"),
        Currency("TND", "Tunisian Dinar"),
        Currency("TOP", "Tongan Paʻanga"),
        Currency("TRY", "Turkish Lira"),
        Currency("TTD", "Trinidad and Tobago Dollar"),
        Currency("TWD", "New Taiwan Dollar"),
        Currency("TZS", "Tanzanian Shilling"),
        Currency("UAH", "Ukrainian Hryvnia"),
        Currency("UGX", "Ugandan Shilling"),
        Currency("USD", "United States Dollar"),
        Currency("UYU", "Uruguayan Peso"),
        Currency("UZS", "Uzbekistan Som"),
        Currency("VES", "Sovereign Bolivar"),
        Currency("VND", "Vietnamese Dong"),
        Currency("VUV", "Vanuatu Vatu"),
        Currency("WST", "Samoan Tala"),
        Currency("XAF", "CFA Franc BEAC"),
        Currency("XAG", "Silver (troy ounce)"),
        Currency("XAU", "Gold (troy ounce)"),
        Currency("XCD", "East Caribbean Dollar"),
        Currency("XCG", "Caribbean Guilder"),
        Currency("XDR", "Special Drawing Rights"),
        Currency("XOF", "CFA Franc BCEAO"),
        Currency("XPF", "CFP Franc"),
        Currency("YER", "Yemeni Rial"),
        Currency("ZAR", "South African Rand"),
        Currency("ZMK", "Zambian Kwacha (pre-2013)"),
        Currency("ZMW", "Zambian Kwacha"),
        Currency("ZWL", "Zimbabwean Dollar")
    )
}

fun getLatestRatesRequest(): GetLatestRates.Request {
    return GetLatestRates.Request(
        source = "USD",
        currencies = "AUD,EUR,GBP,PLN",
        accessKey = "ASDFDFDSGSDFDS12312ASASDSA",
        format = 1
    )
}

fun getLatestRatesResponse(
    source: String,
    avableCurrencies: List<Currency>,
    currencies: String
): GetLatestRates.Response {
    return try {
        val operationSource = source.ifEmpty { getCurrencies().first().code }
        val sourceIsValid = avableCurrencies.find { it.code == operationSource }
        if (sourceIsValid == null) {
            throw Exception()
        }

        val targets = getTargetsByCurrencies(avableCurrencies, currencies)
        val quotes = generateRandomExchangeRates(operationSource, targets)
        GetLatestRates.Response(
            success = true,
            terms = "https://www.termsfeed.com/blog/sample-terms-and-conditions-template/",
            privacy = "https://termify.io/privacy-policy-generator?gad_source=1&gad_campaignid=10836291524&gbraid=0AAAAAC6IOXLl_P31uk89uiTLnLkJzATzP&gclid=CjwKCAjw_-3GBhAYEiwAjh9fUNYYI9vIOQUMFXQWlzEQpkqNzGYYQIkjDFc8QG8kySVNFAFaZ1uy2RoCe3MQAvD_BwE",
            timestamp = Date.from(Instant.now()).time,
            source = operationSource,
            quotes = quotes,
            error = null
        )
    } catch (_: Exception) {
        GetLatestRates.Response(
            success = false,
            terms = null,
            privacy = null,
            timestamp = null,
            source = null,
            quotes = null,
            error = getError()
        )
    }

}

fun getCurrenciesRequest(): GetCurrencies.Request {
    return GetCurrencies.Request(accessKey = "asdasdasdasdsasd")
}

fun getCurrenciesResponse(): GetCurrencies.Response {
    return GetCurrencies.Response(
        success = true,
        terms = "https://www.termsfeed.com/blog/sample-terms-and-conditions-template/",
        privacy = "https://termify.io/privacy-policy-generator?gad_source=1&gad_campaignid=10836291524&gbraid=0AAAAAC6IOXLl_P31uk89uiTLnLkJzATzP&gclid=CjwKCAjw_-3GBhAYEiwAjh9fUNYYI9vIOQUMFXQWlzEQpkqNzGYYQIkjDFc8QG8kySVNFAFaZ1uy2RoCe3MQAvD_BwE",
        error = null,
        currencies = getCurrencies()
    )
}

fun getError(): Error {
    return Error(201, "You have supplied an invalid Source Currency. [Example: source=EUR]")
}


private fun generateRandomExchangeRates(base: String, targets: List<String>): Map<String, Double> {
    return targets.associate { target ->
        "$base$target" to Random.nextDouble(0.5, 5.0) // Adjust range as needed
    }
}

private fun getTargetsByCurrencies(
    avableCurrencies: List<Currency>,
    currencies: String
): List<String> {
    val avableCurrencyCodes = avableCurrencies.map { it.code }
    val targetList = currencies
        .split(",")
        .toSet()
        .filter { currencie ->
            avableCurrencyCodes.find { it == currencie } != null
        }
    return targetList
}
package com.jagl.data.datasource.exchangeRate


import com.jagl.core.network.INetworkManager
import com.jagl.data.api.model.GetLatestRates
import com.jagl.data.api.repository.ICurrencyLayerRepository
import com.jagl.data.api.utils.ApiUtils
import com.jagl.data.api.utils.ApiUtils.safeApiStateCall
import com.jagl.data.local.dao.ExchangeRateDao
import com.jagl.data.local.entity.ExchangeRateEntity
import com.jagl.domain.model.ApiState
import com.jagl.domain.model.Currency
import com.jagl.domain.model.ExchangeRate
import javax.inject.Inject

/**
 * Data source that handles operations related to exchange rates.
 */
class ExchangeDataSource @Inject constructor(
    private val networkManager: INetworkManager,
    private val api: ICurrencyLayerRepository,
    private val exchangeRateDao: ExchangeRateDao,
) : IExchangeDataSource {

    /**
     * Gets the exchange rate for a given amount, date, and currencies.
     * It first checks the local database. If it's not present, it fetches the data from the API
     * and stores it in the local database.
     * @param amount The amount to convert.
     * @param date The date of the exchange rate.
     * @param fromCurrency The source currency.
     * @param toCurrency The target currency.
     * @return An [ApiState] with an [ExchangeRate] object.
     */
    override suspend fun getExchangeRate(
        amount: Double,
        date: String,
        fromCurrency: Currency,
        toCurrency: Currency
    ): ApiState<ExchangeRate> = safeApiStateCall {

        val lastExchange = exchangeRateDao.getExchangeRateForDate(
            fromCurrency = fromCurrency.code,
            toCurrency = toCurrency.code,
            date = date
        )

        if (lastExchange.isNotEmpty()) {
            val rate = amount * lastExchange.first().rate
            val exchangeRate = ExchangeRate(
                fromCurrency = fromCurrency.code,
                toCurrency = toCurrency.code,
                rate = rate
            )
            return@safeApiStateCall ApiState.Success(exchangeRate)
        }

        if (networkManager.isConnected().not())
            return@safeApiStateCall ApiState.Error(message = ApiUtils.NO_INTERNET_ERROR)


        val request = GetLatestRates.Request(
            source = fromCurrency.code,
            currencies = toCurrency.code
        )

        val result = api.getLatestRates(request)

        if (result.isFailure) {
            val message = result.exceptionOrNull()?.message ?: ApiUtils.GENERIC_ERROR
            return@safeApiStateCall ApiState.Error(message)
        }

        val body = result.getOrNull()
        val rate = body?.quotes?.get(toCurrency.code)
        if (rate == null)
            return@safeApiStateCall ApiState.Error(ApiUtils.NO_RATE_ERROR)

        val exchangeRate = ExchangeRate(
            fromCurrency = fromCurrency.code,
            toCurrency = toCurrency.code,
            rate = rate
        )
        val entity = ExchangeRateEntity.fromExchangeRate(
            exchangeRate = exchangeRate,
            date = date,
            timestamp = body.timestamp ?: 0
        )
        exchangeRateDao.insertExchangeRate(entity)
        return@safeApiStateCall ApiState.Success(exchangeRate)

    }
}

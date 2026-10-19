package com.jagl.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jagl.domain.model.Currency

/**
 * Entity for storing currencies in the local database
 */
@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,
    val name: String
) {
    /**
     * Converts the entity to a domain model
     */
    fun toCurrency(): Currency {
        return Currency(
            code = code,
            name = name
        )
    }

    companion object {
        /**
         * Creates an entity from a domain model
         */
        fun fromCurrency(
            currency: Currency
        ): CurrencyEntity {
            return CurrencyEntity(
                code = currency.code,
                name = currency.name
            )
        }

        fun fromMap(
            code: String,
            name: String
        ): CurrencyEntity {
            return CurrencyEntity(
                code = code,
                name = name
            )
        }
    }
}
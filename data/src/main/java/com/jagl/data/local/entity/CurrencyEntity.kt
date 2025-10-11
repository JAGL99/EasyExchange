package com.jagl.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jagl.domain.model.Currency

/**
 * Entidad para almacenar las tasas de cambio en la base de datos local
 */
@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,
    val name: String
) {
    /**
     * Convierte la entidad a un modelo de dominio
     */
    fun toCurrency(): Currency {
        return Currency(
            code = code,
            name = name
        )
    }

    companion object {
        /**
         * Crea una entidad a partir de un modelo de dominio
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
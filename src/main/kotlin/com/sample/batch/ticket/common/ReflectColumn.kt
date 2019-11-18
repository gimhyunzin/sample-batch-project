package com.sample.batch.ticket.common

import com.sample.batch.ticket.model.Ticket
import kotlin.reflect.KProperty1

open class ReflectColumn {

    fun existCompanyContactManager(property: KProperty1<*, *>, ticket: Ticket): Boolean {
        val companyContactManager = "companyContactManager"
        return when(ticket.companyContactManager) {
            true -> isEqualsProperty(property, companyContactManager)
            false -> false
        }
    }

    fun existCompanyCsManager(property: KProperty1<*, *>, ticket: Ticket): Boolean {
        val companyCsManager = "companyCsManager"
        return when(ticket.companyCsManager) {
            true -> isEqualsProperty(property, companyCsManager)
            false -> false
        }
    }

    fun existCompanyStockManager(property: KProperty1<*, *>, ticket: Ticket): Boolean {
        val companyStockManager = "companyStockManager"
        return when(ticket.companyStockManager) {
            true -> isEqualsProperty(property, companyStockManager)
            false -> false
        }
    }

    private fun isEqualsProperty(property: KProperty1<*, *>, propertyName: String): Boolean {
        var exist = false
        return when (property.name.equals(propertyName, true)) {
            true -> {
                exist = true
                exist
            }
            false -> exist
        }
    }
}
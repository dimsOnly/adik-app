package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.TransactionEntity
import com.salwa.adikapp.data.entity.TransactionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.transactionDao()

    val transactions = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val saldo = dao.getSaldo().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    val totalPemasukan = dao.getTotalPemasukan().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    val totalPengeluaran = dao.getTotalPengeluaran().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    fun addTransaction(title: String, amount: Long, type: TransactionType, category: String, note: String, dateMillis: Long) {
        viewModelScope.launch {
            dao.insert(TransactionEntity(title = title, amount = amount, type = type, category = category, note = note, dateMillis = dateMillis))
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch { dao.delete(transaction) }
    }
}

package com.salwa.adikapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salwa.adikapp.AdikApplication
import com.salwa.adikapp.data.entity.WishlistEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WishlistViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as AdikApplication).database.wishlistDao()

    val items = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addItem(name: String, price: Long, priority: Int, note: String) {
        viewModelScope.launch {
            dao.insert(WishlistEntity(name = name, estimatedPrice = price, priority = priority, note = note, createdAtMillis = System.currentTimeMillis()))
        }
    }

    fun toggleAchieved(item: WishlistEntity) {
        viewModelScope.launch { dao.update(item.copy(isAchieved = !item.isAchieved)) }
    }

    fun deleteItem(item: WishlistEntity) {
        viewModelScope.launch { dao.delete(item) }
    }
}

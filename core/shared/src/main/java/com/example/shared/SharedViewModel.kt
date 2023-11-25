package com.example.shared

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SharedViewModel @Inject constructor(): ViewModel() {
    private val _refreshCatalog = MutableStateFlow(false)
    val refreshCatalog: StateFlow<Boolean> = _refreshCatalog

    fun triggerCatalogRefresh() {
        _refreshCatalog.value = true
        println("triggered refresh")
        println(_refreshCatalog.value.toString())
    }

    fun  onCatalogRefreshed() {
        _refreshCatalog.value = false
        println("on catalog refreshed")
        println(_refreshCatalog.value.toString())
    }
}
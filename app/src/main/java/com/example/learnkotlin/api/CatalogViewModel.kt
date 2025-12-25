package com.example.learnkotlin.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val repo: CatalogRepository = CatalogRepository()
) : ViewModel() {
    private val _items = MutableStateFlow<List<PerfumeDto>>(emptyList())
    val items: StateFlow<List<PerfumeDto>> = _items

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun load() {
        if (_loading.value) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _items.value = repo.getPerfumes()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load products"
            } finally {
                _loading.value = false
            }
        }
    }
}
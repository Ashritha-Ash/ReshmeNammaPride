package com.example.reshmenammapride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshmenammapride.AppDatabase
import com.example.reshmenammapride.ClimateEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClimateViewModel(private val db: AppDatabase) : ViewModel() {
    private val _entries = MutableStateFlow<List<ClimateEntry>>(emptyList())
    val entries: StateFlow<List<ClimateEntry>> = _entries

    fun loadEntries(email: String) {
        viewModelScope.launch {
            db.climateDao().getEntriesForUser(email).collect {
                _entries.value = it
            }
        }
    }

    fun saveEntry(entry: ClimateEntry) {
        viewModelScope.launch {
            db.climateDao().insertEntry(entry)
            loadEntries(entry.userEmail)
        }
    }
}
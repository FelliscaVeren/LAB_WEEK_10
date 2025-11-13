package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    // Declare LiveData
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    // Initialize LiveData
    init {
        _total.postValue(0)
    }

    // Increment total
    fun incrementTotal() {
        _total.postValue(_total.value?.plus(1))
    }
}

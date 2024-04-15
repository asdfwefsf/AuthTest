package com.company.authtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class productviewmodel : ViewModel(){


    private val _thingOn = MutableStateFlow<List<String>>(emptyList())
    val thingOn: StateFlow<List<String>> = _thingOn.asStateFlow()
//    private suspend fun updateProduct() = viewModelScope.launch {
//        updateThingOnUseCase().collect { thingOnList ->
//            _thingOn.value = thingOnList
//        }
//    }


}
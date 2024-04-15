package com.company.authtest.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class productviewmodel {
    private val _thingOn = MutableStateFlow<List<String>>(emptyList())
    val thingOn: StateFlow<List<String>> = _thingOn.asStateFlow()
}
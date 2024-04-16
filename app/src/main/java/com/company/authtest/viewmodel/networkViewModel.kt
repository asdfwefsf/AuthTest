package com.company.authtest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.authtest.model.Product
import com.company.authtest.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class networkViewModel : ViewModel() {

    init {

        viewModelScope.launch {
            test()
        }
    }

    private val _thingOn = MutableStateFlow<Product>(Product("d","d"))
    val thingOn: StateFlow<Product> = _thingOn.asStateFlow()
    suspend fun test() {
        val result = NetworkService.productApi.getProduct()
        val resultBody = result.body() ?: emptyList()

        if (result.isSuccessful && result.body() != null) {
            withContext(Dispatchers.IO) {
                Log.d("networkTest", "network success")

                resultBody.map { product ->
                    Log.d("products", "Name: ${product.name}, Image: ${product.image}")
                    _thingOn.value = product
                }
            }
        }
    }
}


package com.company.authtest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.authtest.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class networkViewModel : ViewModel() {

    init {

        viewModelScope.launch {
            test()
        }
    }

    suspend fun test() {
        val result = NetworkService.productApi.getProduct()
        val resultBody = result.body() ?: emptyList()

        Log.d("networkTest" , "network error")

        if (result.isSuccessful && result.body() != null) {
            withContext(Dispatchers.IO) {
                Log.d("networkTest" , "network success")

                resultBody.forEach { product ->
                    Log.d("products", "Name: ${product.name}, Image: ${product.image}")
                }
            }
        }

//        NetworkService.productApi.getProduct()

    }
}


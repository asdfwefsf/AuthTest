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

    //    override suspend fun updateThingOn(): Flow<List<String>> {
//        return dao.getAll().map { entities ->
//            // List<String> 순회하면서 각각의 thingOn만 리스팅한 Flow 반환.
//            // Flow로 반환하니까 실시간 업데이트 가능.
//            entities.map {
//                it.thingOn
//            }
//        }
//    }
    private val _thingOn = MutableStateFlow<Product>(Product("d","d"))
    val thingOn: StateFlow<Product> = _thingOn.asStateFlow()
    suspend fun test() {
        val result = NetworkService.productApi.getProduct()
        val resultBody = result.body() ?: emptyList()
//        result.map {
//            _thingOn.value = it
//        }

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


//        suspend fun test() {
//            viewModelScope.launch {
//                val result = NetworkService.productApi.getProduct().first()  // API 호출 결과를 받아옴
//                if (result.isNotEmpty()) {
//                    _thingOn.value = result  // StateFlow 업데이트
//                } else {
//                    Log.d("networkTest", "Empty or error in fetching data")
//                }
//            }

//        result.map {
//            _thingOn.value = it
//        }
//
//        Log.d("networkTest", "network error")
//
//        if (result.isSuccessful && result.body() != null) {
//            withContext(Dispatchers.IO) {
//                Log.d("networkTest" , "network success")
//
//                resultBody.map { product ->
//                    Log.d("products", "Name: ${product.name}, Image: ${product.image}")
//                    product. }
//                }
//            }
//        }

//        NetworkService.productApi.getProduct()






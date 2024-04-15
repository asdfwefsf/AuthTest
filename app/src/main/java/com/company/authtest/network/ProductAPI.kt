package com.company.authtest.network

import com.company.authtest.model.JsonModel
import com.company.authtest.model.Product
import com.company.authtest.model.ProductInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

object NetworkService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.jsonbin.io/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productApi: ProductAPI = retrofit.create(ProductAPI::class.java)
}

interface ProductAPI {
    @GET("b/661bde01ad19ca34f859e2d3?meta=false")
    @Headers("X-JSON-Path: Product[:]")
    suspend fun getProduct(): Response<List<Product>>

}


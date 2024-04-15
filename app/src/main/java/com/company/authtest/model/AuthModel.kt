package com.company.authtest.model

data class AuthModel(
    val user : String,
    val address : String,
    val orderList : Map<Int , ProductInfo>
)

data class ProductInfo(
    val name : String,
    val url : String
)

data class ProductResponse(
    val Product : List<ProductInfo>
)



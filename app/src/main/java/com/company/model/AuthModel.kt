package com.company.model

data class AuthModel(
    val user : String,
    val address : String,
    val orderList : Map<Int , productInfo>
)

data class productInfo(
    val name : String,
    val url : String
)



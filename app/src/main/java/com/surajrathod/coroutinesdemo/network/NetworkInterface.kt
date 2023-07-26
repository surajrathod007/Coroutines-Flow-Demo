package com.surajrathod.coroutinesdemo.network

import com.surajrathod.coroutinesdemo.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkInterface {


    @GET("products/{id}")
    suspend fun loadProduct(@Path("id") id : Int) : Product

}
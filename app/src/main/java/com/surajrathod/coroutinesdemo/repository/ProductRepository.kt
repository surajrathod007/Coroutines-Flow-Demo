package com.surajrathod.coroutinesdemo.repository

import com.surajrathod.coroutinesdemo.model.Product
import com.surajrathod.coroutinesdemo.network.NetworkInterface
import com.surajrathod.coroutinesdemo.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(private val networkInterface: NetworkInterface) {

    suspend fun getProducts(count: Int=10): Flow<Product> = flow {
        for (i in 1..count) {
            val d = NetworkService.db.loadProduct(i)
            emit(d)
        }

    }.flowOn(Dispatchers.IO)
}
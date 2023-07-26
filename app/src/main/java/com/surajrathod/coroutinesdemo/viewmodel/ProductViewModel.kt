package com.surajrathod.coroutinesdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surajrathod.coroutinesdemo.model.Product
import com.surajrathod.coroutinesdemo.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState(isLoading = true, msg = ""))
    val uiState: StateFlow<ProductUiState> = _uiState

    private val _msg = MutableLiveData<String>("")
    val msg: LiveData<String> = _msg

    init {
        viewModelScope.launch {
            val d = productRepository.getProducts().toList()
            _uiState.update {
                it.copy(false, d.map {
                    ProductState(
                        brand = it.brand,
                        category = it.category,
                        description = it.description,
                        discountPercentage = it.discountPercentage,
                        id = it.id,
                        images = it.images,
                        price = it.price,
                        rating = it.rating,
                        stock = it.stock,
                        thumbnail = it.thumbnail,
                        title = it.title
                    ) {
                        _uiState.update { state->
                            state.copy(msg = it.title)
                        }
                    }
                })
            }
        }
    }

}

data class ProductUiState(
    val isLoading: Boolean,
    val products: List<ProductState> = emptyList(),
    val msg: String
)

data class ProductState(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val id: Int,
    val images: List<String>,
    val price: Float,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val onClick: () -> Unit
)
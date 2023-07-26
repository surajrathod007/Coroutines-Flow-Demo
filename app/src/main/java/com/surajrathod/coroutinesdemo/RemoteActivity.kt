package com.surajrathod.coroutinesdemo

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.surajrathod.coroutinesdemo.databinding.ActivityRemoteBinding
import com.surajrathod.coroutinesdemo.model.Product
import com.surajrathod.coroutinesdemo.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityRemoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRemoteBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val p = ProgressDialog(this)
        lifecycleScope.launch {
            getProducts().collect {
                if (it.isLoading) {
                    p.show()
                    Log.d("RETROSURAJ","Show Loading")
                } else if(!it.isLoading){
                    val t = TextView(this@RemoteActivity)
                    t.text = it.product?.title
                    binding.llContainer.addView(
                        t
                    )
                    p.hide()
                    Log.d("RETROSURAJ","Hide Loading")
                }
            }
        }
    }

    data class ProductState(
        val isLoading: Boolean,
        val product: Product? = null
    )

    suspend fun getProducts() = flow<ProductState> {

        for (i in 1..100) {

            delay(2000)
            emit(ProductState(isLoading = true))
            val data = NetworkService.db.loadProduct(i)

            emit(ProductState(false, data))

        }
    }.flowOn(Dispatchers.IO)
}
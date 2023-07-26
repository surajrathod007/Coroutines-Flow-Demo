package com.surajrathod.coroutinesdemo.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.surajrathod.coroutinesdemo.R
import com.surajrathod.coroutinesdemo.adapter.ProductAdapter
import com.surajrathod.coroutinesdemo.databinding.ActivityProductBinding
import com.surajrathod.coroutinesdemo.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

    lateinit var binding : ActivityProductBinding

    lateinit var vm : ProductViewModel
    lateinit var pr : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)

        setContentView(binding.root)
        vm = ViewModelProvider(this).get(ProductViewModel::class.java)
        pr = ProgressDialog(this)
        pr.setTitle("Please Wait...")
        lifecycleScope.launch {
            vm.uiState.collect{
                if(it.isLoading){
                    pr.show()
                }else{
                    pr.hide()
                }
                if(it.products.isNotEmpty()){
                    binding.rvProducts.adapter = ProductAdapter(it.products)
                }
                if(it.msg.isNotEmpty()){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ProductActivity,it.msg,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}
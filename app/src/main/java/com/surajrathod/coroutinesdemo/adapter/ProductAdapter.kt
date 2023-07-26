package com.surajrathod.coroutinesdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.surajrathod.coroutinesdemo.databinding.ProductItemLayoutBinding
import com.surajrathod.coroutinesdemo.viewmodel.ProductState

class ProductAdapter(val list: List<ProductState>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductItemLayoutBinding) : ViewHolder(binding.root) {
        val txtTitle = binding.txtProductTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val i = list[position]
        with(holder){
            txtTitle.text = i.title
            txtTitle.setOnClickListener {
                i.onClick.invoke()
            }
        }
    }
}
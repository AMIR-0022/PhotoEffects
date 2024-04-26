package com.amar.photostyle.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photostyle.databinding.ListItemCategoryBinding
import com.amar.photostyle.utils.catCurrentPos

class CategoryAdapter(private var callback: (item: Category) -> Unit)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList: List<Category> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val myView = LayoutInflater.from(parent.context)
        val binding = ListItemCategoryBinding.inflate(myView, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: ListItemCategoryBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(category: Category) {
                binding.category = category

                itemView.apply {
                    setOnClickListener {
                        callback.invoke(category)
                    }
                }
            }
    }
}
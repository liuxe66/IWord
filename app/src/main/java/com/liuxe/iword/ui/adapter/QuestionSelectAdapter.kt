package com.liuxe.iword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liuxe.iword.databinding.ItemSelectBinding

/**
 *  author : liuxe
 *  date : 2023/4/19 17:03
 *  description :
 */
class QuestionSelectAdapter(val data:List<String>) : BaseQuickAdapter<String, QuestionSelectAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: ItemSelectBinding = ItemSelectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {
        holder.binding.apply {
            tvSelect.text = data[position]
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
package com.liuxe.iword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liuxe.iword.data.bean.WordBean
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.databinding.ItemTodayWordBinding

/**
 *  author : liuxe
 *  date : 2023/4/18 09:22
 *  description :
 */
class TodayWordAdapter(var list: List<Word>) :
    BaseQuickAdapter<Word, TodayWordAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: ItemTodayWordBinding = ItemTodayWordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: Word?) {
        holder.binding.apply {
            tvWordName.text = list[position % list.size].name
            tvWordTrans.text = list[position % list.size].trans!!
        }
    }

    override fun getItemCount(items: List<Word>): Int {
        return Int.MAX_VALUE;
    }


    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}
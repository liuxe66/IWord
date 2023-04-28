package com.liuxe.iword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liuxe.iword.data.bean.BookBean
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.databinding.ItemBookBinding
import com.liuxe.iword.databinding.ItemStudyWordBinding
import com.liuxe.iword.ext.gone
import com.liuxe.iword.ext.throttleClick

/**
 *  author : liuxe
 *  date : 2023/4/27 09:31
 *  description :
 */
class BookAdapter : BaseQuickAdapter<BookBean, BookAdapter.VH>() {

    var mListener:OnBookClickListener? = null
    class VH(
        parent: ViewGroup,
        val binding: ItemBookBinding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: BookBean?) {
        holder.binding.apply {
            tvBook.text = item?.bookName
            tvBook.isSelected = item?.isSelect!!

            tvBook.throttleClick {
                mListener?.onBook(item!!)
            }
        }
    }


    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    interface OnBookClickListener{
        fun onBook(book:BookBean)
    }
}
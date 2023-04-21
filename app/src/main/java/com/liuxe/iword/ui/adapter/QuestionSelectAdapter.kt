package com.liuxe.iword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liuxe.iword.R
import com.liuxe.iword.data.bean.WordQuestion
import com.liuxe.iword.data.bean.WordQuestionSelect
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.databinding.ItemSelectBinding
import com.liuxe.iword.ext.gone
import com.liuxe.iword.ext.throttleClick
import com.liuxe.iword.ext.visible
import com.liuxe.iword.utils.LogUtils

/**
 *  author : liuxe
 *  date : 2023/4/19 17:03
 *  description :
 */
class QuestionSelectAdapter : BaseQuickAdapter<WordQuestionSelect, QuestionSelectAdapter.VH>() {

    var listerner:ItemClickListener? = null
    class VH(
        parent: ViewGroup,
        val binding: ItemSelectBinding = ItemSelectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: WordQuestionSelect?) {
        holder.setIsRecyclable(false)
        holder.binding.apply {

            when (item?.state) {
                1 -> {
                    tvSelect.text = item.word.trans
                }
                2 -> {
                    tvSelect.text = item.word.name
                }
            }

            ivSelect.gone()
            tvSelect.isSelected = false
            tvSelect.throttleClick {
                tvSelect.isSelected = true
                ivSelect.visible()
                if (item!!.isAnswer) {
                    ivSelect.load(R.drawable.white_ok_24)
                } else {
                    ivSelect.load(R.drawable.white_error_24)
                }

                listerner?.onItenClick(item)
            }

        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    interface ItemClickListener{
        fun onItenClick(item:WordQuestionSelect)
    }
}
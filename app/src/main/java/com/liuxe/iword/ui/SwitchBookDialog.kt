package com.liuxe.iword.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseBottomSheetFragment
import com.liuxe.iword.data.Consts
import com.liuxe.iword.data.bean.BookBean
import com.liuxe.iword.databinding.DialogSwitchBookBinding
import com.liuxe.iword.ext.dp
import com.liuxe.iword.ui.adapter.BookAdapter
import com.liuxe.iword.ui.vm.MainVM
import com.liuxe.iword.utils.PrefUtils

/**
 *  author : liuxe
 *  date : 2023/4/27 09:25
 *  description :
 */
class SwitchBookDialog : BaseBottomSheetFragment() {

    private lateinit var mBinding: DialogSwitchBookBinding

    private val mAdapter by lazy { BookAdapter() }

    private val mMainVM by createViewModel<MainVM>()

    var mListener: OnSelectBookListener? = null

    private var wordBookName by PrefUtils(Consts.CUR_BOOK_NAME, Consts.cet4CN)
    private var wordBookUrl by PrefUtils(Consts.CUR_BOOK, Consts.cet4)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.dialog_switch_book, container)
        mBinding.apply {
            rvBook.layoutManager = GridLayoutManager(requireActivity(), 2)
            rvBook.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val childCount = parent.adapter!!.itemCount
                    val childAdapterPosition = parent.getChildAdapterPosition(view)

                    if (childAdapterPosition % 2 == 0) {
                        outRect.set(0, 0, 16.dp().toInt(), 16.dp().toInt())
                    } else {
                        outRect.set(0, 0, 0, 16.dp().toInt())
                    }
                }
            })
            rvBook.adapter = mAdapter
        }

        mMainVM.loadBook().observe(this, Observer { list ->
            mAdapter.addAll(list)
            mAdapter.mListener = object :BookAdapter.OnBookClickListener{
                override fun onBook(book: BookBean) {
                    wordBookName = book.bookName
                    wordBookUrl = book.bookUrl
                    mListener?.onSuccess(book)
                    dialog?.dismiss()
                }

            }
        })


        return mBinding.root
    }

    interface OnSelectBookListener {
        fun onSuccess(book:BookBean)
    }
}
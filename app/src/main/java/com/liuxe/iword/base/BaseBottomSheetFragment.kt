package com.liuxe.iword.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.liuxe.iword.R
import com.liuxe.iword.utils.ToastUtil


abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    protected inline fun <reified T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes resId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate(inflater, resId, container, false)

    fun toast(str: String) {
        ToastUtil.showToast(str)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (dialog != null) {
            try {
                // 解决Dialog内D存泄漏
                dialog!!.setOnDismissListener(null)
                dialog!!.setOnCancelListener(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheet)
    }
    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]

        //需要toas提示的
        mViewModel.mMsg.observe(this, Observer {
            toast(it)
        })

        //网络请求失败的情况
        mViewModel.mException.observe(this, Observer {
            toast(it.msg)
            onError(it.msg)
        })

        mViewModel
    }

    /**
     * 处理网络请求失败
     */
    open fun onError(errorMsg: String) {

    }
}
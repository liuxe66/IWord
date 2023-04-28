package com.liuxe.iword.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.utils.ToastUtil

abstract class BaseDataBindingActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }

    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]

        mViewModel.mMsg.observe(this, Observer {
            toast(it)
        })

        mViewModel.mException.observe(this, Observer {
            toast(it.msg)
        })

        mViewModel.mLoadingState.observe(this, Observer {
            if (it) {
                showLoadingDialog()
            } else {
                dissmissLoadingDialog()
            }
        })

        mViewModel
    }

    var loadingDialog: Dialog? = null
    open fun showLoadingDialog() {
        var builder = AlertDialog.Builder(this, R.style.TransparentDialog)

        val contentView =
            LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)

        builder.setView(contentView)
        builder.setCancelable(false)
        loadingDialog = builder.create()

        loadingDialog?.show()
    }

    open fun dissmissLoadingDialog() {
        runOnUiThread {
            loadingDialog?.dismiss()
        }
    }

    override fun getResources(): Resources {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.getDisplayMetrics())
        return res
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 处理网络请求失败
     */
    open fun onError(errorMsg: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        init(savedInstanceState)
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    fun toast(str: String) {
        ToastUtil.showToast(str)
    }

    open fun initStatusBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .keyboardEnable(false)
            .init()
    }

    lateinit var centerTitleText: TextView
    protected fun initToolBar(toolbar: Toolbar, title: String) {
        toolbar.setNavigationIcon(R.drawable.black_back_24)
        toolbar.navigationIcon?.setTint(resources.getColor(R.color.color_text_main))
        toolbar.setNavigationOnClickListener { onBackPressed() }
        centerTitleText = TextView(this)
        setTitleCenter(toolbar, title, R.color.color_text_main)

    }

    private fun setTitleCenter(toolbar: Toolbar, title: String, color: Int) {

        centerTitleText.setTextColor(ContextCompat.getColor(this, color))
        centerTitleText.text = title
        centerTitleText.textSize = 18f
        centerTitleText.gravity = Gravity.CENTER
        centerTitleText.setLines(1)
        centerTitleText.ellipsize = TextUtils.TruncateAt.END
        val layoutParams = Toolbar.LayoutParams(
            Toolbar.LayoutParams.WRAP_CONTENT,
            Toolbar.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER
        centerTitleText.layoutParams = layoutParams
        toolbar.addView(centerTitleText)
    }
}
package com.liuxe.iword.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.databinding.ActivitySplashBinding
import com.liuxe.iword.ext.toActivity
import com.liuxe.iword.ui.vm.MainVM
import com.liuxe.iword.utils.PrefUtils
import com.liuxe.iword.utils.Preference

class SplashActivity : BaseDataBindingActivity() {

    private var isFirst: Boolean by Preference(Preference.isFirstLoad, true)

    private val mainVM by createViewModel<MainVM>()

    private val mBinding by binding<ActivitySplashBinding>(R.layout.activity_splash)

    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {
            mainVM.progressData.observe(this@SplashActivity, Observer {
                progressLoad.progress = it.toDouble()
            })
            if (isFirst) {
                mainVM.loadWord().observe(this@SplashActivity, Observer {
                    isFirst = false
                    toActivity<MainActivity>()
                })
            } else {
                toActivity<MainActivity>()
            }
        }
    }

    override fun showLoadingDialog() {

    }

    override fun dissmissLoadingDialog() {

    }
}
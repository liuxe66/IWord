package com.liuxe.iword.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.databinding.ActivityMainBinding
import com.liuxe.iword.ext.launchActivity
import com.liuxe.iword.ext.throttleClick
import com.liuxe.iword.ui.adapter.TodayWordAdapter
import com.liuxe.iword.ui.vm.WordVM

class MainActivity : BaseDataBindingActivity() {

    private val mWordVM by createViewModel<WordVM>()

    private val mBinding by binding<ActivityMainBinding>(R.layout.activity_main)

    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).keyboardEnable(false)
            .init()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    fun initData(){
        mBinding.apply {
            mWordVM.initData().observe(this@MainActivity, Observer {

                tvWordNum.text = "${mWordVM.hasStudyWordNum}/${mWordVM.wordSize}"
                progressWord.max = mWordVM.wordSize
                progressWord.progress = mWordVM.hasStudyWordNum.toDouble()

                rvTodayWord.adapter = TodayWordAdapter(mWordVM.todayWordList)
                rvTodayWord.scrollToPosition(Int.MAX_VALUE / 2);
                rvTodayWord.start()
            })
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvTodayWord.layoutManager = layoutManager

            tvStart.throttleClick {
                launchActivity<WordActivity>()
            }
        }

    }
}
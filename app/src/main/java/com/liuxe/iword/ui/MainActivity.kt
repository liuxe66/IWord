package com.liuxe.iword.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.databinding.ActivityMainBinding
import com.liuxe.iword.ui.adapter.TodayWordAdapter
import com.liuxe.iword.ui.vm.WordVM

class MainActivity : BaseDataBindingActivity() {

    private val mWordVM by createViewModel<WordVM>()

    private val mBinding by binding<ActivityMainBinding>(R.layout.activity_main)

    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).keyboardEnable(false)
            .init()
    }

    override fun init(savedInstanceState: Bundle?) {

        mBinding.apply {


            val wordList = mWordVM.getLevel()
            val layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvTodayWord.layoutManager = layoutManager
            val todayAdapter = TodayWordAdapter(wordList)
            rvTodayWord.adapter = todayAdapter
            rvTodayWord.scrollToPosition(Int.MAX_VALUE / 2);
            rvTodayWord.start()
            todayAdapter.setOnItemClickListener { adapter, view, position ->
                toast(
                    wordList[position % wordList.size].name!!
                )
            }

        }
    }
}
package com.liuxe.iword.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.databinding.ActivityWordStudyBinding
import com.liuxe.iword.ext.throttleClick
import com.liuxe.iword.ext.toActivity
import com.liuxe.iword.ui.adapter.StudyWordAdapter
import com.liuxe.iword.ui.vm.WordVM
import com.liuxe.iword.utils.LogUtils

class WordStudyActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityWordStudyBinding>(R.layout.activity_word_study)
    private val mWordVM by createViewModel<WordVM>()
    private val mWordAdapter by lazy { StudyWordAdapter() }


    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {
            initToolBar(toolbar, "本关单词")
            rvWord.layoutManager = LinearLayoutManager(this@WordStudyActivity)
            rvWord.adapter = mWordAdapter
            mWordVM.initData().observe(this@WordStudyActivity, Observer {
                LogUtils.e("本关单词")
                mWordAdapter.submitList(mWordVM.todayWordList)
            })

            //开始闯关
            tvNext.throttleClick {
                toActivity<WordQuestionActivity>()
            }
        }

    }
}
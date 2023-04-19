package com.liuxe.iword.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.databinding.ActivityEn2cnBinding
import com.liuxe.iword.ext.throttleClick
import com.liuxe.iword.ui.adapter.QuestionSelectAdapter
import com.liuxe.iword.ui.vm.WordVM
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class En2cnActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityEn2cnBinding>(R.layout.activity_en2cn)

    private val mWordVM by createViewModel<WordVM>()

    override fun initStatusBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_qa_bg)
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .keyboardEnable(false)
            .init()
    }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {
            ivClose.throttleClick {
                finish()
            }

            rvSelect.layoutManager = LinearLayoutManager(this@En2cnActivity)


            mWordVM.curQuestionLiveData.observe(this@En2cnActivity, Observer {
                tvWordName.text = it.question
                var adapter = QuestionSelectAdapter(it.selectList)
                rvSelect.adapter = adapter

                adapter.setOnItemClickListener { adapter, view, position ->

                    if ( it.selectList[position] == it.answer){

                    } else {

                    }
                    

                }

            })

        }
    }

    private fun creatQuestion() {
        mWordVM.nextQuestion()
    }
}
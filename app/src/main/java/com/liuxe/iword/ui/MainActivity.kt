package com.liuxe.iword.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.data.Consts
import com.liuxe.iword.data.bean.BookBean
import com.liuxe.iword.databinding.ActivityMainBinding
import com.liuxe.iword.ext.differDay
import com.liuxe.iword.ext.launchActivity
import com.liuxe.iword.ext.throttleClick
import com.liuxe.iword.ui.adapter.BookAdapter
import com.liuxe.iword.ui.adapter.TodayWordAdapter
import com.liuxe.iword.ui.vm.MainVM
import com.liuxe.iword.ui.vm.WordVM
import com.liuxe.iword.utils.PrefUtils

class MainActivity : BaseDataBindingActivity() {

    private val mWordVM by createViewModel<WordVM>()
    private val mMainVM by createViewModel<MainVM>()

    private val mBinding by binding<ActivityMainBinding>(R.layout.activity_main)

    var lastDays by PrefUtils(PrefUtils.LASTDAY, 0L)
    var studyDays by PrefUtils(PrefUtils.STUDYDAYS, 0)
    var wordBook by PrefUtils(Consts.CUR_BOOK_NAME, "四级词汇")
    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).keyboardEnable(false)
            .init()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
    fun loadWord(book:BookBean){
        mMainVM.loadWord(book.bookUrl).observe(this, Observer {
            initData()
        })
    }

    fun initData() {
        mBinding.apply {
            if (lastDays == 0L) {
                studyDays = 1
                lastDays = System.currentTimeMillis()
            } else {
                if (System.currentTimeMillis().differDay(lastDays) != 0) {
                    lastDays = System.currentTimeMillis()
                    studyDays += 1
                }
            }
            tvDays.text = studyDays.toString()
            mWordVM.initData().observe(this@MainActivity, Observer {

                tvBook.text = "${wordBook}(${mWordVM.allWordSize}词)"
                tvWordNum.text =
                    "${mWordVM.wordsIndex}/${mWordVM.allWordSize / mWordVM.wordsSize + 1}关"
                progressWord.max = mWordVM.allWordSize / mWordVM.wordsSize
                progressWord.progress = mWordVM.wordsIndex.toDouble()

                rvTodayWord.adapter = TodayWordAdapter(mWordVM.showWordList)
                rvTodayWord.scrollToPosition(Int.MAX_VALUE / 2);
                rvTodayWord.start()
            })
        }

    }

    override fun init(savedInstanceState: Bundle?) {


        mBinding.apply {

            switchWord.throttleClick {
                var bookDialog = SwitchBookDialog()
                bookDialog.mListener = object : SwitchBookDialog.OnSelectBookListener {
                    override fun onSuccess(book:BookBean) {
                       loadWord(book)
                    }
                }
                bookDialog.show(supportFragmentManager, "book")
            }


            val layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvTodayWord.layoutManager = layoutManager

            tvStart.throttleClick {
                launchActivity<WordStudyActivity>()
            }

            conEn.throttleClick {
                launchActivity<ErrorQuestionActivity> {
                    putExtra("type", Consts.EN_WORD)
                }
            }

            conCn.throttleClick {
                launchActivity<ErrorQuestionActivity> {
                    putExtra("type", Consts.CN_WORD)
                }
            }

            conSp.throttleClick {
                launchActivity<ErrorQuestionActivity> {
                    putExtra("type", Consts.SP_WORD)
                }
            }


        }

    }
}
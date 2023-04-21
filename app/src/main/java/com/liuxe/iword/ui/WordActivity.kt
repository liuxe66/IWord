package com.liuxe.iword.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.app.AppConfig
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.data.bean.WordQuestionSelect
import com.liuxe.iword.databinding.ActivityWordBinding
import com.liuxe.iword.ext.gone
import com.liuxe.iword.ext.loadAssetsSVGA
import com.liuxe.iword.ext.throttleClick
import com.liuxe.iword.ext.visible
import com.liuxe.iword.ui.adapter.QuestionSelectAdapter
import com.liuxe.iword.ui.vm.WordVM
import java.io.IOException

class WordActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityWordBinding>(R.layout.activity_word)

    private val mWordVM by createViewModel<WordVM>()

    var isWordPlaying = false

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

            rvSelect.layoutManager = LinearLayoutManager(this@WordActivity)
            val adapter = QuestionSelectAdapter()
            rvSelect.adapter = adapter

            mWordVM.initWordsQuestion()
            mWordVM.finishData.observe(this@WordActivity, Observer {
                toast("学习结束")
                tvWordName.gone()
                tvWordTrans.gone()
                ivVolume.gone()
                rvSelect.gone()

                svgaView.visible()
                tvOk.visible()

                tvOk.throttleClick {
                    finish()
                }

                svgaView.loadAssetsSVGA(fileName = "gift_rose")
            })

            mWordVM.questionData.observe(this@WordActivity, Observer {
                tvWordName.text = it.word.name
                tvWordTrans.text = it.word.trans
                when (it.questionType) {
                    1 -> {
                        tvWordName.visible()
                        tvWordTrans.gone()
                        ivVolume.visible()
//                        playOnlineSound("https://dict.youdao.com/dictvoice?audio=${it.word.name}&type=2")
                        conWord.throttleClick {
                            playOnlineSound("https://dict.youdao.com/dictvoice?audio=${it.word.name}&type=2")
                        }
                    }
                    2 -> {
                        tvWordName.gone()
                        tvWordTrans.visible()
                        ivVolume.gone()
                        conWord.throttleClick {

                        }
                    }
                }

                adapter.submitList(it.selectList)
                adapter.notifyDataSetChanged()

                adapter.listerner = object :QuestionSelectAdapter.ItemClickListener{
                    override fun onItenClick(item: WordQuestionSelect) {
                        mWordVM.onSelect(item.isAnswer,it)
                    }
                }

            })

        }
    }

    /**
     * 播放单词发音
     * @param soundUrlDict String
     */
    private fun playOnlineSound(soundUrlDict: String) {
        if (!isWordPlaying) {
            isWordPlaying = true
            try {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(soundUrlDict)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
                mediaPlayer.setOnCompletionListener { mp ->
                    mp?.release()
                    isWordPlaying = false
                }
                mediaPlayer.setOnErrorListener { p0, p1, p2 ->
                    isWordPlaying = false
                    false
                }
            } catch (e: IOException) {
                isWordPlaying = false
            }
        }

    }
}
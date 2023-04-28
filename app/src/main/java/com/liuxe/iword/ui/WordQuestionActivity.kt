package com.liuxe.iword.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.databinding.ActivityWordBinding
import com.liuxe.iword.ext.*
import com.liuxe.iword.ui.adapter.QuestionSelectAdapter
import com.liuxe.iword.ui.vm.WordVM
import com.liuxe.iword.utils.LogUtils
import java.io.IOException

class WordQuestionActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityWordBinding>(R.layout.activity_word)

    private val mWordVM by createViewModel<WordVM>()

    var isWordPlaying = false

    var hasClick = false

    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {

            initToolBar(toolbar,"单词闯关")
            rvSelect.layoutManager = LinearLayoutManager(this@WordQuestionActivity)
            val adapter = QuestionSelectAdapter()
            rvSelect.adapter = adapter

            mWordVM.initWordsQuestion()

            mWordVM.finishData.observe(this@WordQuestionActivity, Observer {
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

            mWordVM.questionData.observe(this@WordQuestionActivity, Observer {

                hasClick = false
                tvNext.invisible()
                when (it.questionType) {
                    1 -> {
//                        tvTitle.text = "英文选词"
                        tvWordName.text = it.word.name
                        tvWordTrans.text = it.word.trans
                        tvWordName.visible()
                        tvWordTrans.gone()

                    }
                    2 -> {
//                        tvTitle.text = "中文选义"
                        tvWordName.text = it.word.name
                        tvWordTrans.text = it.word.trans
                        tvWordName.gone()
                        tvWordTrans.visible()
                    }
                    3 -> {
//                        tvTitle.text = "单词拼写"
                        var replaceBlank = ""
                        it.blank!!.forEach {
                            replaceBlank += "_"
                        }
                        var showWord = it.word.name!!.replace(it.blank!!, replaceBlank)
                        LogUtils.e("showWord:" + showWord)
                        tvWordTrans.text = it.word.trans
                        tvWordName.text = showWord
                        tvWordTrans.visible()
                        tvWordName.visible()
                    }


                }
                ivVolume.visible()
                conWord.throttleClick {
                    playOnlineSound("https://dict.youdao.com/dictvoice?audio=${it.word.name}&type=2")
                }
                adapter.submitList(it.selectList)
                adapter.notifyDataSetChanged()

                tvNext.throttleClick {
                    mWordVM.creatQuestion()
                }

                adapter.listerner = object : QuestionSelectAdapter.ItemClickListener {
                    override fun onItemClick(position: Int) {
                        if (!hasClick) {
                            hasClick = true
                            if (it.selectList[position].isAnswer) {
                                val data = it.selectList
                                data[position].isClick = true
                                adapter.submitList(data)
                                adapter.notifyDataSetChanged()
                                mWordVM.creatQuestion()
                            } else {
                                val data = it.selectList
                                data.forEach { word ->
                                    if (word.isAnswer) {
                                        word.isClick = true
                                    }
                                }
                                data[position].isClick = true
                                adapter.submitList(data)
                                adapter.notifyDataSetChanged()
                                tvWordTrans.visible()
                                tvWordName.visible()
                                tvNext.visible()
                                mWordVM.onSelectError(it)
                            }

                        }


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
package com.liuxe.iword.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.iword.R
import com.liuxe.iword.base.BaseDataBindingActivity
import com.liuxe.iword.data.Consts
import com.liuxe.iword.databinding.ActivityWordBinding
import com.liuxe.iword.ext.*
import com.liuxe.iword.ui.adapter.QuestionSelectAdapter
import com.liuxe.iword.ui.vm.ErrorWordVM
import com.liuxe.iword.ui.vm.WordVM
import com.liuxe.iword.utils.LogUtils
import java.io.IOException

class ErrorQuestionActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityWordBinding>(R.layout.activity_word)

    private val mWordVM by createViewModel<ErrorWordVM>()

    var isWordPlaying = false

    var hasClick = false

    override fun init(savedInstanceState: Bundle?) {

        val typeQuestion = intent.getStringExtra("type")?:"单词复习"
        mBinding.apply {

            initToolBar(toolbar,typeQuestion)
            rvSelect.layoutManager = LinearLayoutManager(this@ErrorQuestionActivity)
            val adapter = QuestionSelectAdapter()
            rvSelect.adapter = adapter

            when(typeQuestion){
                Consts.CN_WORD -> mWordVM.initCnQuestion()
                Consts.EN_WORD -> mWordVM.initEnQuestion()
                Consts.SP_WORD -> mWordVM.initSpellQuestion()
            }

            mWordVM.finishData.observe(this@ErrorQuestionActivity, Observer {
                tvWordName.gone()
                tvWordTrans.gone()
                ivVolume.gone()
                rvSelect.gone()

                svgaView.visible()
                tvOk.text = "没有要复习的单词了"
                tvOk.visible()

                tvOk.throttleClick {
                    finish()
                }


                svgaView.loadAssetsSVGA(fileName = "gift_rose")
            })

            mWordVM.questionData.observe(this@ErrorQuestionActivity, Observer {

                hasClick = false
                tvNext.invisible()
                when (it.questionType) {
                    1 -> {
                        tvWordName.text = it.word.name
                        tvWordTrans.text = it.word.trans
                        tvWordName.visible()
                        tvWordTrans.gone()

                    }
                    2 -> {
                        tvWordName.text = it.word.name
                        tvWordTrans.text = it.word.trans
                        tvWordName.gone()
                        tvWordTrans.visible()
                    }
                    3 -> {
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
                                var data = it.selectList
                                data[position].isClick = true
                                adapter.submitList(data)
                                adapter.notifyDataSetChanged()
                                mWordVM.creatQuestion()
                            } else {
                                var data = it.selectList
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
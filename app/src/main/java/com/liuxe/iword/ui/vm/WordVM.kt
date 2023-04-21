package com.liuxe.iword.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.liuxe.iword.base.BaseViewModel
import com.liuxe.iword.data.bean.WordListBean
import com.liuxe.iword.data.bean.WordQuestion
import com.liuxe.iword.data.bean.WordQuestionSelect
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.utils.PrefUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  author : liuxe
 *  date : 2023/3/31 10:30
 *  description :
 */
class WordVM : BaseViewModel() {
    private var wordsListStr by PrefUtils(PrefUtils.prefWordList, "")
    private var wordsList = ArrayList<Word>()
    private var wordsIndex by PrefUtils(PrefUtils.prefWordIndex, 1)
    private var wordsSize by PrefUtils(PrefUtils.prefWordSize, 10)

    //问题集合
    private var questionList = ArrayList<WordQuestion>()

    //当前问题
    private val _questionData = MutableLiveData<WordQuestion>()
    var questionData = _questionData

    //结束标志
    private val _finishData = MutableLiveData<Boolean>()
    var finishData = _finishData

    //当前问题索引
    var curIndex = 0

    //今日单词
    var todayWordList = ArrayList<Word>()
    var hasStudyWordNum = 0
    var wordSize = 0

    fun initData() = liveData<String>{
        getCurData()
        emit("success")
    }

    /**
     * 获取今日单词集合
     * @param level Int
     * @return MutableList<WordBean>
     */
    fun getCurData() {
        wordsList.clear()
        wordsList.addAll(Gson().fromJson(wordsListStr, WordListBean::class.java).wordList!!)
        val data = wordsList.subList((wordsIndex - 1) * wordsSize, wordsIndex * wordsSize)
        todayWordList.addAll(data)

        hasStudyWordNum = (wordsIndex - 1) * wordsSize
        wordSize = wordsList.size

    }

    /**
     * 初始化 今日问题
     * @return LiveData<String>
     */
    fun initWordsQuestion() = viewModelScope.launch {
        //1.根据今日单词生成今日练习题
        //2.今日练习题添加中文选义，英文选义
        withContext(Dispatchers.IO) {
            getCurData()

            //组装今日考察单词问题列表
            todayWordList.forEach { word ->
                val selectList = creatSelectList(1)
                selectList.add(
                    WordQuestionSelect(
                        word = word, state = 1, isAnswer = true
                    )
                )
                //打乱顺序
                selectList.shuffle()


                //问题集合 添加question
                questionList.add(
                    WordQuestion(
                        word = word, selectList = selectList, questionType = 1
                    )
                )
            }

            //组装今日考察单词问题列表
            todayWordList.forEach { word ->
                val selectList = creatSelectList(2)
                selectList.add(
                    WordQuestionSelect(
                        word = word, state = 2, isAnswer = true
                    )
                )
                //打乱顺序
                selectList.shuffle()

                //问题集合 添加question
                questionList.add(
                    WordQuestion(
                        word = word, selectList = selectList, questionType = 2
                    )
                )
            }

        }
        creatQuestion()
    }

    fun creatSelectList(state: Int): ArrayList<WordQuestionSelect> {
        val randomStart = 0
        val randomEnd = wordsList.size

        val index1 = (randomStart until randomEnd).random()

        var index2 = (randomStart until randomEnd).random()
        var index3 = (randomStart until randomEnd).random()

        while (index1 == index2) {
            index2 = (randomStart until randomEnd).random()
        }

        while (index1 == index3 || index2 == index3) {
            index3 = (randomStart until randomEnd).random()
        }
        //选项集合
        val selectList = ArrayList<WordQuestionSelect>()
        selectList.add(
            WordQuestionSelect(
                word = wordsList[index1], state = state, isAnswer = false
            )
        )
        selectList.add(
            WordQuestionSelect(
                word = wordsList[index2], state = state, isAnswer = false
            )
        )
        selectList.add(
            WordQuestionSelect(
                word = wordsList[index3], state = state, isAnswer = false
            )
        )
        return selectList
    }

    /**
     * 生成当前问题
     * @returnviiveData<WordQuestion>
     */
    fun creatQuestion() = viewModelScope.launch {
        if (curIndex != 0) {
            delay(500)
        }
        if (curIndex == questionList.size) {
            _finishData.value = true
            wordsIndex++
        } else {
            _questionData.value = questionList[curIndex]
            curIndex++
        }

    }

    /**
     * 答题
     * @param answer Boolean
     * @param wordQuestion WordQuestion
     */
    fun onSelect(answer: Boolean, wordQuestion: WordQuestion) {
        if (!answer) {
            questionList.add(wordQuestion)
        }
        creatQuestion()
    }

}
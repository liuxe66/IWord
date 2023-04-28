package com.liuxe.iword.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.liuxe.iword.base.BaseViewModel
import com.liuxe.iword.data.Consts
import com.liuxe.iword.data.bean.WordListBean
import com.liuxe.iword.data.bean.WordQuestion
import com.liuxe.iword.data.bean.WordQuestionSelect
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.data.repository.RepositoryFactory
import com.liuxe.iword.utils.MmkvUtils
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

    private var repository = RepositoryFactory.makeWordRepository()

    private var wordsList = ArrayList<Word>()
    var wordsIndex by PrefUtils(PrefUtils.PREFWORDINDEX, 1)
    var wordsSize by PrefUtils(PrefUtils.PREFWORDSIZE, 10)

    //问题集合
    var questionList = ArrayList<WordQuestion>()


    //当前问题
    private val _questionData = MutableLiveData<WordQuestion>()
    var questionData = _questionData

    //错题统计
    private val _errorData = MutableLiveData<List<Word>>()
    var errorData = _errorData

    //当前问题
    private val _errorQuestionData = MutableLiveData<WordQuestion>()
    var errorQuestionData = _errorQuestionData

    //结束标志
    private val _finishData = MutableLiveData<Boolean>()
    var finishData = _finishData

    //当前问题索引
    var curIndex = 0

    //今日单词
    var todayWordList = ArrayList<Word>()

    //首页显示单词
    var showWordList = ArrayList<Word>()

    //复习单词集合
    var errorList = ArrayList<Word>()

    var hasStudyWordNum = 0
    var allWordSize = 0

    var wordChar = "abcdefghijklmnopqrstuvwxyz"

    fun initData() = liveData<String> {
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
        todayWordList.clear()
        errorList.clear()
        showWordList.clear()
        wordsList.addAll(
            MmkvUtils.decodeParcelable(
                Consts.ALL_WORD, WordListBean::class.java
            )?.wordList!!
        )
        val data = wordsList.subList((wordsIndex - 1) * wordsSize, wordsIndex * wordsSize)
        todayWordList.addAll(data)

        errorList = repository.getError()

        showWordList.addAll(todayWordList)
        showWordList.addAll(errorList)

        hasStudyWordNum = (wordsIndex - 1) * wordsSize
        allWordSize = wordsList.size

    }

    /**
     * 创建填空题
     * @return String
     */
    private fun creatBlank(blank: String): String {
        var str = ""
        for (i in blank.indices) {
            str += wordChar.random().toString()
        }
        return str
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


            //英文选义
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
            //中文选词
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
//            //单词拼写
//            todayWordList.forEach { word ->
//
//                var blankStart = (0 until word?.name!!.length - 1).random()
//                var blankEnd = (blankStart + 1 until word?.name!!.length).random()
//                var blank = word?.name!!.substring(blankStart, blankEnd)
//
//                val selectList = creatSelectList(3, blank)
//                selectList.add(
//                    WordQuestionSelect(
//                        word = word, state = 3, isAnswer = true, blank = blank
//                    )
//                )
//                //打乱顺序
//                selectList.shuffle()
//
//                //问题集合 添加question
//                questionList.add(
//                    WordQuestion(
//                        word = word,
//                        selectList = selectList,
//                        questionType = 3,
//                        blank = blank,
//                        blankStart = blankStart
//                    )
//                )
//            }

        }
        creatQuestion()
    }


    fun creatEnQuestion() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            getCurData()
            //英文选义
            errorList.forEach { word ->
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
        }
        creatQuestion()
    }

    fun creatSelectList(state: Int, blank: String = "abcd"): ArrayList<WordQuestionSelect> {
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
                word = wordsList[index1], state = state, isAnswer = false, blank = creatBlank(blank)
            )
        )
        selectList.add(
            WordQuestionSelect(
                word = wordsList[index2], state = state, isAnswer = false, blank = creatBlank(blank)
            )
        )
        selectList.add(
            WordQuestionSelect(
                word = wordsList[index3], state = state, isAnswer = false, blank = creatBlank(blank)
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
            delay(600)
        }
        if (curIndex == questionList.size) {
            _finishData.value = true
            wordsIndex++
        } else {
            val data = questionList[curIndex]
            data.selectList.forEach {
                it.isClick = false
            }
            _questionData.value = data
            curIndex++
        }

    }

    /**
     * 答题
     * @param answer Boolean
     * @param wordQuestion WordQuestion
     */
    fun onSelectError(wordQuestion: WordQuestion) {
        questionList.add(wordQuestion)
        insertError(wordQuestion)
    }


    /**
     * 获取错误单词
     * @return LiveData<List<Word>>
     */
    fun getError() = viewModelScope.launch {
        _errorData.value = repository.getError()
    }

    /**
     * 插入错题
     * @param error WordQuestion
     * @return LiveData<String>
     */
    fun insertError(error: WordQuestion) {
        repository.insertError(error)
    }

    /**
     * 删除错题
     */
    fun removeError(error: WordQuestion) {
        repository.removeError(error)
    }
}
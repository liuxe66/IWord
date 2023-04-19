package com.liuxe.iword.ui.vm

import androidx.lifecycle.MutableLiveData
import com.liuxe.iword.base.BaseViewModel
import com.liuxe.iword.utils.PrefUtils
import com.liuxe.iword.data.bean.WordLevelQuestionFillBean
import com.liuxe.iword.data.bean.WordLevelQuestionSelectBean
import com.liuxe.iword.data.entity.Word

/**
 *  author : liuxe
 *  date : 2023/3/31 10:30
 *  description :
 */
class WordVM : BaseViewModel() {

    private var wordsList: MutableList<Word> by PrefUtils(
        PrefUtils.prefWordList, ArrayList()
    )
    private var wordsLevel by PrefUtils(PrefUtils.prefWordLevel, 1)

    private val _curQuestionLiveData = MutableLiveData<WordLevelQuestionSelectBean>()
    var curQuestionLiveData = _curQuestionLiveData

    var curIndex = 0

    var letter = "abcdefghijklmnopqrstuvwxyz"

    /**
     * 获取闯关单词集合
     * @param level Int
     * @return MutableList<WordBean>
     */
    fun getLevel() = wordsList.subList((wordsLevel - 1) * 10, wordsLevel * 10)


    /**
     * 开始闯关
     * 1.打乱单词顺序
     * 2.按照新的排序取单词，当选错答案时，记录错题(陌生)，在全部问题结束之后，重新显示错题(模糊)
     * 3.取单词，挖空填字母，考察拼写，记录错题(陌生)，在全部问题结束之后，重新显示错题(模糊)
     * 4。全部结束，闯关完成。同时错题记录在数据库
     */
    fun startGame() {
        curIndex = 0
        nextQuestion()
    }


    fun nextQuestion() {
        if (curIndex == getLevel().size) {
            curIndex = 0
        } else {
            creatSelectQuestion(curIndex)
            curIndex += 1
        }
    }

    /**
     * 选择题
     * @param index Int
     */
    fun creatSelectQuestion(index: Int) {
        var index1 = (100 until wordsList.size - 100).random()
        var index2 = (100 until wordsList.size - 100).random()
        var index3 = (100 until wordsList.size - 100).random()

        while (index1 == index2) {
            index2 = (100 until wordsList.size - 100).random()
        }

        while (index1 == index3 || index2 == index3) {
            index3 = (100 until wordsList.size - 100).random()
        }

        var selectList = ArrayList<String>()
        selectList.add(wordsList[index1].trans!!)
        selectList.add(wordsList[index2].trans!!)
        selectList.add(wordsList[index3].trans!!)
        selectList.add(getLevel()[index].trans!!)
        selectList.shuffle()

        _curQuestionLiveData.value = WordLevelQuestionSelectBean(
            question = getLevel()[index].name!!,
            selectList = selectList,
            answer = getLevel()[index].trans!!
        )
    }

    /**
     * 创建填空题
     */
    fun creatInputQuestion(index: Int) {

        val question = getLevel()[index].name!!

        val selectList = ArrayList<Char>()
        var fillList = ArrayList<Int>()
        var input1: Int = question.indices.random()
        var input2: Int = question.indices.random()
        while (input1 == input2) {
            input2 = question.indices.random()
        }
        if (question.length > 5) {
            fillList.add(input1)
            fillList.add(input2)
            selectList.add(question[input1])
            selectList.add(question[input2])
            selectList.add(letter.random())
            selectList.add(letter.random())
            selectList.add(letter.random())
            selectList.add(letter.random())

        } else {
            selectList.add(question[input1])
            selectList.add(letter.random())
            selectList.add(letter.random())
            selectList.add(letter.random())
            selectList.add(letter.random())
            selectList.add(letter.random())
        }

        var fill = WordLevelQuestionFillBean(
            question = question, fillList = fillList, selectList = selectList
        )
    }


    fun onSelect(position: Int, data: WordLevelQuestionSelectBean) {
        if (data.selectList[position] == data.answer) {
            //选对了
            data.state = 2
        } else {
            //选错了
            data.state = 1
        }

        nextQuestion()
    }

    /**
     *
     */
    fun onInput(position: Int, curIndex: Int) {

    }

    /**
     * 背单词记录统计
     * 统计内容：背了多少单词，背了几天，今日目标
     * 在用户系统中记录
     */
    fun getUserWordData() {

    }
}
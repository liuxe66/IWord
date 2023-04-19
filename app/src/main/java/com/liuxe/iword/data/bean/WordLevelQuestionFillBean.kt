package com.liuxe.iword.data.bean

/**
 *  author : liuxe
 *  date : 2023/4/11 10:36
 *  description :
 */
class WordLevelQuestionFillBean(
    val question: String,
    val fillList:List<Int>,
    val selectList: List<Char>,
    var state:Int = 0 //状态 0 没学 1 模糊 2 学会了
)
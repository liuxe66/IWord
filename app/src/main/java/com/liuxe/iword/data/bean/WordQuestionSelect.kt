package com.liuxe.iword.data.bean

import com.liuxe.iword.data.entity.Word

/**
 *  author : liuxe
 *  date : 2023/4/20 17:04
 *  description :
 */
class WordQuestionSelect(
    //单词
    val word: Word,
    //是否是答案
    val isAnswer: Boolean = false,
    //作为选项的时候用到
    var state: Int? = 1, //1 英文选义 2 中文选词
)

package com.liuxe.iword.data.bean

import android.os.Parcelable
import com.liuxe.iword.data.entity.Word
import kotlinx.parcelize.Parcelize

/**
 *  author : liuxe
 *  date : 2023/4/20 09:18
 *  description :
 */
@Parcelize
class WordQuestion(
    val word: Word, //考察单词
    val selectList: List<WordQuestionSelect>, //选项集合
    var questionType: Int = 1,//问题类型 1 英文选义 2 中文选词 3 听音选词
    var blank: String? = null,// 填空题用到
    var blankStart: Int? = null
):Parcelable

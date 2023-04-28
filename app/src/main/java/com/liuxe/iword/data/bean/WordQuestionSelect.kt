package com.liuxe.iword.data.bean

import android.os.Parcelable
import com.liuxe.iword.data.entity.Word
import kotlinx.parcelize.Parcelize

/**
 *  author : liuxe
 *  date : 2023/4/20 17:04
 *  description :
 */
@Parcelize
class WordQuestionSelect(
    //选择题选项
    val word: Word,
    //是否是答案
    val isAnswer: Boolean = false,
    //作为选项的时候用到
    var state: Int? = 1, //1 英文选义 2 中文选词 3 选词填空
    //填空题用到
    var blank: String = "",
    //是否点击
    var isClick: Boolean = false
):Parcelable

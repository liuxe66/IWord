package com.liuxe.iword.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  author : liuxe
 *  date : 2023/4/25 09:03
 *  description :
 */
@Parcelize
class WordQuestionListBean(
    var wordQuestionList: List<WordQuestion>? = null
) : Parcelable
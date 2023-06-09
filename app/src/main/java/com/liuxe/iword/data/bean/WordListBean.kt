package com.liuxe.iword.data.bean

import android.os.Parcelable
import com.liuxe.iword.data.entity.Word
import kotlinx.parcelize.Parcelize


/**
 *  author : liuxe
 *  date : 2023/4/20 15:14
 *  description :
 */
@Parcelize
class WordListBean(
    var wordList:List<Word>? = null
):Parcelable
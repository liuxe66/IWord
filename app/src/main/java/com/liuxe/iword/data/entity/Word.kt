package com.liuxe.iword.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  author : liuxe
 *  date : 2023/4/18 16:46
 *  description :
 */

@Parcelize
class Word(
    val name: String? = null,
    val trans: String? = null,
    val ukphone: String? = null,
    val usphone: String? = null,
    var isError:Boolean = false
):Parcelable
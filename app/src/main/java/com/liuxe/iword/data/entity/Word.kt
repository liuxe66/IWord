package com.liuxe.iword.data.entity

import java.io.Serializable

/**
 *  author : liuxe
 *  date : 2023/4/18 16:46
 *  description :
 */

data class Word(
    val name: String? = null,
    val trans: String? = null,
    val ukphone: String? = null,
    val usphone: String? = null,
    var state: Int? = 0, //0 未学习 1 en2cn错题 2 cn2en错题 3 voice2cn错题 4 spell错题 5学会了
    var lastStudyTime: Long? = null
) : Serializable
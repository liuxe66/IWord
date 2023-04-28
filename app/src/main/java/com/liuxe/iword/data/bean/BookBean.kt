package com.liuxe.iword.data.bean

/**
 *  author : liuxe
 *  date : 2023/4/27 09:31
 *  description :
 */
data class BookBean(
    val bookName:String,
    val bookUrl:String,
    var isSelect:Boolean = false
)
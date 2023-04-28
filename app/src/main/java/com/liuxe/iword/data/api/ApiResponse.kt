package com.liuxe.iword.data.api

/**
 *  author : liuxe
 *  date : 5/17/21 10:43 AM
 *  description : 网络请求返回基本数据结构
 */
class ApiResponse<T>(
    var code: String,
    var msg: String? = null,
    var data: T? = null,
    val time:Int
)
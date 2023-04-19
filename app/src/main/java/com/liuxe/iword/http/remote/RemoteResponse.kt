package com.liuxe.iword.http.remote

/**
 *  author : liuxe
 *  date : 5/17/21 10:43 AM
 *  description : 网络请求返回基本数据结构
 */
class RemoteResponse<T>(
    var code: Int,
    var msg: String,
    var timeStamp:Long,
    var data: T

)
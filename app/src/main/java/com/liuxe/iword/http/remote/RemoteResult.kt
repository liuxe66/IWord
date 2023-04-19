package com.liuxe.iword.http.remote

import com.liuxe.iword.http.gson.ApiException

/**
 *  author : liuxe
 *  date : 2021/7/16 5:25 下午
 *  description : 处理成功或者失败的情况
 */
sealed class RemoteResult<out T> {
    //code = 0
    data class Success<out T>(val value: T) : RemoteResult<T>()

    //code != 0
    // 请求不成功 都走这个
    data class Failure(val throwable: ApiException) : RemoteResult<Nothing>()

}

inline fun <reified T> RemoteResult<T>.doSuccess(success: (T) -> Unit) {
    if (this is RemoteResult.Success) {
        success(value)
    }
}

inline fun <reified T> RemoteResult<T>.doFailure(failure: (ApiException) -> Unit) {
    if (this is RemoteResult.Failure) {
        failure(throwable)
    }
}


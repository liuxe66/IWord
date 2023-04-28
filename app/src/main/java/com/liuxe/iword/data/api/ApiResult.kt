package com.liuxe.iword.data.api

import com.liuxe.iword.http.gson.ApiException

/**
 *  author : liuxe
 *  date : 2021/7/16 5:25 下午
 *  description : 处理成功或者失败的情况
 */
sealed class ApiResult<out T> {
    //code = 0
    data class Success<out T>(val value: T) : ApiResult<T>()

    //code != 0
    // 请求不成功 都走这个
    data class Failure(val throwable: ApiException) : ApiResult<Nothing>()

}

inline fun <reified T> ApiResult<T>.doSuccess(success: (T) -> Unit) {
    if (this is ApiResult.Success) {
        success(value)
    }
}

inline fun <reified T> ApiResult<T>.doFailure(failure: (ApiException) -> Unit) {
    if (this is ApiResult.Failure) {
        failure(throwable)
    }
}


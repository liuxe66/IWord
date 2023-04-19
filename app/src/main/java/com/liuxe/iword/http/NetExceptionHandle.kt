package com.liuxe.iword.http

import com.google.gson.JsonParseException
import com.liuxe.iword.http.gson.ApiException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException


/**
 *  author : liuxe
 *  date : 2021/7/29 6:10 下午
 *  description : 网络异常处理
 */
class NetExceptionHandle {
    companion object {
        fun handleException(e: Throwable): ApiException {
            when (e) {

                //自定义异常，处理返回code != 200的情况
                is ApiException -> {
                    return ApiException(e.code,e.msg)
                }

                is HttpException -> {

                    var msg= when(e.code()){
                        401 -> "文件未授权或证书错误"
                        403 -> "服务器拒绝请求"
                        404 -> "服务器找不到请求的文件"
                        408 -> "请求超时，服务器未响应"
                        500 -> "服务器内部错误）服务器遇到错误，无法完成请求。"
                        502 -> "服务器从上游服务器收到无效响应。"
                        503 -> "服务器目前无法使用"
                        504 -> "服务器从上游服务器获取数据超时"
                        else -> "服务器错误"
                    }

                    return ApiException(e.code(),msg)
                }

                is JsonParseException -> {
                    return ApiException(msg = "json参数错误")
                }

                is ConnectException -> {
                    return ApiException(msg = "连接失败")
                }
                is java.net.SocketTimeoutException -> {
                    return ApiException(msg = "服务器响应超时")
                }
                is UnknownHostException -> {
                    return ApiException(msg = "网络未连接")
                }
                is JSONException -> {
                    return ApiException(msg = "json解析异常")
                }

                else -> {
                    return  if (e.message == null) ApiException(msg = "未知错误") else ApiException(msg = e.message?:"")
                }
            }
        }
    }
}
package com.liuxe.iword.http

import com.liuxe.iword.utils.LogUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * author : liuxe
 * date : 2021/9/1 2:41 下午
 * desription : 网络请求日志打印，重新获取token
 */
class NetLogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        val request: Request = chain.request()

        val method = request.method
        val t1 = System.nanoTime() //请求发起的时间
        val response: Response = chain.proceed(request)
        val t2 = System.nanoTime() //收到响应的时间
        val responseBody = response.peekBody((1024 * 1024).toLong())


        if ("POST" === method || "PUT" === method) {
            if (request.body is FormBody) {
                val jsonObject = JSONObject()
                val body = request.body as FormBody?
                if (body != null) {
                    for (i in 0 until body.size) {
                        try {
                            jsonObject.put(body.name(i), body.encodedValue(i))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
                LogUtils.e(
                    String.format(
                        "Retrofit接收响应: %n发送请求:%s  %nRequestParams:%s  %n耗时：%.1fms %n返回json:%s",
                        response.request.url,
                        jsonObject.toString(),
                        (t2 - t1) / 1e6,
                        responseBody.string()
                    )
                )
            } else {
                val buffer = Buffer()
                val requestBody = request.body
                if (requestBody != null) {
                    request.body!!.writeTo(buffer)
                    val body = buffer.readUtf8()
                    LogUtils.e(
                        String.format(
                            "Retrofit接收响应: %n发送请求:%s  %nRequestParams:%s  %n耗时：%.1fms %n返回json:%s",
                            response.request.url,
                            body,
                            (t2 - t1) / 1e6,
                            responseBody.string()
                        )
                    )
                }
            }
        } else {
            LogUtils.e(
                String.format(
                    "Retrofit接收响应: %n发送请求:%s  %n耗时：%.1fms %n返回json:%s",
                    response.request.url,
                    (t2 - t1) / 1e6,
                    responseBody.string()
                )
            )
        }


        return response
    }

}
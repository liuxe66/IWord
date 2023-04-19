package com.liuxe.iword.http

import com.liuxe.iword.http.gson.CheckGsonConverterFactory
import com.liuxe.iword.utils.HttpLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetWorkHelper {

    /**
     * okhttp实例
     *  请求头签名加密
     * @return OkHttpClient
     */
    private fun provideOkHttpClient(): OkHttpClient {
        //拦截日志 打印请求头
        val httpLogInterceptor = HttpLoggingInterceptor(HttpLogger())
        httpLogInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //初始化okhttp
        val builder = OkHttpClient.Builder()

        builder.connectTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLogInterceptor)
            .addInterceptor(NetLogInterceptor())
            .addInterceptor { chain ->

                val timestamp = System.currentTimeMillis().toString()

                val request = chain.request()
                    .newBuilder()
                    .header("Timestamp", timestamp)
                    .build()

                return@addInterceptor chain.proceed(request)
            }

        return builder.build()
    }


    /**
     * 获取Retrofit实例
     * @return Retrofit
     */
    fun provideRetrofit(): Retrofit {
        val okHttpClient = provideOkHttpClient()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("")
            .addConverterFactory(CheckGsonConverterFactory.create())
            .build()
    }


}

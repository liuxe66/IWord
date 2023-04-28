package com.liuxe.iword.http

import android.text.TextUtils
import com.blankj.utilcode.util.NetworkUtils
import com.liuxe.iword.app.IWordApp
import com.liuxe.iword.http.interceptor.NetLogInterceptor
import com.liuxe.iword.utils.HttpLogger
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
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


        val cacheFile = File(IWordApp.context.cacheDir.path , "http_cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 10) //缓存文件为10MB
        builder.cache(cache)


        builder.connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(OfflineInterceptor())
            .addNetworkInterceptor(OnlineInterceptor())

        return builder.build()
    }


    /**
     * 获取Retrofit实例
     * @return Retrofit
     */
    fun provideRetrofit(): Retrofit {
        val okHttpClient = provideOkHttpClient()
        return Retrofit.Builder().client(okHttpClient).baseUrl("https://qwerty.kaiyi.cool/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }



    /**
     * 有网络的时候
     */
    class OnlineInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            val onlineCacheTime = 0 //在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
            return response.newBuilder()
                .header("Cache-Control", "public, max-age=$onlineCacheTime")
                .removeHeader("Pragma")
                .build()
        }
    }

    /**
     * 没有网的时候
     */
    class OfflineInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            if (!NetworkUtils.isConnected()) {
                //从缓存取数据
                val newRequest = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
                val maxTime = 60 * 60 * 24
                val response = chain.proceed(newRequest)
                return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxTime")
                    .build()
            }
            return chain.proceed(request)
        }
    }



}

package com.liuxe.iword.utils

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

/**
 *  author : liuxe
 *  date : 2021/7/13 4:40 下午
 *  description :
 */
class HttpLogger: HttpLoggingInterceptor.Logger{

    override fun log(message: String) {
        message?.let { Log.i("ihomelog",it) }
    }
}
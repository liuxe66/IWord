package com.liuxe.iword.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.liuxe.iword.http.gson.ApiException

/**
 *  author : liuxe
 *  date : 2021/7/12 2:24 下午
 *  description :
 */
abstract class BaseViewModel : ViewModel() {

    //消息提示
    val mMsg:MutableLiveData<String> = MutableLiveData()
    //消息提示
    val mException:MutableLiveData<ApiException> = MutableLiveData()

}
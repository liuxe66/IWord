package com.liuxe.iword.base

import com.liuxe.iword.data.api.ApiResult
import com.liuxe.iword.http.NetExceptionHandle
import com.liuxe.iword.http.NetWorkHelper
import com.liuxe.iword.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 *  author : liuxe
 *  date : 2021/7/16 1:27 下午
 *  description :
 */
abstract class BaseRepository {

    /**
     * 网络请求
     * @param request SuspendFunction0<RemoteResponse<T>>
     * @return Flow<RemoteResult<RemoteResponse<T>>>
     */
    suspend fun <T> tryCatch(request: suspend () -> T) = flow {
        try {
            val data = request()
            //状态码 0 成功 其他都是出现错误
            emit(ApiResult.Success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            val response = NetExceptionHandle.handleException(e)
            emit(ApiResult.Failure(response))
            LogUtils.i("Exception：${response.code} ${response.msg}")
        }
    }.flowOn(Dispatchers.IO)


    /**
     * 初始化默认的apiService
     */
    inline fun <reified Api> createApi(): Api {
        return NetWorkHelper.provideRetrofit().create(Api::class.java)
    }

}
package com.liuxe.iword.data.api

import com.liuxe.iword.data.bean.WordBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 *  author : liuxe
 *  date : 2023/4/26 17:48
 *  description :
 */
interface ApiService {
    /**
     * 获取单词
     * https://qwerty.kaiyi.cool/dicts/GaoKao_3500.json
     */
    @Headers("Cache-Control: public, max-age=" + 24*60*60*10)
    @GET("dicts/{name}")
    suspend fun getAllFoods(
        @Path("name") name: String
    ): List<WordBean>

}
package com.liuxe.iword.data.repository

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liuxe.iword.app.IWordApp
import com.liuxe.iword.base.BaseRepository
import com.liuxe.iword.data.bean.WordBean
import com.liuxe.iword.data.bean.WordListBean
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.utils.PrefUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

/**
 *  author : liuxe
 *  date : 2023/4/18 17:28
 *  description : 单词数据仓库
 */
class WordRepository : BaseRepository() {

    private var wordsIndex by PrefUtils(PrefUtils.prefWordIndex, 1)
    private var wordsList by PrefUtils(PrefUtils.prefWordList,"")

    /**
     * 加载单词
     * @param fileName String
     * @return Flow<String>
     */
    suspend fun loadWord(fileName: String = "CET4_T.json") = flow {
        // 解析Json数据
        val newstringBuilder = StringBuilder()
        var inputStream: InputStream? = null
        try {
            inputStream = IWordApp.context.assets.open(fileName)
            val isr = InputStreamReader(inputStream)
            val reader = BufferedReader(isr)
            var jsonLine: String?
            while (reader.readLine().also { jsonLine = it } != null) {
                newstringBuilder.append(jsonLine)
            }
            reader.close()
            isr.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val str = newstringBuilder.toString()
        val gson = Gson()
        val wordType: Type = object : TypeToken<List<WordBean>>() {}.type
        var wordBeanList: MutableList<WordBean> = gson.fromJson(str, wordType)
        val tempWordList = ArrayList<Word>()
        wordBeanList.forEach {
            val word = Word(
                name = it.name, trans = it.trans!![0], usphone = it.usphone, ukphone = it.ukphone
            )
            tempWordList.add(word)
        }

        var wordListBean = WordListBean(tempWordList)
        wordsList = Gson().toJson(wordListBean)
        emit("success")
    }.flowOn(Dispatchers.IO)


    /**
     * 清空单词
     * @return Flow<String>
     */
    suspend fun clearWord() = flow {
        wordsIndex = 1
        emit("success")
    }.flowOn(Dispatchers.IO)

}
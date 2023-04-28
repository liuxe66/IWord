package com.liuxe.iword.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liuxe.iword.app.IWordApp
import com.liuxe.iword.base.BaseRepository
import com.liuxe.iword.data.Consts
import com.liuxe.iword.data.api.ApiService
import com.liuxe.iword.data.bean.*
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.http.gson.ApiException
import com.liuxe.iword.utils.MmkvUtils
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
    private val api: ApiService = createApi()
    private var wordsIndex by PrefUtils(PrefUtils.PREFWORDINDEX, 1)
    private var curBook by PrefUtils(Consts.CUR_BOOK,Consts.cet4)
    suspend fun loadBooks() = flow<List<BookBean>> {
        var data = ArrayList<BookBean>()

        data.add(BookBean(Consts.cet4CN,Consts.cet4,curBook == Consts.cet4))
        data.add(BookBean(Consts.cet6CN,Consts.cet6,curBook == Consts.cet6))
        data.add(BookBean(Consts.gaokaoCN,Consts.gaokao,curBook == Consts.gaokao))
        data.add(BookBean(Consts.kaoyanCN,Consts.kaoyan,curBook == Consts.kaoyan))

        emit(data)
    }.flowOn(Dispatchers.IO)

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
            if (it.name?.length!! >= 2) {
                val word = Word(
                    name = it.name,
                    trans = it.trans!![0],
                    usphone = it.usphone,
                    ukphone = it.ukphone
                )
                tempWordList.add(word)
            }
        }
        //mmkv存储
        MmkvUtils.encodeParcelable(Consts.ALL_WORD, WordListBean(tempWordList))
        emit("success")
    }.flowOn(Dispatchers.IO)


    suspend fun loadNetWords(bookName:String) = tryCatch {
        api.getAllFoods(bookName)
    }

    /**
     * 清空单词
     * @return Flow<String>
     */
    suspend fun clearWord() = flow {
        wordsIndex = 1
        emit("success")
    }.flowOn(Dispatchers.IO)


    /**
     * 插入错题
     */
    fun insertError(error: WordQuestion) {
        var data = getError()
        var word = error.word
        word.isError = true

        data.forEach {
            if (it.name == error.word.name) {
                return
            }
        }

        data.add(0, word)
        MmkvUtils.encodeParcelable(Consts.ERROR_WORD, WordListBean(data))

    }

    /**
     * 删除错题
     */
    fun removeError(error: WordQuestion) {
        var data = getError()
        data.remove(error.word)
        MmkvUtils.encodeParcelable(Consts.ERROR_WORD, WordListBean(data))
    }

    /**
     * 获取错题
     */
    fun getError(): ArrayList<Word> {
        var data = MmkvUtils.decodeParcelable(Consts.ERROR_WORD, WordListBean::class.java)
        return if (data?.wordList == null) {
            ArrayList()
        } else {
            data.wordList!! as ArrayList<Word>
        }
    }

}
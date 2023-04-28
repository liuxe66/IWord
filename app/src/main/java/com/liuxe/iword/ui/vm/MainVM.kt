package com.liuxe.iword.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.liuxe.iword.base.BaseViewModel
import com.liuxe.iword.data.Consts
import com.liuxe.iword.data.api.doFailure
import com.liuxe.iword.data.api.doSuccess
import com.liuxe.iword.data.bean.WordListBean
import com.liuxe.iword.data.entity.Word
import com.liuxe.iword.data.repository.RepositoryFactory
import com.liuxe.iword.utils.MmkvUtils
import com.liuxe.iword.utils.PrefUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 *  author : liuxe
 *  date : 2023/3/22 16:33
 *  description :
 */
class MainVM : BaseViewModel() {
    private val repository = RepositoryFactory.makeWordRepository()

    private var _progressData = MutableLiveData<Int>()
    val progressData = _progressData
    fun loadBook() = liveData {
        repository.loadBooks().collectLatest {
            emit(it)
        }
    }

    /**
     * 单词数据库清除
     * @return LiveData<String>
     */
    fun clearWord() = liveData {
        repository.clearWord().collectLatest {
            emit(it)
        }
    }

    /**
     * 单词初始化
     * @return LiveData<String>
     */
    fun loadWord(fileName: String = "CET4_T.json") = liveData<String> {
        repository.loadNetWords(fileName).onStart {
            creatProgressBar()
            mLoadingState.value = true
        }.onCompletion {
            _progressData.value = 100
            mLoadingState.value = false
        }.collectLatest { res ->
            res.doSuccess { list ->
                val tempWordList = ArrayList<Word>()
                list.forEach {
                    if (it.name?.length!! > 2) {
                        val word = Word(
                            name = it.name,
                            trans = initTrans(it.trans!!),
                            usphone = it.usphone,
                            ukphone = it.ukphone
                        )
                        tempWordList.add(word)
                    }
                }
                if (fileName == Consts.gaokao){
                                    tempWordList.shuffle()
                }

                //mmkv存储
                MmkvUtils.encodeParcelable(Consts.ALL_WORD, WordListBean(tempWordList))
                emit("success")
            }
            res.doFailure {

            }
        }
    }

    fun initTrans(list: List<String>): String {
        var trans = ""
        for (position in list.indices){
            if (position == list.size -1){
                trans += list[position]
            } else {
                trans += list[position]+"\n"
            }
        }
        return list[0]
    }

    fun creatProgressBar() = viewModelScope.launch {
        //进度条 没100ms随机增加到[30-50]之间的随机数，之后每100ms增加【5-15】之间的随机数 当总进度>83时停止
        var pregress = 0
        _progressData.value = pregress
        delay(100)
        pregress = (10 until 30).random()
        _progressData.value = pregress
        while (pregress < 90) {
            delay(200)
            pregress += (5 until 10).random()
            _progressData.value = pregress
        }
        delay(100)
        _progressData.value = 98
    }

}
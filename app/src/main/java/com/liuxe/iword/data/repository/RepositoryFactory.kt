package com.liuxe.iword.data.repository

import com.liuxe.iword.base.BaseRepository

object RepositoryFactory {

    fun makeWordRepository() = WordRepository()
    fun makeUserRepository() = UserRepository()
}
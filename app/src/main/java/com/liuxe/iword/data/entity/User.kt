package com.liuxe.iword.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : liuxe
 *  date : 2023/4/19 09:52
 *  description :
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val userName: String,
    var wordNum: Int,
    var wordBook:String,//cet4 cet6
    var studyDays:Int//学习多少天了
)
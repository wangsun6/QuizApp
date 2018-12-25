package com.wangsun.android.qaapp.sqlite.tables


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "question_table")
data class Question(
    var quest: String,

    var option1: String,

    var option2: String,

    var option3: String,

    var answer: Int){

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
package com.wangsun.android.qaapp.sqlite.tables


import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


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
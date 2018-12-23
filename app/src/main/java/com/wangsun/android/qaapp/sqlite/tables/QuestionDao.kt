package com.wangsun.android.qaapp.sqlite.tables

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query
import io.reactivex.Flowable


@Dao
interface QuestionDao {

    @Insert
    fun insert(question: Question)

    @Update
    fun update(question: Question)

    @Query("SELECT * from question_table")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query("SELECT * FROM question_table WHERE id = :temp_id")
    fun searchQuestion(temp_id: Int): LiveData<List<Question>>
}
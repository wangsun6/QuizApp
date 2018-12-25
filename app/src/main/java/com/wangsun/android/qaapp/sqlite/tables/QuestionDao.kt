package com.wangsun.android.qaapp.sqlite.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.lifecycle.LiveData
import androidx.room.Query
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
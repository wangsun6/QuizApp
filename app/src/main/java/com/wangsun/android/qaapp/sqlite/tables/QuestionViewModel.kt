package com.wangsun.android.qaapp.sqlite.tables

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask
import android.arch.lifecycle.LiveData
import com.wangsun.android.qaapp.sqlite.Database1


class QuestionViewModel(application: Application): AndroidViewModel(application) {
    private var questionDao: QuestionDao? = null

    init{
        val questionDatabase = Database1.getInstance(application)
        questionDao = questionDatabase?.questionDao()
    }

    fun insert(newQuestion: Question) {
        questionDao?.let {
            InsertQuestionAsyncTask(it).execute(newQuestion)
        }
    }

    fun update(newQuestion: Question) {
        questionDao?.let {
            UpdateQuestionAsyncTask(it).execute(newQuestion)
        }
    }

    fun getAllQuestions(): LiveData<List<Question>>{
        questionDao?.let {
            return it.getAllQuestions()
        }
    }


    //**********INSERT******************
    class InsertQuestionAsyncTask(var questionDao: QuestionDao): AsyncTask<Question, Void, Void>(){
        override fun doInBackground(vararg params: Question?): Void? {
            params[0]?.let {
                questionDao.insert(it)
            }
            return null
        }
    }

    //**********UPDATE******************
    class UpdateQuestionAsyncTask(var questionDao: QuestionDao): AsyncTask<Question, Void, Void>(){
        override fun doInBackground(vararg params: Question?): Void? {
            params[0]?.let {
                questionDao.update(it)
            }
            return null
        }
    }

}
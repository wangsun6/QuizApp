package com.wangsun.android.qaapp.sqlite.tables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.wangsun.android.qaapp.sqlite.Database1


class MemberViewModel(application: Application): AndroidViewModel(application) {
    private var memberDao: MemberDao? = null

    init{
        val adminDatabase = Database1.getInstance(application)
        memberDao = adminDatabase?.memberDao()
    }

    fun getMember(email: String): LiveData<List<Member>>{
        memberDao?.let {
            return it.getMember(email)
        }
    }

    fun getMemberOnce(email: String):List<Member>{
        memberDao?.let {
            return it.getMemberOnce(email)
        }
    }

    fun getAllMembers(): LiveData<List<Member>>{
        memberDao?.let {
            return it.getAllMembers()
        }
    }

    fun insert(new_Member: Member) {
        memberDao?.let {
            InsertMemberAsyncTask(it).execute(new_Member)
        }
    }

    fun insertCallback(new_Member: Member): Long {
        memberDao?.let {
            return it.insertCallback(new_Member)
        }
    }

//    fun update(new_Member: Member) {
//        memberDao?.let {
//        }
//    }

    fun updateScorePosition(email: String, score: Int,position: Int,totalAttempted: Int): Int{
        memberDao?.let {
            return it.updateScorePosition(score,position,totalAttempted,email)
        }
    }


    fun updateProfile(name: String,phone: String, password: String, score: Int,totalAttempted: Int,email: String): Int{
        memberDao?.let {
            return it.updateProfile(name,phone,password,score,totalAttempted,email)
        }
    }

    //**********INSERT******************
    class InsertMemberAsyncTask(var memberDao: MemberDao): AsyncTask<Member, Void, Void>(){
        override fun doInBackground(vararg params: Member?): Void? {
            params[0]?.let {
                memberDao.insert(it)
            }
            return null
        }
    }


}
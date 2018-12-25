package com.wangsun.android.qaapp.sqlite.tables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.wangsun.android.qaapp.sqlite.Database1
import io.reactivex.Flowable


class AdminViewModel(application: Application): AndroidViewModel(application) {
    private var adminDao: AdminDao? = null

    init{
        val adminDatabase = Database1.getInstance(application)
        adminDao = adminDatabase?.adminDao()
    }

    fun getAdmin(email: String): LiveData<List<Admin>>{
        adminDao?.let {
            return it.getAdmin(email)
        }
    }

    fun getAdminOnce(email: String):List<Admin>{
        adminDao?.let {
            return it.getAdminOnce(email)
        }

    }

    fun getAllAdmins(): LiveData<List<Admin>>{
        adminDao?.let {
            return it.getAllAdmins()
        }
    }

    fun insert(new_Admin: Admin) {
        adminDao?.let {
            InsertAdminAsyncTask(it).execute(new_Admin)
        }
    }

    fun insertCallback(new_admin: Admin): Long {
        adminDao?.let {
            return it.insertCallback(new_admin)
        }
    }

    fun update(new_Admin: Admin): Int {
        adminDao?.let {
            return it.update(new_Admin)
        }
    }

    //**********INSERT******************
    class InsertAdminAsyncTask(var adminDao: AdminDao): AsyncTask<Admin, Void, Void>(){
        override fun doInBackground(vararg params: Admin?): Void? {
            params[0]?.let {
                adminDao.insert(it)
            }
            return null
        }
    }

}
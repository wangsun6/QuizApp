package com.wangsun.android.qaapp.sqlite.tables

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query


@Dao
interface AdminDao {

    @Insert
    fun insert(admin: Admin)

    @Insert
    fun insertCallback(admin: Admin): Long

    @Update
    fun update(admin: Admin): Int

    @Query("SELECT * FROM admin_table WHERE email = :e1")
    fun getAdmin(e1: String): LiveData<List<Admin>>

    @Query("SELECT * FROM admin_table WHERE email = :e1")
    fun getAdminOnce(e1: String): List<Admin>

    @Query("SELECT * from admin_table")
    fun getAllAdmins(): LiveData<List<Admin>>
}
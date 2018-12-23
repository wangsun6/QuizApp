package com.wangsun.android.qaapp.sqlite.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey




@Entity(tableName = "admin_table")
data class Admin(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String){

}
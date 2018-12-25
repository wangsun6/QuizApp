package com.wangsun.android.qaapp.sqlite.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "admin_table")
data class Admin(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String){

}
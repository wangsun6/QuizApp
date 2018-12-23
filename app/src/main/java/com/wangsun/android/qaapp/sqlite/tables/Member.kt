package com.wangsun.android.qaapp.sqlite.tables

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "member_table")
data class Member(
    @PrimaryKey(autoGenerate = false) var email: String,
    var name: String,
    var phone: String,
    var password: String,
    var score: Int = 0,
    var questPosition: Int = 0,
    var totalAttempted: Int = 0): Serializable {

    companion object {
        val ARG_BUNDLE = javaClass.canonicalName + ".bundle_arg"
    }
}
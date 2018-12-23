package com.wangsun.android.qaapp.sqlite.tables

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query


@Dao
interface MemberDao {

    @Insert
    fun insert(member: Member)

    @Insert
    fun insertCallback(member: Member): Long

    @Update
    fun update(member: Member)


    @Query("UPDATE member_table SET score =:tempScore,questPosition =:tempPosition,totalAttempted =:tempAttempted WHERE email =:tempEmail")
    fun updateScorePosition(tempScore: Int,tempPosition: Int,tempAttempted: Int, tempEmail: String): Int

    @Query("UPDATE member_table SET name =:tempName, phone =:tempPhone, password =:tempPassword,score =:tempScore,totalAttempted =:tempAttempted WHERE email =:tempEmail")
    fun updateProfile(tempName: String, tempPhone: String, tempPassword: String,tempScore: Int,tempAttempted: Int, tempEmail: String): Int

    @Query("SELECT * from member_table")
    fun getAllMembers(): LiveData<List<Member>>

    @Query("SELECT * FROM member_table WHERE email = :e1")
    fun getMember(e1: String): LiveData<List<Member>>

    @Query("SELECT * FROM member_table WHERE email = :e1")
    fun getMemberOnce(e1: String): List<Member>

}
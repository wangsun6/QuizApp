package com.wangsun.android.qaapp.sqlite

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.os.AsyncTask
import com.wangsun.android.qaapp.sqlite.tables.*

@Database(entities = arrayOf(
    Admin::class,
    Member::class,
    Question::class), version = 1, exportSchema = false)
abstract class Database1: RoomDatabase() {

    abstract fun adminDao(): AdminDao
    abstract fun memberDao(): MemberDao
    abstract fun questionDao(): QuestionDao

    companion object {
        private var instance: Database1? = null

        fun getInstance(context: Context): Database1? {
            if (instance == null) {
                synchronized(Database1::class) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), Database1::class.java, "admin.db")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                instance?.let {

                    PopulateAsyncTask(it).execute()
                    PopulateAsyncTask2(it).execute()
                    PopulateAsyncTask3(it).execute()

                }
            }
        }

        private class PopulateAsyncTask(db: Database1) : AsyncTask<Void, Void, Void>() {

            private val adminDao: AdminDao = db.adminDao()
            override fun doInBackground(vararg voids: Void): Void? {
                adminDao.insert(Admin("admin@gmail.com", "p"))
                return null
            }
        }


        private class PopulateAsyncTask2(db: Database1) : AsyncTask<Void, Void, Void>() {

            private val memberDao: MemberDao = db.memberDao()
            override fun doInBackground(vararg voids: Void): Void? {
                memberDao.insert(Member("member@gmail.com", "member","phone","p",0,0,0))
                return null
            }
        }


        private class PopulateAsyncTask3(db: Database1) : AsyncTask<Void, Void, Void>() {

            private val questionDao: QuestionDao = db.questionDao()

            override fun doInBackground(vararg voids: Void): Void? {
                questionDao.insert(Question("Answer 1 is correct","Answer-1",
                    "Answer-2", "Answer-3", 1))
                questionDao.insert(Question("Answer 2 is correct","Answer-1",
                    "Answer-2", "Answer-3", 2))
                questionDao.insert(Question("Answer 3 is correct","Answer-1",
                    "Answer-2", "Answer-3", 3))
                questionDao.insert(Question("Answer 1 is correct","Answer-1",
                    "Answer-2", "Answer-3", 1))
                questionDao.insert(Question("Answer 2 is correct","Answer-1",
                    "Answer-2", "Answer-3", 2))
                return null
            }
        }
    }
}
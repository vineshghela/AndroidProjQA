package com.example.qa_todo_app.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class TaskDbHelper(context: Context?) :
    SQLiteOpenHelper(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE)
        onCreate(db)
    }
}
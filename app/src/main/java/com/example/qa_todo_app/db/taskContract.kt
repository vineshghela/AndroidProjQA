package com.example.qa_todo_app.db

import android.provider.BaseColumns


object TaskContract {
    const val DB_NAME = "com.example.qa_todo_app.db"
    const val DB_VERSION = 1

    object TaskEntry : BaseColumns {
        val _ID = BaseColumns._ID
        const val TABLE = "tasks"
        const val COL_TASK_TITLE = "title"
    }
}
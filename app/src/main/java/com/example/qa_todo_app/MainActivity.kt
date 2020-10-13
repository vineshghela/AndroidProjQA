package com.example.qa_todo_app

import android.app.AlertDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.qa_todo_app.db.TaskContract
import com.example.qa_todo_app.db.TaskDbHelper


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var mHelper: TaskDbHelper = TaskDbHelper(this)
    var mTaskListView: ListView? = null

    //    val mAdapter: ArrayAdapter<*>
    private var mAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHelper = TaskDbHelper(this)
        mTaskListView = findViewById<ListView>(R.id.list_to_do)
        updateUI()

    }

    fun updateUI() {
        val taskList: ArrayList<String> = ArrayList()
        val db = mHelper.readableDatabase
        val cursor = db.query(
            TaskContract.TaskEntry.TABLE,
            arrayOf(TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE),
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)
            Log.d("ID",idx.toString())
            val something = BaseColumns._ID
            Log.d("OtherID", something)
            taskList.add(cursor.getString(idx))
        }

        if (mAdapter == null) {
            mAdapter = ArrayAdapter(
                this,
                R.layout.item_todo,
                R.id.task_title,
                taskList
            )
            mTaskListView?.adapter = mAdapter
        } else {
            mAdapter?.clear()
            mAdapter?.addAll(taskList)
            mAdapter?.notifyDataSetChanged()
        }

        cursor.close()
        db.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                val taskEditText = EditText(this)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Add a new task!")
                    .setView(taskEditText)
                    .setPositiveButton(
                        "Add"
                    ) { dialog, which ->
                        val task = taskEditText.text.toString()
                        val db = mHelper.writableDatabase
                        val values = ContentValues()
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task)
                        db.insertWithOnConflict(
                            TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE
                        )
                        db.close()
                        updateUI()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun deleteTask(view: View) {
        val parent = view.parent as View
        val taskTextView = parent.findViewById<View>(R.id.task_title) as TextView
        val task = taskTextView.text.toString()
        val db = mHelper.writableDatabase
        db.delete(
            TaskContract.TaskEntry.TABLE,
            TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", arrayOf(task)
        )
        db.close()
        updateUI()
    }

    fun updateTask(view: View){
        val parent = view.parent as View
        val taskTextView = parent.findViewById<View>(R.id.task_title) as TextView
        val task = taskTextView.text.toString()
        val message = "Current todo is: "

        Log.d(TAG, "Task: " + task);

        val taskEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Update task!")
            .setView(taskEditText)
            .setMessage(message+task)
            .setPositiveButton(
                "Add"
            )
            { dialog, which ->
                val task = taskEditText.text.toString()
                val db = mHelper.writableDatabase
                val values = ContentValues()
                val check = taskEditText.id
                val id = TaskContract.TaskEntry._ID
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task)
                db.update(
                    TaskContract.TaskEntry.TABLE,
                    null,
                    "$\\s COLUMN_ID=?",
                    arrayOf(id)
                )
                db.close()
                updateUI()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
        true
    }
}






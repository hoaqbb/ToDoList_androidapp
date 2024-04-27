package com.example.todo_list.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todo_list.model.Task

class DbHelper (context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){
        companion object{
            const val DATABASE_NAME = "db_todo"
            const val DATABASE_VERSION = 1
            const val TABLE_NAME = "todo_list"
            //column name for table
            const val id_col = "id"
            const val title_col = "title"
            const val status_col = "status"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                    id_col + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    title_col + " TEXT, " +
                    status_col + " INTEGER" + ")")
            db?.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        //get all task
        @SuppressLint("Range", "Recycle") //một xíu báo đỏ tự động add vào là tự động add dữ chưa
        fun readTask(): List<Task> {
            val rs: ArrayList<Task> = ArrayList()
            val sql = "SELECT * FROM $TABLE_NAME"
            val db = this.readableDatabase
            var cursor: Cursor? = null
            //read data to cursor
            try {
                cursor = db.rawQuery(sql, null)
            } catch (e: SQLException) {
                db.execSQL(sql)
                return ArrayList()
            }
            //iteration on cursor
            if(cursor.moveToFirst()) {
                do {
                    val task = Task(
                        cursor.getInt(cursor.getColumnIndex(id_col)),
                        cursor.getString(cursor.getColumnIndex(title_col)),
                        cursor.getInt(cursor.getColumnIndex(status_col))
                    )
                    rs.add(task)
                }while (cursor.moveToNext())
            }
            return rs
        }

        //add task
        fun addTask(task: Task): Long {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(title_col, task.title)
                put(status_col, 0)
            }
            val rs = db.insert(TABLE_NAME, null, values)
            db.close()
            return rs
        }

        //update task by title
        fun updateStatus(taskId: Int, status: Int): Int {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(status_col, status)
            }
            val whereClause = "${id_col}='" + taskId + "'"
            val rs = db.update(TABLE_NAME, values, whereClause, null)
            db.close()
            return rs
        }

        fun editTask(taskId: Int, newtitle: String): Int{
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(title_col, newtitle)
            }
            val whereClause = "${id_col}='" + taskId + "'"
            val rs = db.update(TABLE_NAME, values, whereClause, null)
            db.close()
            return rs
        }

        //delete task by id
        fun deleteTask(taskId: Int): Int {
            val db = this.writableDatabase
            val whereClause = "${id_col}='" + taskId + "'"
            val rs = db.delete(TABLE_NAME, whereClause, null)
            db.close()
            return rs
        }
    }

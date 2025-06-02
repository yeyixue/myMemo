package com.example.mymemo.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "user.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "user_table"
    }
    // 这个方法只在 数据库第一次创建时 调用，具体逻辑如下：
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                password TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 插入用户
    fun insertUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }

    // 检查用户名是否已存在
    fun isUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE username = ?",
            arrayOf(username)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }


    // 登录验证
    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // 查询所有用户（返回用户名列表）
    fun getAllUsers(): List<String> {
        val db = readableDatabase
        val list = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        while (cursor.moveToNext()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            list.add(username)
        }
        cursor.close()
        return list
    }

    // 删除用户
    fun deleteUserById(userId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "user_id = ?", arrayOf(userId.toString()))
        return result > 0
    }

    // 修改密码
    fun updatePassword(userId: Int, newPassword: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("password", newPassword)
        }
        val result = db.update(TABLE_NAME, values, "user_id = ?", arrayOf(userId.toString()))
        return result > 0
    }

    //更新密码
    fun updatePwd(username: String, newPassword: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("password", newPassword)
        }
        // 返回受影响的行数
        return db.update(TABLE_NAME, values, "username = ?", arrayOf(username))
    }

}

//val db = UserDbHelper(this)
//db.insertUser("admin", "123456")
//val isValid = db.checkUser("admin", "123456") // true

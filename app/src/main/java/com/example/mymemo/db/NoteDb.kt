package com.example.mymemo.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mymemo.entity.Noteinfo

class NoteDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "note.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "note_table"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                note_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                note_title TEXT,
                note_content TEXT,
                note_create_time TEXT,
                note_is_top INTEGER
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 插入笔记
    fun insertNote(username: String, title: String, content: String, createTime: String, isTop: Int = 0): Boolean {
        Log.d("NoteDbHelper", "insert into DB：username=$username title=$title")
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("note_title", title)
            put("note_content", content)
            put("note_create_time", createTime)
            put("note_is_top", isTop)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }

    // 查询所有笔记
    fun getAllNotes(): List<Map<String, String>> {
        val db = readableDatabase
        val list = mutableListOf<Map<String, String>>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY note_is_top DESC, note_create_time DESC", null)
        while (cursor.moveToNext()) {
            val map = mapOf(
                "note_id" to cursor.getInt(cursor.getColumnIndexOrThrow("note_id")).toString(),
                "username" to cursor.getString(cursor.getColumnIndexOrThrow("username")),
                "title" to cursor.getString(cursor.getColumnIndexOrThrow("note_title")),
                "content" to cursor.getString(cursor.getColumnIndexOrThrow("note_content")),
                "time" to cursor.getString(cursor.getColumnIndexOrThrow("note_create_time")),
                "isTop" to cursor.getInt(cursor.getColumnIndexOrThrow("note_is_top")).toString()
            )
            list.add(map)
        }
        cursor.close()
        return list
    }

    // 删除笔记
    fun deleteNoteById(noteId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "note_id = ?", arrayOf(noteId.toString()))
        return result > 0
    }

    // 更新置顶状态
    fun updateTopStatus(noteId: Int, isTop: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("note_is_top", isTop)
        }
        val result = db.update(TABLE_NAME, values, "note_id = ?", arrayOf(noteId.toString()))
        return result > 0
    }

    fun getNotesByUser(username: String): List<Noteinfo> {
        Log.d("NoteDbHelper", "find in DB：username=$username")
        val db = readableDatabase
        val list = mutableListOf<Noteinfo>()
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE username = ? ORDER BY note_is_top DESC, note_create_time DESC",
            arrayOf(username)
        )
        while (cursor.moveToNext()) {
            val note = Noteinfo(
                note_id = cursor.getInt(cursor.getColumnIndexOrThrow("note_id")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                note_title = cursor.getString(cursor.getColumnIndexOrThrow("note_title")),
                note_content = cursor.getString(cursor.getColumnIndexOrThrow("note_content")),
                note_create_time = cursor.getString(cursor.getColumnIndexOrThrow("note_create_time")),
                note_is_top = cursor.getInt(cursor.getColumnIndexOrThrow("note_is_top"))
            )
            list.add(note)
        }
        cursor.close()
        return list
    }

    fun getNoteById(noteId: Int): Noteinfo? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE note_id = ?", arrayOf(noteId.toString()))
        var note: Noteinfo? = null
        if (cursor.moveToFirst()) {
            note = Noteinfo(
                note_id = cursor.getInt(cursor.getColumnIndexOrThrow("note_id")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                note_title = cursor.getString(cursor.getColumnIndexOrThrow("note_title")),
                note_content = cursor.getString(cursor.getColumnIndexOrThrow("note_content")),
                note_create_time = cursor.getString(cursor.getColumnIndexOrThrow("note_create_time")),
                note_is_top = cursor.getInt(cursor.getColumnIndexOrThrow("note_is_top"))
            )
        }
        cursor.close()
        return note
    }

    //模糊匹配，
    fun searchNote(content: String): List<Noteinfo> {
        val db = readableDatabase
        val list = mutableListOf<Noteinfo>()
        //标题和内容一起查询
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE note_title LIKE ? OR note_content LIKE ? ORDER BY note_create_time DESC",
            arrayOf("%$content%", "%$content%")
        )

        while (cursor.moveToNext()) {
            val note = Noteinfo(
                note_id = cursor.getInt(cursor.getColumnIndexOrThrow("note_id")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                note_title = cursor.getString(cursor.getColumnIndexOrThrow("note_title")),
                note_content = cursor.getString(cursor.getColumnIndexOrThrow("note_content")),
                note_create_time = cursor.getString(cursor.getColumnIndexOrThrow("note_create_time")),
                note_is_top = cursor.getInt(cursor.getColumnIndexOrThrow("note_is_top"))
            )
            list.add(note)
        }
        cursor.close()
        return list
    }

    //更新
    fun editNote(noteId: Int, title: String, content: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("note_title", title)
            put("note_content", content)
        }
        val result = db.update(TABLE_NAME, values, "note_id = ?", arrayOf(noteId.toString()))
        return result > 0
    }

    // 删除指定用户的所有笔记
    fun deleteAllNotesByUsername(username: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "note_user = ?", arrayOf(username))
    }



}

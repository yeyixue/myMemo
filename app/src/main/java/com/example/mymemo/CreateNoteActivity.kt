package com.example.mymemo

import java.util.Locale
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.mymemo.base.BaseActivity
import com.example.mymemo.db.NoteDbHelper
import com.example.mymemo.entity.Noteinfo
import com.example.mymemo.entity.Userinfo
import java.text.SimpleDateFormat
import java.util.Date

class CreateNoteActivity : BaseActivity() {

    private lateinit var et_note_title: EditText
    private lateinit var et_note_content: EditText
    private lateinit var toolbar:Toolbar
    private lateinit var btn_submit:Button
    private var noteinfo: Noteinfo?=null
    override fun getLayoutResId(): Int {
        return R.layout.activity_create_note
    }

    override fun initViews() {
        et_note_title = findViewById(R.id.et_note_title)
        et_note_content = findViewById(R.id.et_note_content)
        btn_submit = findViewById(R.id.btn_submit)
        toolbar = findViewById(R.id.toolbar)
    }

    override fun setListener() {
        toolbar.setOnClickListener {
            finish()
        }

        btn_submit.setOnClickListener {
            val userInfo = Userinfo.getUserInfo()
            if (userInfo == null) {
                Toast.makeText(this, "用户信息为空，请重新登录", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val username = userInfo.getUsername()
            val title = et_note_title.text.toString().trim()
            val content = et_note_content.text.toString().trim()
            val createTime = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (noteinfo == null) {
                // 创建新笔记
                val success = NoteDbHelper(this).insertNote(username, title, content, createTime, isTop = 0)
                if (success) {
                    Toast.makeText(this, "创建成功~~", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "创建失败，请稍后再试", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 编辑已有笔记
                val success = NoteDbHelper(this).editNote(noteinfo!!.note_id, title, content)
                if (success) {
                    Toast.makeText(this, "修改成功~~", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "修改失败，请稍后再试", Toast.LENGTH_SHORT).show()
                }
            }
            setResult(1000)
            finish()

        }
    }

    override fun initData() {


        //跳转传值
        noteinfo = intent.getSerializableExtra("note") as? Noteinfo
        if (noteinfo != null) {
            et_note_title.setText(noteinfo!!.getNoteTitle())
            et_note_content.setText(noteinfo!!.getNoteContent())

            Log.d("et_note_content.text",et_note_content.text.toString())

        }else{
            Log.d("noteinfo","noteinfo is null")
        }
    }
}

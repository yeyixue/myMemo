package com.example.mymemo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import com.example.mymemo.base.BaseActivity
import com.example.mymemo.entity.Noteinfo


class NoteDetailActivity : BaseActivity() {
    private lateinit var tv_note_title:TextView
    private lateinit var tv_note_content:TextView
    private var noteinfo: Noteinfo? = null



    override fun getLayoutResId(): Int {
        return  R.layout.activity_note_detail
    }

    override fun initViews() {
        tv_note_title=findViewById(R.id.tv_note_title)
        tv_note_content=findViewById(R.id.tv_note_content)
    }

    override fun setListener() {
        findViewById<Toolbar>(R.id.toolbar).setOnClickListener{
            finish()
        }
    }

    override fun initData() {

        //跳转传值
        noteinfo = intent.getSerializableExtra("note") as? Noteinfo
        if (noteinfo != null) {
            tv_note_title.text = noteinfo!!.getNoteTitle()
            tv_note_content.text = noteinfo!!.getNoteContent()
            Log.d("tv_note_content.text",tv_note_content.text.toString())
//            tv_note_content.invalidate()
//            tv_note_content.requestLayout()

        }else{
            Log.d("noteinfo","noteinfo is null")
        }

    }
}
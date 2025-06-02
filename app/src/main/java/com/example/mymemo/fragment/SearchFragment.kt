package com.example.mymemo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemo.CreateNoteActivity
import com.example.mymemo.NoteDetailActivity
import com.example.mymemo.R
import com.example.mymemo.adapter.SearchListAdapter
import com.example.mymemo.base.BaseFragment
import com.example.mymemo.entity.Noteinfo
import com.example.mymemo.db.NoteDbHelper


class SearchFragment : BaseFragment() {

    private lateinit var et_search: EditText
    private lateinit var recyclerview:RecyclerView
    private lateinit var mListAdapter: SearchListAdapter
    private lateinit var ll_empty: LinearLayout
    private lateinit var createNoteLauncher: ActivityResultLauncher<Intent>

    override fun getLayoutResId(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {
        et_search=rootView.findViewById(R.id.et_search)
        recyclerview=rootView.findViewById(R.id.recyclerview)
        ll_empty=rootView.findViewById(R.id.ll_empty)


        //初始化适配器
        mListAdapter=SearchListAdapter()
        recyclerview.adapter=mListAdapter

        //recyclerview点击事件
        mListAdapter.setOnItemClickListener(object : SearchListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, noteInfo: Noteinfo) {
//                val intent = Intent(requireContext(), NoteDetailActivity::class.java)
//                // 如果你想把 noteInfo 数据传过去，可以用 intent.putExtra()
//                intent.putExtra("note_title", noteInfo.getNoteTitle())
//                intent.putExtra("note_content", noteInfo.getNoteContent())
//                intent.putExtra("note_time", noteInfo.getNoteCreateTime())
//                startActivity(intent)
                Log.d("note_title", noteInfo.getNoteTitle())
                Log.d("note_content", noteInfo.getNoteContent())
                val intent = Intent(requireContext(), NoteDetailActivity::class.java)
                intent.putExtra("note", noteInfo)  // key 自定义
                startActivity(intent)

            }
        })
    }

    override fun setListener() {
        rootView.findViewById<TextView>(R.id.btn_search).setOnClickListener {
            val content = et_search.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(requireContext(), "搜索内容不能为空~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val noteInfoList = NoteDbHelper(requireContext()).searchNote(content)

            if (::mListAdapter.isInitialized) {
                mListAdapter.setNoteInfoList(noteInfoList)
            }

            if (noteInfoList.isEmpty()) {
                ll_empty.visibility = View.VISIBLE
            } else {
                ll_empty.visibility = View.GONE
            }
        }
    }


    override fun initData() {
    }

    override fun initExtra() {
        createNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 1000) {
                initData()
            }
        }
    }

}
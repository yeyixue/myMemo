package com.example.mymemo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemo.CreateNoteActivity
import com.example.mymemo.NoteDetailActivity
import com.example.mymemo.R
import com.example.mymemo.adapter.NoteListAdapter
import com.example.mymemo.base.BaseFragment
import com.example.mymemo.db.NoteDbHelper
import com.example.mymemo.entity.Noteinfo
import com.example.mymemo.entity.Userinfo

class HomeFragment : BaseFragment() {


    private lateinit var recyclerview:RecyclerView
    private lateinit var mListAdapter: NoteListAdapter
    private lateinit var ll_empty: LinearLayout
    private lateinit var createNoteLauncher: ActivityResultLauncher<Intent>
    private val item1 = arrayOf("置顶", "编辑", "删除")
    private val item2 = arrayOf("取消置顶", "编辑", "删除")

    private var currentIndex = -1

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        recyclerview = rootView.findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        ll_empty=rootView.findViewById(R.id.ll_empty)

        //初始化适配器
        mListAdapter = NoteListAdapter()
        recyclerview.adapter=mListAdapter

        //recyclerview点击事件
        mListAdapter.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
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

            override fun onMore(position: Int, noteInfo: Noteinfo) {
                val items = if (noteInfo.note_is_top == 0) item1 else item2

                AlertDialog.Builder(requireContext())
                    .setTitle("操作选项")
                    .setSingleChoiceItems(items, 0) { _, which ->
                        currentIndex = which
                    }
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认") { _, _ ->
                        if (currentIndex == 2) { // 删除
                            val success  = NoteDbHelper(requireContext()).deleteNoteById(noteInfo.note_id)
                            if (success) {
                                Toast.makeText(requireContext(), "删除成功", Toast.LENGTH_SHORT).show()
                                initData() // 删除成功后刷新列表
                            } else {
                                Toast.makeText(requireContext(), "删除失败", Toast.LENGTH_SHORT).show()
                            }
                        }else if (currentIndex == 1){
                            //修改
                            val intent = Intent(requireContext(), CreateNoteActivity::class.java)
                            intent.putExtra("note",noteInfo)
                            createNoteLauncher.launch(intent)

                        }else{
                            //置顶
                            val newTopStatus = if (noteInfo.note_is_top == 0) 1 else 0
                            val success = NoteDbHelper(requireContext()).updateTopStatus(noteInfo.note_id, newTopStatus)
                            if (success) {
                                Toast.makeText(requireContext(), "更新成功", Toast.LENGTH_SHORT).show()
                                initData() // 刷新列表
                            } else {
                                Toast.makeText(requireContext(), "更新失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .show()
                currentIndex=0
            }
        })
    }

    override fun setListener() {
        rootView.findViewById<View>(R.id.btn_create_note).setOnClickListener {
            val intent = Intent(requireContext(), CreateNoteActivity::class.java)
            createNoteLauncher.launch(intent)
        }
    }

    override fun initData() {
        val userInfo = Userinfo.getUserInfo()
        if (userInfo == null) {
            Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show()
            return
        }
        val noteInfos = NoteDbHelper(requireContext()).getNotesByUser(userInfo.getUsername())
        Log.d("Note", "current user is ：${userInfo.getUsername()}")

        if (::mListAdapter.isInitialized) {
            mListAdapter.setNoteInfoList(noteInfos)
        }
        Log.d("HomeFragment", "noteInfos.size is : ${noteInfos.size}")


        if(noteInfos.size==0){
            ll_empty.visibility=View.VISIBLE
        }else{
            ll_empty.visibility = View.GONE
        }
    }

    override fun initExtra() {
        createNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 1000) {
                initData()
            }
        }
    }
}

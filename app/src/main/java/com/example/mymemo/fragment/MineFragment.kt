package com.example.mymemo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.mymemo.CreateNoteActivity
import com.example.mymemo.LoginActivity
import com.example.mymemo.R
import com.example.mymemo.UpdatePwdActivity
import com.example.mymemo.base.BaseFragment
import com.example.mymemo.entity.Userinfo
import androidx.appcompat.widget.Toolbar
import com.example.mymemo.AppHelpActivity
import com.example.mymemo.db.NoteDbHelper


class MineFragment : BaseFragment() {

    private lateinit var createNoteLauncher: ActivityResultLauncher<Intent>
    private lateinit var tv_username:TextView
    override fun getLayoutResId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        tv_username=rootView.findViewById(R.id.tv_username)
    }

    override fun setListener() {
        rootView.findViewById<TextView>(R.id.btn_edit_pwd).setOnClickListener{
            val intent = Intent(requireContext(), UpdatePwdActivity::class.java)
            createNoteLauncher.launch(intent)
        }
        rootView.findViewById<TextView>(R.id.btn_clear).setOnClickListener{
            //清楚所有的数据
            clearUserAllData()

        }
        rootView.findViewById<TextView>(R.id.btn_help).setOnClickListener{
            val intent = Intent(requireContext(), AppHelpActivity::class.java)
            startActivity(intent)
        }
        rootView.findViewById<TextView>(R.id.btn_app).setOnClickListener{
            val intent = Intent(requireContext(), AppHelpActivity::class.java)
            startActivity(intent)
        }
        rootView.findViewById<TextView>(R.id.tv_quit).setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun clearUserAllData() {
        val context = requireContext()
        val userinfo = Userinfo.getUserInfo()

        if (userinfo == null) {
            Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show()
        }

        // 弹出确认对话框
        AlertDialog.Builder(context)
            .setTitle("确认操作")
            .setMessage("是否清除当前账号的所有数据？\n此操作不可恢复！")
            .setNegativeButton("取消", null)
            .setPositiveButton("确认") { _, _ ->
                // 执行删除操作
                val dbHelper = NoteDbHelper(context)
                val rows = userinfo.let { Userinfo.getUserInfo()
                    ?.let { it1 -> dbHelper.deleteAllNotesByUsername(it1.getUsername()) } }
                if (rows != null) {
                    if (rows > 0) {
                        Toast.makeText(context, "已清除${rows}条数据", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "无可删除的数据", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    override fun initData() {
        var userinfo=Userinfo.getUserInfo()
        if(userinfo!=null){
            tv_username.setText(userinfo.getUsername())
        }
    }

    override fun initExtra() {
        createNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 2000) {
                var intent=Intent(activity,LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

}
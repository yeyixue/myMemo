package com.example.mymemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mymemo.db.UserDbHelper
import com.example.mymemo.base.BaseActivity



class RegisterActivity : BaseActivity() {
    private lateinit var et_username: EditText
    private lateinit var et_passwd: EditText
    private lateinit var toolbar_back:Toolbar

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initViews() {
        toolbar_back = findViewById(R.id.toolbar_back)


        et_username=findViewById(R.id.et_username)
        et_passwd=findViewById(R.id.et_passwd)
    }


    override fun setListener() {
        // 返回登录页面
        toolbar_back.setOnClickListener {
            finish()
        }

        // 注册逻辑.
        findViewById<Button>(R.id.btn_register).setOnClickListener {
            val username = et_username.text.toString().trim()
            val password = et_passwd.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show()
            } else {
                val dbHelper = UserDbHelper(this)

                if (dbHelper.isUsernameExists(username)) {
                    Toast.makeText(this, "用户名已存在，注册失败", Toast.LENGTH_SHORT).show()
                } else {
                    val success = dbHelper.insertUser(username, password)
                    if (success) {
                        Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun initData() {

    }
}
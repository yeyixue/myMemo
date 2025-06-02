package com.example.mymemo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mymemo.db.UserDbHelper
import com.example.mymemo.base.BaseActivity
import com.example.mymemo.entity.Userinfo

class LoginActivity : BaseActivity() {
    private lateinit var et_username: EditText
    private lateinit var et_passwd: EditText
    private lateinit var checkbox: CheckBox
    private  var is_login: Boolean=false
    private  lateinit var mSharedPreferences: SharedPreferences



    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {
        et_username=findViewById(R.id.et_username)
        et_passwd=findViewById(R.id.et_passwd)
        checkbox=findViewById(R.id.checkbox)
    }

    override fun initData() {
        mSharedPreferences=getSharedPreferences("userinfo", MODE_PRIVATE)
        is_login = mSharedPreferences.getBoolean("is_login", false)
        val username = mSharedPreferences.getString("username", "") ?: ""
        val password = mSharedPreferences.getString("password", "") ?: ""

        if (is_login) {
            et_username.setText(username)
            et_passwd.setText(password)
            checkbox.isChecked = true
        }
    }
    override fun setListener() {

        findViewById<TextView>(R.id.btn_register).setOnClickListener{
            var m_intent:Intent=Intent(this,RegisterActivity::class.java)
            startActivity(m_intent)
        }
        findViewById<Button>(R.id.btn_login).setOnClickListener {
            // 登录逻辑写在这里
            val username = findViewById<EditText>(R.id.et_username).text.toString().trim()
            val password = findViewById<EditText>(R.id.et_passwd).text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show()
            } else {
                val dbHelper = UserDbHelper(this)
                val isValid = dbHelper.checkUser(username, password)
                if (isValid) {
                    //登录成功
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    var edit:SharedPreferences.Editor=mSharedPreferences.edit()
                    edit.putString("username",username)
                    edit.putString("password",password)
                    edit.putBoolean("is_login", checkbox.isChecked)
                    edit.commit()

                    //保存用户信息
                    Userinfo.setUserInfo(Userinfo(username, password))

                    // 可跳转到主页面：
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
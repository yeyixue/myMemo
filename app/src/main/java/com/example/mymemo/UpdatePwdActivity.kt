package com.example.mymemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mymemo.base.BaseActivity
import com.example.mymemo.db.UserDbHelper
import com.example.mymemo.entity.Userinfo

class UpdatePwdActivity : BaseActivity() {

    private lateinit var et_new_password:TextView
    private lateinit var et_confirm_password:TextView
    override fun getLayoutResId(): Int {
        return R.layout.activity_update_pwd
    }

    override fun initViews() {
        et_new_password=findViewById(R.id.et_new_password)
        et_confirm_password=findViewById(R.id.et_confirm_password)
    }

    override fun setListener() {

        findViewById<Toolbar>(R.id.toolbar_back).setOnClickListener{
            finish()
        }

        findViewById<Button>(R.id.btn_updatepwd).setOnClickListener{
            val newPwd = et_new_password.text.toString()
            val confirmPwd = et_confirm_password.text.toString()

            if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
                Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show()
            } else if (newPwd != confirmPwd) {
                Toast.makeText(this, "新密码和确认密码不一致", Toast.LENGTH_SHORT).show()
            } else {
                val userInfo = Userinfo.getUserInfo()
                if (userInfo != null) {
                    val rows = UserDbHelper(this).updatePwd(userInfo.getUsername(), newPwd)
                    if (rows > 0) {
                        Toast.makeText(this, "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show()
                        setResult(2000)
                        finish()
                    } else {
                        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "用户信息为空，请重新登录", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun initData() {

    }
}
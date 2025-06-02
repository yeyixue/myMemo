package com.example.mymemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mymemo.base.BaseActivity

class AppHelpActivity : BaseActivity() {

    private lateinit var toolbar_back:Toolbar
    override fun getLayoutResId(): Int {
        return R.layout.activity_app_help
    }

    override fun initViews() {
        toolbar_back=findViewById(R.id.toolbar_back)
    }

    override fun setListener() {
        toolbar_back.setOnClickListener{
            finish()
        }
    }

    override fun initData() {
    }
}
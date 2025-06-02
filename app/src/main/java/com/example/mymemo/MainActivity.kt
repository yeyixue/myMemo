package com.example.mymemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentTransaction
import com.example.mymemo.ui.theme.MyMemoTheme
import com.example.mymemo.fragment.HomeFragment
import com.example.mymemo.fragment.MineFragment
import com.example.mymemo.fragment.SearchFragment
import com.example.mymemo.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {
    private var mHomeFragment: HomeFragment? = null
    private var mMineFragment: MineFragment? = null
    private var mSearchFragment: SearchFragment? = null

    private lateinit var mBottNavigationView: BottomNavigationView

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        mBottNavigationView=findViewById(R.id.bottomNavigationView)
    }

    override fun setListener() {
        mBottNavigationView.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home -> {
                    selectFragment(0)
                }
                R.id.search -> {
                    selectFragment(1)
                }
                R.id.mine -> {
                    selectFragment(2)
                }
            }
            true
        }
    }

    override fun initData() {
        selectFragment(0)
    }


    private fun selectFragment(position: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragment(fragmentTransaction)

        when (position) {
            0 -> {
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment()
                    fragmentTransaction.add(R.id.content, mHomeFragment!!)
                } else {
                    fragmentTransaction.show(mHomeFragment!!)
                }
            }
            1 -> {
                if (mSearchFragment == null) {
                    mSearchFragment = SearchFragment()
                    fragmentTransaction.add(R.id.content, mSearchFragment!!)
                } else {
                    fragmentTransaction.show(mSearchFragment!!)
                }
            }
            2 -> {
                if (mMineFragment == null) {
                    mMineFragment = MineFragment()
                    fragmentTransaction.add(R.id.content, mMineFragment!!)
                } else {
                    fragmentTransaction.show(mMineFragment!!)
                }
            }
        }

        fragmentTransaction.commit()
    }

    private fun hideFragment(fragmentTransaction: FragmentTransaction) {
        mHomeFragment?.let { fragmentTransaction.hide(it) }
        mSearchFragment?.let { fragmentTransaction.hide(it) }
        mMineFragment?.let { fragmentTransaction.hide(it) }
    }

}

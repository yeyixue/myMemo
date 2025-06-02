package com.example.mymemo.entity

import java.io.Serializable

class Userinfo : Serializable {

    private var username: String = ""
    private var password: String = ""

    constructor()
    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    companion object {
        private var sUserInfo: Userinfo? = null

        @JvmStatic
        fun getUserInfo(): Userinfo? {
            return sUserInfo
        }

        @JvmStatic
        fun setUserInfo(userinfo: Userinfo) {
            sUserInfo = userinfo
        }
    }



    // getter 和 setter
    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }
}

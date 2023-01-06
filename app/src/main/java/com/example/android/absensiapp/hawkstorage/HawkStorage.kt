package com.example.android.absensiapp.hawkstorage

import android.content.Context
import com.example.android.absensiapp.model.User
import com.orhanobut.hawk.Hawk

class HawkStorage {

    companion object{
        private const val USER_KEY = "user_key"
        private const val TOKE_KEY = "token_key"
        private val hawkStorage = HawkStorage()

        fun instance(context: Context?): HawkStorage {
            Hawk.init(context).build()
            return hawkStorage
        }
    }

    fun setUser(user: User) {
        Hawk.put(USER_KEY, user)
    }

    fun getUser(): User {
        return Hawk.get(USER_KEY)
    }

    fun setToken(token: String) {
        Hawk.put(TOKE_KEY, token)
    }

    fun getToken(): String {
        val rawToken = Hawk.get<String>(TOKE_KEY)
        val token = rawToken.split("|")
        return token[1]
    }

    fun isLogin(): Boolean {
        if (Hawk.contains(USER_KEY)) {
            return true
        }
        return false
    }

    fun deleteAll() {
        Hawk.deleteAll()
    }

}
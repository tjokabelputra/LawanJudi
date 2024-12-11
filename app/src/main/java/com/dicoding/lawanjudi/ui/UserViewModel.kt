package com.dicoding.lawanjudi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.lawanjudi.database.User
import com.dicoding.lawanjudi.database.UserPreference
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(name: String, email: String) {
        viewModelScope.launch {
            val user = User(name, email)
            pref.saveUser(user)
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            pref.clearUser()
        }
    }
}
package com.dicoding.lawanjudi.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

data class User(val name: String, val email: String)

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val name = stringPreferencesKey("name")
    private val email = stringPreferencesKey("email")

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                name = preferences[name] ?: "",
                email = preferences[email] ?: ""
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[name] = user.name
            preferences[email] = user.email
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
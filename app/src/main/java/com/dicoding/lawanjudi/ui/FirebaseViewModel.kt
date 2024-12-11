package com.dicoding.lawanjudi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.model.LoginResult
import com.dicoding.lawanjudi.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FirebaseViewModel : ViewModel() {
    val addUserResult = MutableLiveData<Result<String>>()
    val loginResult = MutableLiveData<Result<LoginResult>>()
    val userNameResult = MutableLiveData<Result<String>>()

    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = Firebase.database

    fun registerUser(username: String, email: String, password: String){
        addUserResult.postValue(Result.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newUser = User(
                        username = username,
                        email = email,
                        uid = auth.currentUser?.uid ?: ""
                    )
                    saveNewUserData(newUser)
                } else {
                    addUserResult.postValue(Result.Error(task.exception?.message ?: "Unknown error"))
                }
            }
    }

    private fun saveNewUserData(newUser: User) {
        val userRef = db.reference.child("users")

        userRef.child(newUser.uid!!).setValue(newUser) { error, _ ->
            if (error != null) {
                addUserResult.postValue(Result.Error(error.message))
            } else {
                auth.signOut()
                addUserResult.postValue(Result.Success("Selamat Akun Anda Berhasil Dibuat"))
            }
        }
    }

    fun loginUser(email: String, password: String) {
        loginResult.postValue(Result.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserName(auth.uid.toString())
                }
                else {
                    loginResult.postValue(Result.Error(task.exception?.message ?: "Unknown error"))
                }
            }
    }

    private fun getUserName(uid: String) {
        val userRef = db.reference.child("users").child(uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val loginUser = LoginResult(
                        message = "Username dan Password Cocok, Siap Untuk Melaporkan Situs/Iklan Judi Online",
                        username = user.username,
                        email = user.email
                    )
                    loginResult.postValue(Result.Success(loginUser))
                } else {
                    loginResult.postValue(Result.Error("User not found"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                loginResult.postValue(Result.Error(error.message))
            }
        })
    }
}
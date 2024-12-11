package com.dicoding.lawanjudi.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityLoginBinding
import com.dicoding.lawanjudi.ui.FirebaseViewModel
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.database.UserPreference
import com.dicoding.lawanjudi.database.userDataStore
import com.dicoding.lawanjudi.ui.UserViewModel
import com.dicoding.lawanjudi.ui.factory.UserModelFactory
import com.dicoding.lawanjudi.ui.home.HomeActivity
import com.dicoding.lawanjudi.ui.register.RegisterActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth

    private val viewModel: FirebaseViewModel by viewModels()
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        val pref = UserPreference.getInstance(this.userDataStore)
        userViewModel = UserModelFactory(pref).create(UserViewModel::class.java)

        binding?.pgLogin?.visibility = View.GONE

        viewModel.loginResult.observe(this) { result ->
            when(result){
                is Result.Loading -> {
                    binding?.pgLogin?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgLogin?.visibility = View.GONE
                    userViewModel.saveUser(result.data.username, result.data.email)
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    AlertDialog.Builder(this)
                        .setTitle(R.string.login_success)
                        .setMessage(result.data.message)
                        .setPositiveButton(R.string.next) { dialog, _ ->
                            dialog.dismiss()
                            startActivity(intent)
                        }
                        .show()
                }
                is Result.Error -> {
                    binding?.pgLogin?.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle(R.string.registration_failed)
                        .setMessage(result.error)
                        .setNegativeButton(R.string.next) {dialog, _ -> dialog.dismiss()}
                        .show()
                    Log.e("Login Activity", "Error: ${result}")
                }
            }
        }

        binding?.tvClickHere?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding?.btnLogin?.setOnClickListener {
            val email = binding?.emailEditText?.text.toString().trim()
            val password = binding?.passwordEditText?.text.toString().trim()
            when {
                email.isEmpty() -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.empty_email)
                        .setMessage(R.string.empty_email_desc)
                        .setNegativeButton(R.string.next) {dialog, _ -> dialog.dismiss()}
                        .show()
                }
                password.isEmpty() -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.empty_password)
                        .setMessage(R.string.empty_password_desc)
                        .setNegativeButton(R.string.next) {dialog, _ -> dialog.dismiss()}
                        .show()
                }
                else -> {
                    viewModel.loginUser(email, password)
                }
            }
        }

        binding?.btnGoogle?.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            userViewModel.saveUser(currentUser.displayName.toString(), currentUser.email.toString())
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
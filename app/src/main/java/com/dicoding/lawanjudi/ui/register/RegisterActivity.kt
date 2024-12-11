package com.dicoding.lawanjudi.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.databinding.ActivityRegisterBinding
import com.dicoding.lawanjudi.ui.FirebaseViewModel
import com.dicoding.lawanjudi.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        binding?.pgRegister?.visibility = View.GONE

        viewModel.addUserResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding?.pgRegister?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgRegister?.visibility = View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    AlertDialog.Builder(this)
                        .setTitle(R.string.registration_success)
                        .setMessage(result.data)
                        .setPositiveButton(R.string.next) { dialog, _ ->
                            dialog.dismiss()
                            startActivity(intent)
                        }
                        .show()
                }
                is Result.Error -> {
                    binding?.pgRegister?.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle(R.string.registration_failed)
                        .setMessage(result.error)
                        .setNegativeButton(R.string.next) {dialog, _ -> dialog.dismiss()}
                        .show()
                    Log.e("RegisterActivity", "Error: ${result}")
                }
            }
        }

        binding?.btnRegister?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding?.btnRegister?.setOnClickListener {
            val name = binding?.nameEditText?.text.toString().trim()
            val email = binding?.emailEditText?.text.toString().trim()
            val password = binding?.passwordEditText?.text.toString().trim()

            when {
                name.isEmpty() -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.empty_name)
                        .setMessage(R.string.empty_name_desc)
                        .setNegativeButton(R.string.next) {dialog, _ -> dialog.dismiss()}
                        .show()
                }
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
                    viewModel.registerUser(name, email, password)
                }
            }
        }
    }
}
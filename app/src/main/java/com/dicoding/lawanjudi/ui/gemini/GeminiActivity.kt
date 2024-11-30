package com.dicoding.lawanjudi.ui.gemini

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lawanjudi.database.remote.response.ContentRequest
import com.dicoding.lawanjudi.database.remote.response.ContentsItem
import com.dicoding.lawanjudi.database.remote.response.Message
import com.dicoding.lawanjudi.database.remote.response.Parts
import com.dicoding.lawanjudi.databinding.ActivityGeminiBinding
import com.dicoding.lawanjudi.ui.ChatModelFactory
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.database.local.entity.ChatEntity
import com.dicoding.lawanjudi.ui.adapter.ChatAdapter

class GeminiActivity : AppCompatActivity() {
    private var _binding: ActivityGeminiBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGeminiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        binding?.pgGemini?.visibility = View.GONE

        val factory: ChatModelFactory = ChatModelFactory.getInstance(this)
        val viewModel: GeminiViewModel by viewModels {
            factory
        }

        val chatAdapter = ChatAdapter()

        viewModel.chats.observe(this) { chats ->
            chatAdapter.submitList(chats)
        }

        viewModel.promptResult.observe(this) { response ->
            when(response){
                is Result.Loading -> {
                    binding?.pgGemini?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgGemini?.visibility = View.GONE
                    val responseText = response.data.candidates?.get(0)?.content?.parts?.get(0)?.text ?: "Something Wrong"
                    val botResponse = ChatEntity(role = "bot", message = responseText)
                    viewModel.addChat(botResponse)
                }
                is Result.Error -> {
                    binding?.pgGemini?.visibility = View.GONE
                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding?.btnAsk?.setOnClickListener {
            val text = binding?.geminiEditText?.text.toString()
            val request = ContentRequest(
                contents = listOf(ContentsItem(parts = listOf(Parts(text = text))))
            )
            val userRequest = ChatEntity(role = "user", message = text)
            viewModel.addChat(userRequest)
            binding?.geminiEditText?.text = null
            viewModel.callApi(request)
        }

        binding?.fabDelete?.setOnClickListener {
            viewModel.deleteChat()
        }

        binding?.rvChat?.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }
}
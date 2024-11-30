package com.dicoding.lawanjudi.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.database.local.entity.ChatEntity
import com.dicoding.lawanjudi.databinding.ItemChatBinding

class ChatAdapter: ListAdapter<ChatEntity, ChatAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    class MyViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatEntity){
            binding.tvMessage.text = chat.message

            binding.cardView.setBackgroundResource(R.drawable.bg_user)
            binding.tvMessage.textSize = 18f
            binding.ivCopy.visibility = View.GONE
            binding.ivShare.visibility = View.GONE

            if(chat.role == "bot"){
                binding.cardView.setBackgroundResource(R.drawable.bg_ai)
                binding.tvMessage.textSize = 16f
                binding.ivShare.visibility = View.VISIBLE
                binding.ivShare.setOnClickListener{
                    val context = it.context
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, chat.message)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share message via"))
                }
                binding.ivCopy.visibility = View.VISIBLE
                binding.ivCopy.setOnClickListener{
                    val context = it.context
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Copied Text", chat.message)
                    clipboard.setPrimaryClip(clip)

                    Toast.makeText(context, "Message copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ChatEntity> =
            object: DiffUtil.ItemCallback<ChatEntity>() {
                override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
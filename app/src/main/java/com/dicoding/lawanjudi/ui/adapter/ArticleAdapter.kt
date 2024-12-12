package com.dicoding.lawanjudi.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.lawanjudi.database.local.entity.ArticleEntity
import com.dicoding.lawanjudi.databinding.ItemArticleBinding
import com.dicoding.lawanjudi.ui.article.ArticleDetailActivity

class ArticleAdapter : ListAdapter<ArticleEntity, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(article: ArticleEntity){
            Log.d("ArticleAdapter", "Image URL: ${article.imageUrl}")
            Glide
                .with(binding.root.context)
                .load(article.imageUrl)
                .into(binding.ivImage)
            binding.tvArticleTitle.text = article.title
            binding.tvShortDesc.text = article.content


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ArticleDetailActivity::class.java)
                intent.putExtra(ARTICLE_TITLE, article.title)
                startActivity(itemView.context, intent, null)
            }
        }
    }

    companion object{
        private const val ARTICLE_TITLE = "article_title"

        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleEntity> =
            object: DiffUtil.ItemCallback<ArticleEntity>() {
                override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
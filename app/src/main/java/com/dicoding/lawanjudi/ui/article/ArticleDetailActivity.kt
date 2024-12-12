package com.dicoding.lawanjudi.ui.article

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityArticleDetailBinding
import com.dicoding.lawanjudi.ui.education.ArticleViewModel
import com.dicoding.lawanjudi.ui.factory.ArticleModelFactory

class ArticleDetailActivity : AppCompatActivity() {

    private var _binding : ActivityArticleDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        val factory: ArticleModelFactory = ArticleModelFactory.getInstance(this)
        val viewModel: ArticleViewModel by viewModels {
            factory
        }

        val title = intent.getStringExtra(ARTICLE_TITLE)

        viewModel.articleDetail(title.toString()).observe(this){ result ->
            if (result != null) {
                binding?.tvArticleTitle?.text = result.title
                binding?.tvArticleContent?.text = result.content.replace("\\n", "\n")
                binding?.ivArticleImg?.let {
                    Glide.with(this)
                        .load(result.imageUrl)
                        .into(it)
                }
            }
        }
    }

    companion object{
        private const val ARTICLE_TITLE = "article_title"
    }
}
package com.example.assignment_quantamit.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment_quantamit.databinding.ActivityNewsBinding
import com.example.assignment_quantamit.databinding.NewsTemplateBinding
import com.example.assignment_quantamit.remote_data.Article

class NewsAdapter:RecyclerView.Adapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
    return NewsViewHolder(
            NewsTemplateBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    private val diffUtil  = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = differ.currentList[position]
        val binding = holder.binding

        binding.tvTitle.text = item.title
        binding.tvDesc.text = item.description
        binding.tvSource.text = item.source.name
        Glide.with(binding.root).load(item.urlToImage).into(binding.ivThumbnail)

        binding.newsCard.visibility = if(item.description?.isBlank() == true ) View.GONE else View.VISIBLE

        binding.newsCard.setOnClickListener{
            onItemClick?.let {
                it(item)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClick:((Article)->Unit)? = null
    fun onNewsClicked(listener:(Article)->Unit){
        onItemClick = listener
    }
}

class NewsViewHolder(val binding:NewsTemplateBinding):RecyclerView.ViewHolder(binding.root)
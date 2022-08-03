package com.example.assignment_quantamit.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment_quantamit.MainActivity
import com.example.assignment_quantamit.databinding.ActivityNewsBinding
import com.example.assignment_quantamit.utils.NewsAdapter
import com.example.assignment_quantamit.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SharedViewModel>()
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val newsAdapter = NewsAdapter()
        binding.rlNews.apply {
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = newsAdapter
        }

        lifecycleScope.launchWhenCreated {
            viewModel.newsState.collectLatest {state->
                    when{
                        state.isLoading -> {
                            binding.loading.visibility = View.VISIBLE
                        }
                        state.error.isNotBlank() -> {
                            binding.loading.visibility = View.GONE
                            Snackbar.make(binding.root,state.error,Snackbar.LENGTH_LONG).setAction("Retry"){
                                viewModel.getHeadlines()
                            }.show()

                        }
                        state.news.isNotEmpty() -> {
                            binding.loading.visibility = View.GONE
                            newsAdapter.differ.submitList(state.news)
                        }
                    }
            }
        }

        newsAdapter.onNewsClicked {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(it.url)
                startActivity(this)
            }

        }

        //Search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchNews(it)
                    //To hide keyboard after searching
                    val imm: InputMethodManager =
                         this@NewsActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(this@NewsActivity.currentFocus?.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               // Do nothing
                if(newText?.isBlank() == true){
                    viewModel.getHeadlines()
                }
                return true
            }

        })

        //On clicking logout
        binding.btnLogout.setOnClickListener{
            AlertDialog.Builder(this).apply {
                create()
                setTitle("Logout")
                setMessage("Do you want to logout ? ")
                setPositiveButton("Yes"){_,_ ->
                    Firebase.auth.signOut()
                    Intent(this@NewsActivity,MainActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
                setNegativeButton("No"){d,_ ->
                    d.dismiss()
                }
                show()
            }
        }
    }


}
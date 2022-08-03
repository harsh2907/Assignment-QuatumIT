package com.example.assignment_quantamit

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.assignment_quantamit.databinding.ActivityMainBinding
import com.example.assignment_quantamit.screens.LoginFragment
import com.example.assignment_quantamit.screens.NewsActivity
import com.example.assignment_quantamit.screens.RegisterFragment
import com.example.assignment_quantamit.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //To setup custom top bar
        supportActionBar?.hide()

        //Setting up ui state according to top navigation
        viewModel.loginScreenState.observe(this) { state ->
            when (state) {
               is Screen.Login -> {

                    binding.tvLogin.apply {
                        setTextColor(resources.getColor(R.color.white))
                        background = ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.rounded_corner_bottom
                        )
                    }

                    binding.tvRegister.apply {
                        setTextColor(resources.getColor(R.color.black))
                        background = null
                    }

                    loadFragment(LoginFragment())
                }

                //When register is clicked
              is Screen.Register -> {

                    binding.tvLogin.apply {
                        setTextColor(resources.getColor(R.color.black))
                        background = null
                    }

                    binding.tvRegister.apply {
                        setTextColor(resources.getColor(R.color.white))
                        background = ContextCompat.getDrawable(
                            this@MainActivity,
                            R.drawable.rounded_corner_bottom
                        )
                    }

                    loadFragment(RegisterFragment())
                }
                else-> Unit
            }
        }

        //When login is clicked
        binding.tvLogin.setOnClickListener {
            viewModel.changeLoginScreenState(Screen.Login)
        }

        //When Register is clicked
        binding.tvRegister.setOnClickListener {
            viewModel.changeLoginScreenState(Screen.Register)
        }

    }

    //Function to load fragment in frame layout
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flSignUp, fragment)
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            Intent(this,NewsActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}

sealed class Screen{
    object Login:Screen()
    object Register:Screen()
}
package com.example.assignment_quantamit.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.assignment_quantamit.R
import com.example.assignment_quantamit.Screen
import com.example.assignment_quantamit.databinding.FragmentLoginBinding
import com.example.assignment_quantamit.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding
    private lateinit var auth:FirebaseAuth
    private val viewModel:SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navigate to signup screen
        binding.btnRegisterNow.setOnClickListener {
            viewModel.changeLoginScreenState(Screen.Register)
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.btnLogin.isEnabled = text.isNotBlank() && text.contains("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
            }
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.btnLogin.isEnabled = text.isNotBlank() && text.length > 5
            }
        }

        //Unused Features
        binding.btnForgotPass.setOnClickListener {
            Toast.makeText(requireContext(), "Forgot password feature yet to be implemented", Toast.LENGTH_SHORT).show()
        }

        binding.ivGoogle.setOnClickListener {
            Toast.makeText(requireContext(), "Login with google feature yet to be implemented", Toast.LENGTH_SHORT).show()
        }

        binding.ivFacebook.setOnClickListener {
            Toast.makeText(requireContext(), "Login with facebook feature yet to be implemented", Toast.LENGTH_SHORT).show()
        }

        //handled behavior of login button
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when{
                email.isBlank() -> { Toast.makeText(requireContext(), "Email can't be empty", Toast.LENGTH_SHORT).show() }
                password.isBlank() -> { Toast.makeText(requireContext(), "Password can't be empty", Toast.LENGTH_SHORT).show()}
                password.length < 6 -> {
                    Toast.makeText(requireContext(), "Password is too short minimum length is 6 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //Login Feature
                    binding.loading.visibility = View.VISIBLE

                    auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                        binding.loading.visibility = View.GONE

                        Intent(activity,NewsActivity::class.java).apply {
                                startActivity(this)
                                activity?.finish()
                            }
                        }.addOnFailureListener {
                        binding.loading.visibility = View.GONE

                        Log.e("LoginFrag",it.message.toString())
                        Toast.makeText(requireContext(), "Wrong credentials please check your email and password", Toast.LENGTH_SHORT).show() }
                    }
                }
            }
        }
}
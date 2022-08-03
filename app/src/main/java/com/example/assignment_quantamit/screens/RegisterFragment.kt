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
import com.example.assignment_quantamit.databinding.FragmentRegisterBinding
import com.example.assignment_quantamit.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private val viewModel:SharedViewModel by viewModels()
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.btnRegister.isEnabled = text.isNotBlank() && text.contains("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
            }
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.btnRegister.isEnabled = text.isNotBlank() && text.length > 5
            }
        }

        binding.etContact.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.btnRegister.isEnabled = text.isNotBlank() && text.length > 9
            }
        }

        binding.etName.doOnTextChanged { text, _, _, _ ->
            text?.let {
                binding.btnRegister.isEnabled = text.isNotBlank()
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.changeLoginScreenState(Screen.Login)
        }

        //handled behavior of login button
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val contact = binding.etContact.text.toString().trim()
            val name = binding.etName.text.toString().trim()

            when{
                email.isBlank() -> { Toast.makeText(requireContext(), "Email can't be empty", Toast.LENGTH_SHORT).show() }
                password.isBlank() -> { Toast.makeText(requireContext(), "Password can't be empty", Toast.LENGTH_SHORT).show()}
                contact.isBlank() -> { Toast.makeText(requireContext(), "Contact can't be empty", Toast.LENGTH_SHORT).show()}
                name.isBlank() -> { Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT).show()}
                !binding.cbTerms.isChecked -> {Toast.makeText(requireContext(), "Please agree to terms and conditions", Toast.LENGTH_SHORT).show()}
                else -> {
                    binding.loading.visibility = View.VISIBLE
                   auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                       binding.loading.visibility = View.GONE
                       Intent(activity,NewsActivity::class.java).apply {
                               startActivity(this)
                               activity?.finish()
                           }
                       }.addOnFailureListener{
                       binding.loading.visibility = View.GONE
                       Log.e("RegisterFrag",it.message.toString())
                           Toast.makeText(requireContext(), "An error occurred while registering", Toast.LENGTH_SHORT).show()
                       }
                   }
                }
            }

        }
    }

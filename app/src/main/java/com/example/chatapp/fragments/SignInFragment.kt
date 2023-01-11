package com.example.chatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            findNavController().navigate(R.id.logInFragment)
        }

        if (auth.currentUser != null){
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
            return binding.root
        }

        binding.login.setOnClickListener {
            val gmail = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if (gmail.isNotEmpty() && password.isNotEmpty()) {
                registration(gmail, password)
            } else {
                Toast.makeText(context, "Edittext is Empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun registration(gmail: String, password: String) {
        auth.signInWithEmailAndPassword(gmail, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homeFragment)
                }else{
                    Toast.makeText(context, "Gmail or Password incorrect", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
package com.example.chatapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentAllUsersBinding
import com.example.chatapp.models.MyMessage
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AllUsersFragment : Fragment() {
    private lateinit var binding: FragmentAllUsersBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var list: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllUsersBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        list = ArrayList()

        reference = firebaseDatabase.getReference("users")

        binding.search.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }

        binding.more.setOnClickListener {
            auth.signOut()
            findNavController().popBackStack()
            findNavController().navigate(R.id.signInFragment)
        }

        return binding.root
    }

}
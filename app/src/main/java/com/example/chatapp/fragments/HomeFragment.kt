package com.example.chatapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.chatapp.adapter.Click
import com.example.chatapp.adapter.MyRvAdapter
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.models.User
import com.google.firebase.database.*
import com.example.chatapp.R
import com.example.chatapp.utils.Data
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment(), Click {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var rvAdapter: MyRvAdapter
    private lateinit var list: ArrayList<User>
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()

        reference = firebaseDatabase.getReference("users")
        auth = FirebaseAuth.getInstance()
        list = ArrayList()
        rvAdapter = MyRvAdapter(list, this, auth = FirebaseAuth.getInstance())

        binding.rvUsers.adapter = rvAdapter

        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (child in snapshot.children) {
                    val users = child.getValue(User::class.java)
                    if (users != null) {
                        if (auth.uid != users.uid) {
                            list.add(users)
                        }
                    }
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.more.setOnClickListener {
            auth.signOut()
            findNavController().popBackStack()
            findNavController().navigate(R.id.signInFragment)
        }

        return binding.root
    }

    override fun onPressed(user: User) {
        findNavController().navigate(R.id.chatFragment, bundleOf("user" to user))
    }

    override fun onPause() {
        super.onPause()
        Data.online("offline", FirebaseAuth.getInstance())
    }

    override fun onResume() {
        super.onResume()
        Data.online("online", FirebaseAuth.getInstance())
    }
}
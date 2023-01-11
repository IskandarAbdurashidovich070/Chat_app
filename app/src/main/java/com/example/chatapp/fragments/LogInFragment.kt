package com.example.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLogInBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var path: String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseStorage: FirebaseStorage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)

        path = ""
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReference("image")

        binding.image.setOnClickListener {
            getImageContent.launch("image/*")
        }

        binding.signup.setOnClickListener {
            val gmail = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val name = binding.name.text.toString().trim()
            if (gmail.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                registration(gmail, password, name)
            } else {
                Toast.makeText(context, "Edittext is Empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }



    private fun registration(gmail: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(gmail, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    reference.child(auth.uid.toString()).setValue(User(name,path,gmail, password, auth.uid.toString()))
                        .addOnSuccessListener {
                            findNavController().navigate(R.id.homeFragment)
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Problem With Saving", Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(context, "Problem with Signing", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            val hours = System.currentTimeMillis()

            val tesk = storageReference.child(hours.toString()).putFile(uri!!)

            tesk.addOnSuccessListener {


                val downloadUrl = it.metadata?.reference?.downloadUrl

                downloadUrl?.addOnSuccessListener { uri ->
                    path = uri.toString()
                    Glide.with(binding.root.context).load(path).into(binding.image)
                }

            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
}


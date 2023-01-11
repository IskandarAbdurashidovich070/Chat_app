package com.example.chatapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.adapter.MyMessageAdapter
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.models.MyMessage
import com.example.chatapp.models.User
import com.example.chatapp.utils.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var list: ArrayList<MyMessage>
    private lateinit var listUser: ArrayList<User>
    private lateinit var myMessageAdapter: MyMessageAdapter
    private lateinit var storageReference: StorageReference
    private lateinit var path:String
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var user: User
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    var paths = ""
    private val TAG = "ChatFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("message")
        user = arguments?.getSerializable("user") as User
        list = ArrayList()
        listUser = ArrayList()
        myMessageAdapter = MyMessageAdapter(list, auth)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReference("messageImages")
        path = ""


        binding.rv.adapter = myMessageAdapter
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.message.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.send.visibility = View.VISIBLE
            } else {
                binding.send.visibility = View.GONE

            }
        }

        binding.send.setOnClickListener {
            if (binding.message.text.isNotBlank()) {
                var key = reference.push().key
                reference.child(key.toString()).setValue(
                    MyMessage(
                        binding.message.text.toString(),
                        auth.uid.toString(),
                        user.uid,
                        "text"
                    )
                )
                binding.message.text.clear()
            } else {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        }

        binding.text.text = user.name
        Glide.with(binding.root.context).load(user.image).into(binding.image)

        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (child in snapshot.children) {
                    val value = child.getValue(MyMessage::class.java)
                    if (value != null) {
                        if ((value.fromUid == auth.uid && user!!.uid == value.toUid) || (auth.uid == value.toUid && user!!.uid == value.fromUid)) {
                            list.add(value)
                        }
                    }
                }
                myMessageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUser.clear()
                for (child in snapshot.children) {
                    val value = child.getValue(User::class.java)
                    if (value != null) {
                        listUser.add(value)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        binding.files.setOnClickListener {
            getImageContent.launch("image/*")
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Data.online("offline", FirebaseAuth.getInstance())
    }

    override fun onResume() {
        super.onResume()
        Data.online("online", FirebaseAuth.getInstance())
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            val hours = System.currentTimeMillis()

            val tesk = storageReference.child(hours.toString()).putFile(uri!!)

            Toast.makeText(binding.root.context, "$uri", Toast.LENGTH_SHORT).show()

            tesk.addOnSuccessListener {


                val downloadUrl = it.metadata?.reference?.downloadUrl

                downloadUrl?.addOnSuccessListener { uri ->
                    path = uri.toString()
                    val key = reference.push().key
                    reference.child(key.toString()).setValue(
                        MyMessage(
                            binding.message.text.toString(),
                            uri.toString(),
                            auth.uid.toString(),
                            user.uid,
                            "image"
                        )
                    )
                }

            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

}
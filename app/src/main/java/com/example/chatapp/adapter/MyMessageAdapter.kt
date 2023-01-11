package com.example.chatapp.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.DialogItemBinding
import com.example.chatapp.databinding.FromMessageBinding
import com.example.chatapp.databinding.RvItemBinding
import com.example.chatapp.databinding.ToMessageBinding
import com.example.chatapp.models.MyMessage
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth

class MyMessageAdapter(var list: List<MyMessage>, var auth: FirebaseAuth) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class Vh1(var fromMessageBinding: FromMessageBinding) :
        RecyclerView.ViewHolder(fromMessageBinding.root) {
        fun onBind(user: MyMessage, position: Int) {
            when (user.type) {
                "text" -> {
                    fromMessageBinding.messageFrom.text = user.text
                    fromMessageBinding.messageFrom.visibility = View.VISIBLE
                    fromMessageBinding.image.visibility = View.GONE

                }
                "image" -> {
                    fromMessageBinding.image.setOnClickListener {
                        var dialog = AlertDialog.Builder(fromMessageBinding.image.context)
                        var item = DialogItemBinding.inflate(LayoutInflater.from(fromMessageBinding.root.context))
                        dialog.setView(item.root)
                        Glide.with(fromMessageBinding.root).load(list[position].image).into(item.image)
                        dialog.show()
                        dialog.setCancelable(true)
                    }
                    fromMessageBinding.messageFrom.visibility = View.GONE
                    fromMessageBinding.image.visibility = View.VISIBLE
                    Glide.with(fromMessageBinding.root).load(user.image)
                        .into(fromMessageBinding.image)
                }
            }

        }
    }

    inner class Vh2(var toMessageBinding: ToMessageBinding) :
        RecyclerView.ViewHolder(toMessageBinding.root) {
        fun onBind(user: MyMessage, position: Int) {
            when (user.type) {
                "text" -> {
                    toMessageBinding.image.setOnClickListener {
                        var dialog = AlertDialog.Builder(toMessageBinding.image.context)
                        var item = DialogItemBinding.inflate(LayoutInflater.from(toMessageBinding.root.context))
                        dialog.setView(item.root)
                        Glide.with(toMessageBinding.root).load(list[position].image).into(item.image)
                        dialog.show()
                        dialog.setCancelable(true)
                    }
                    toMessageBinding.messageTo.text = user.text
                    toMessageBinding.messageTo.visibility = View.VISIBLE
                    toMessageBinding.image.visibility = View.GONE

                }
                "image" -> {
                    toMessageBinding.messageTo.visibility = View.GONE
                    toMessageBinding.image.visibility = View.VISIBLE
                    Glide.with(toMessageBinding.root).load(user.image)
                        .into(toMessageBinding.image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            Vh1(FromMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            Vh2(ToMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            var fromHolder = holder as Vh1
            fromHolder.onBind(list[position], position)
        } else {
            var toHolder = holder as Vh2
            toHolder.onBind(list[position], position)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (list[position].fromUid == auth.uid) {
            0
        } else {
            1
        }
    }
}


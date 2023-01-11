package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.RvItemBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth


class MyRvAdapter(var list: List<User>, var click: Click, var auth: FirebaseAuth) :
    RecyclerView.Adapter<MyRvAdapter.Vh>() {

    inner class Vh(var rvItemBinding: RvItemBinding) : RecyclerView.ViewHolder(rvItemBinding.root) {
        fun onBind(user: User, position: Int) {
                Glide.with(rvItemBinding.root.context).load(user.image).into(rvItemBinding.image)
                rvItemBinding.name.text = user.name

            rvItemBinding.root.setOnClickListener {
                click.onPressed(user)
            }
            rvItemBinding.lastMassage.text = user.online
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}

interface Click {
    fun onPressed(user: User)
}

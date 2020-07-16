package com.example.chatter.ui.main.users

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.users_view_item.view.*
import javax.inject.Inject

class UsersRecyclerAdapter
@Inject constructor(
    private val firestoreRecyclerOptions: FirestoreRecyclerOptions<User>,
    private val requestManager: RequestManager
) : FirestoreRecyclerAdapter<User, UsersRecyclerAdapter.UserViewHolder>(firestoreRecyclerOptions) {

    private lateinit var userClickListener: UserClickListener

    fun setUserClickListener(listener: UserClickListener) {
        userClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.users_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.apply {
            holder.itemView.name_user_item.text = model.name
            holder.itemView.setOnClickListener(this)
            if (model.image != "default") {
                requestManager
                    .load(model.image)
                    .into(holder.itemView.image_user_item)
            }
        }
    }

    inner class UserViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        override fun onClick(v: View?) {
            val documentSnapshot = snapshots.getSnapshot(adapterPosition)
            println("debug: user was clicked ${documentSnapshot.id}")
            userClickListener.onUserItemClicked(documentSnapshot)
        }
    }

    interface UserClickListener {
        fun onUserItemClicked(documentSnapshot: DocumentSnapshot)
    }
}
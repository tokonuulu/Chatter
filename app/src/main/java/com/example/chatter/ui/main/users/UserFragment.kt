package com.example.chatter.ui.main.users

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.ui.main.MainActivity
import com.example.chatter.ui.user.OtherUserActivity
import com.google.firebase.firestore.DocumentSnapshot
import dagger.android.AndroidInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.user_fragment.*
import javax.inject.Inject

class UserFragment() : DaggerFragment(), UsersRecyclerAdapter.UserClickListener {

    override fun onUserItemClicked(documentSnapshot: DocumentSnapshot) {
        openProfile(documentSnapshot.id)
    }

    @Inject lateinit var recyclerAdapter: UsersRecyclerAdapter
    @Inject lateinit var requestManager: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).onTitleChange("Users")

        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(recycler_view_users) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
            recyclerAdapter.setUserClickListener(this@UserFragment)
            recyclerAdapter.startListening()
        }
    }

    private fun openProfile(id: String) {
        val intent = Intent(activity, OtherUserActivity::class.java)
        intent.putExtra("uid", id)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        recyclerAdapter.stopListening()
    }

    override fun onStart() {
        super.onStart()
        recyclerAdapter.startListening()
    }
}
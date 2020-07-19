package com.example.chatter.ui.main.openchats

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.model.OpenChat
import com.example.chatter.ui.main.MainActivity
import com.example.chatter.ui.messages.MessagesActivity
import com.google.firebase.firestore.DocumentSnapshot
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.open_chat_fragment.*
import kotlinx.android.synthetic.main.user_fragment.*
import javax.inject.Inject

class OpenChatsFragment : DaggerFragment(), OpenChatsRecyclerAdapter.OpenChatClickListener {

    override fun onOpenChatItemClicked(documentSnapshot: DocumentSnapshot) {
        openMessages(documentSnapshot.id)
    }

    @Inject
    lateinit var recyclerAdapter: OpenChatsRecyclerAdapter
    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.open_chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).onTitleChange("Chats")

        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(recycler_view_open_chats) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
            recyclerAdapter.setUserClickListener(this@OpenChatsFragment)
            recyclerAdapter.startListening()
        }
    }

    private fun openMessages(id: String) {
        val intent = Intent(activity, MessagesActivity::class.java)
        intent.putExtra("receiver_uid", id)
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
package com.example.chatter.ui.messages

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.model.Message
import com.example.chatter.model.User
import com.example.chatter.ui.user.OtherUserActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_messages.*
import javax.inject.Inject

class MessagesActivity : DaggerAppCompatActivity() {

    @Inject lateinit var modelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: MessagesViewModel

    @Inject lateinit var messagesRecyclerAdapter: MessagesRecyclerAdapter
    @Inject lateinit var requestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        setSupportActionBar(toolbar_messages)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, modelProvider)
            .get(MessagesViewModel::class.java)

        initializeRecyclerView()

        val receiverUid = intent.getStringExtra("receiver_uid")
        viewModel.setReceiverUid(receiverUid)

        title_toolbar_messages.setOnClickListener {
            openReceiverProfile(receiverUid)
        }

        image_send_messages.setOnClickListener {
            sendMessage()
        }

        startObservingReceiverInfo()
        startObservingMessages()
        startObservingNewMessages()
    }

    private fun sendMessage() {
        if (message_text_messages.text.isNotEmpty()) {
            val message = message_text_messages.text.toString()
            viewModel.sendMessage(message)
            message_text_messages.text.clear()
        }
    }

    private fun initializeRecyclerView() {
        with(recycler_view_messages) {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@MessagesActivity)
        }
    }

    private fun startObservingReceiverInfo() {
        viewModel.receiverUser.observe(this, Observer { user ->
            if (user == null) return@Observer

            updateActionBar(user)
        })
    }

    fun startObservingMessages() {
        viewModel.messageList.observe(this, Observer { list ->
            if (list == null) return@Observer

            messagesRecyclerAdapter.setSenderInfo(viewModel.getCurrentUid())
            messagesRecyclerAdapter.setMessageList(list)
            recycler_view_messages.adapter = messagesRecyclerAdapter
            if (list.isNotEmpty())
                recycler_view_messages.smoothScrollToPosition(messagesRecyclerAdapter.itemCount-1)
        })
    }

    fun startObservingNewMessages() {
        viewModel.newMessage.observe(this, Observer { message ->
            if (message == null) return@Observer

            messagesRecyclerAdapter.onNewMessage(message)
            recycler_view_messages.smoothScrollToPosition(messagesRecyclerAdapter.itemCount-1)
        })
    }

    private fun updateActionBar(user: User) {
        title_toolbar_messages.text = user.name

        if (user.image != "default")
            requestManager
                .load(user.image)
                .into(image_toolbar_messages)
    }

    private fun openReceiverProfile(uid: String) {
        val intent = Intent(this, OtherUserActivity::class.java)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
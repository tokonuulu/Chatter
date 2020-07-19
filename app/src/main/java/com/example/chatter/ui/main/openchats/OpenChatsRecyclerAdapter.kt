package com.example.chatter.ui.main.openchats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.model.OpenChat
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.open_chat_view_item.view.*
import javax.inject.Inject

class OpenChatsRecyclerAdapter
@Inject constructor(
    private val firestoreRecyclerOptions: FirestoreRecyclerOptions<OpenChat>,
    private val requestManager: RequestManager,
    private val currentUid: String
) : FirestoreRecyclerAdapter<OpenChat, OpenChatsRecyclerAdapter.OpenChatViewHolder>(
    firestoreRecyclerOptions
) {

    private lateinit var openChatClickListener: OpenChatClickListener

    fun setUserClickListener(listener: OpenChatClickListener) {
        openChatClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatViewHolder {
        return OpenChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.open_chat_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OpenChatViewHolder, position: Int, model: OpenChat) {
        holder.apply {
            holder.itemView.name_open_chat_item.text = model.withName

            var msg = model.lastMessage
            if (currentUid == model.senderUid)
                msg = "You: $msg"
            holder.itemView.message_open_chat_item.text = msg

            holder.itemView.setOnClickListener(this)
            if (model.withImageUrl != "default") {
                requestManager
                    .load(model.withImageUrl)
                    .into(holder.itemView.image_open_chat_item)
            }
        }
    }

    inner class OpenChatViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            val documentSnapshot = snapshots.getSnapshot(adapterPosition)
            println("debug: open chat was clicked ${documentSnapshot.id}")
            openChatClickListener.onOpenChatItemClicked(documentSnapshot)
        }
    }

    interface OpenChatClickListener {
        fun onOpenChatItemClicked(documentSnapshot: DocumentSnapshot)
    }
}
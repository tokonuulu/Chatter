package com.example.chatter.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.model.Message
import kotlinx.android.synthetic.main.message_receiver_view_item.view.*
import kotlinx.android.synthetic.main.message_sender_view_item.view.*
import java.lang.IllegalStateException
import javax.inject.Inject

class MessagesRecyclerAdapter
@Inject
constructor(
    private val requestManager: RequestManager
) : RecyclerView.Adapter<MessagesRecyclerAdapter.BaseViewHolder<*>>() {

    companion object {
        private const val TYPE_SENDER = 0
        private const val TYPE_RECEIVER = 1
    }

    private var messageList: MutableList<Message> = emptyList<Message>().toMutableList()
    private lateinit var currentUid: String

    fun setSenderInfo(currentUid: String) {
        this.currentUid = currentUid
    }

    fun setMessageList(list: List<Message>) {
        messageList = list.toMutableList()
    }

    fun onNewMessage(newMessage: Message) {
        messageList.add(newMessage)
        notifyItemInserted(messageList.size - 1)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList.get(position).senderUid == currentUid) {
            TYPE_SENDER
        } else {
            TYPE_RECEIVER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_SENDER) {
            SenderViewHolder(
                inflater.inflate(
                    R.layout.message_sender_view_item,
                    parent,
                    false
                )
            )
        } else {
            ReceiverViewHolder(
                inflater.inflate(
                    R.layout.message_receiver_view_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val message = messageList[position]
        when (holder) {
            is SenderViewHolder -> holder.bind(message)
            is ReceiverViewHolder -> holder.bind(message)
            else -> throw IllegalStateException()
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class SenderViewHolder (itemView: View) : BaseViewHolder<Message>(itemView) {

        override fun bind(item: Message) {
            itemView.message_sender.text = item.message

            requestManager
                .load(item.senderUser.image)
                .into(itemView.image_sender)
        }
    }

    inner class ReceiverViewHolder (itemView: View) : BaseViewHolder<Message>(itemView) {

        override fun bind(item: Message) {
            itemView.message_receiver.text = item.message

            requestManager
                .load(item.senderUser.image)
                .into(itemView.image_receiver)
        }
    }

}
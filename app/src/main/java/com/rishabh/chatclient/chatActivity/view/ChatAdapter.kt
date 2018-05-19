package com.rishabh.chatclient.chatActivity.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.rishabh.chatclient.R
import com.rishabh.chatclient.model.ChatMessage
import java.util.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val TYPE_SENT_MESSAGE = 0
    private val TYPE_RECEIVED_MESSAGE = 1

    private val chatList = LinkedList<ChatMessage>()
    private var recyclerView: RecyclerView? = null

    fun addMessage(message: ChatMessage) {
        chatList.add(message)
        notifyItemInserted(chatList.size - 1)
        recyclerView?.scrollToPosition(chatList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View = when (viewType) {
            TYPE_SENT_MESSAGE ->
                LayoutInflater.from(parent.context).inflate(R.layout.message_sent_layout, parent, false)
            else ->
                LayoutInflater.from(parent.context).inflate(R.layout.message_recieved_layout, parent, false)
        }
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (chatList[position].sentByMe) {
            true -> TYPE_SENT_MESSAGE
            else -> TYPE_RECEIVED_MESSAGE
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.message_tv)
        lateinit var messageTv: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(chatMessage: ChatMessage) {
            messageTv.text = chatMessage.message
        }
    }
}
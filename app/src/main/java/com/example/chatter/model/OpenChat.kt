package com.example.chatter.model

import com.google.firebase.Timestamp


class OpenChat(
    val withName: String,
    val withImageUrl: String,
    val senderUid: String,
    val lastMessage: String
) {
    constructor() : this("", "", "", "")

}
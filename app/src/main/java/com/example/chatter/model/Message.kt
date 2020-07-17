package com.example.chatter.model

class Message(
    val senderUid: String,
    val senderUser: User,
    val message: String
) {
    constructor() : this("", User(), "") {


    }
}
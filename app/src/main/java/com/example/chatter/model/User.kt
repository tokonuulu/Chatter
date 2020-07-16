package com.example.chatter.model

class User (
    val name: String,
    val email: String,
    val password: String,
    val image: String
) {

    constructor() : this("", "", "", "") {

    }

}
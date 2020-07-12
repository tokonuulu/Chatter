package com.example.chatter.repository

import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource
) {

    fun getCurrentUser() : FirebaseUser? {
        return firebaseAuthSource.getCurrentUser()
    }

    fun getCurrentUid() : String? {
        return firebaseAuthSource.getCurrentUid()
    }

    fun registerNewUser(name: String, email: String, password: String) : Completable {
        return firebaseAuthSource.registerNewUser(name, email, password)
    }

    fun signIn(email: String, password: String) : Completable {
        return firebaseAuthSource.signIn(email, password)
    }

    fun signOut() {
        firebaseAuthSource.signOut()
    }
}
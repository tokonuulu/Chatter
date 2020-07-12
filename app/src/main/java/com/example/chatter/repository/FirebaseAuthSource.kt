package com.example.chatter.repository

import com.example.chatter.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    fun getCurrentUid(): String? {
        return firebaseAuth.currentUser?.uid
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun registerNewUser(name: String, email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
                .addOnCompleteListener {
                    val map = mutableMapOf<String, Any>()
                    map["name"] = name
                    map["email"] = email
                    map["password"] = password
                    map["image"] = ""

                    firebaseFirestore.collection(Constants.USERS)
                        .document(getCurrentUid()!!).set(map)
                        .addOnFailureListener { exception ->
                            emitter.onError(exception)
                        }
                        .addOnSuccessListener {
                            emitter.onComplete()
                        }
                }
        }
    }

    fun signIn (email: String, password: String) : Completable {
        return Completable.create { emitter ->  
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
                .addOnSuccessListener {
                    emitter.onComplete()
                }
        }
    }

    fun signOut () {
        firebaseAuth.signOut()
    }
}
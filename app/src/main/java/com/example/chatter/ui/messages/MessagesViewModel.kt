package com.example.chatter.ui.messages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatter.model.Message
import com.example.chatter.model.User
import com.example.chatter.repository.AuthRepository
import com.example.chatter.repository.DatabaseRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MessagesViewModel
@Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message> = _newMessage

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> = _messageList

    private val _receiverUser = MutableLiveData<User>()
    val receiverUser: LiveData<User> = _receiverUser

    private lateinit var receiverUid: String
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private var isThisTheFirstSnapshot: Boolean

    init {
        isThisTheFirstSnapshot = true
    }

    fun setReceiverUid(uid: String) {
        receiverUid = uid

        loadReceiverInfo()
        loadMessages()
    }

    private fun loadReceiverInfo() {
        databaseRepository.getUserInfo(receiverUid)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }

                override fun onNext(user: User?) {
                    _receiverUser.value = user
                }

                override fun onError(e: Throwable?) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun loadMessages() {
        databaseRepository.getMessageListWithUser(receiverUid)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<QuerySnapshot> {
                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }

                override fun onNext(querySnapshot: QuerySnapshot?) {
                    if (querySnapshot == null) return

                    /* we get list of all the messages in the first snapshot */
                    if (isThisTheFirstSnapshot) {
                        _messageList.value = querySnapshot.toObjects(Message::class.java)
                        isThisTheFirstSnapshot = false
                    }
                    /* if the snapshot changes it means we have new messages */
                    else {
                        for (documentChange in querySnapshot.documentChanges) {
                            /* Here we need to emit all the newly added messages */
                            if (documentChange.type == DocumentChange.Type.ADDED)
                                _newMessage.value = documentChange.document.toObject(Message::class.java)
                        }
                    }
                }

                override fun onError(e: Throwable?) {

                }

                override fun onComplete() {

                }
            })
    }

    fun getCurrentUid() : String = authRepository.getCurrentUid()!!

    fun sendMessage(message: String) {
        println("debug: send message: sending message")
        val currentUid = getCurrentUid()
        println("debug: send message: retrieved uid $currentUid")
        databaseRepository.getUserInfo(currentUid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }
                override fun onSuccess(user: User) {
                    databaseRepository.sendMessage(receiverUid, Message(currentUid, user, message))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : CompletableObserver {
                            override fun onSubscribe(d: Disposable?) {
                                compositeDisposable.add(d)
                            }

                            override fun onComplete() {
                                println("debug: send message: success")
                            }

                            override fun onError(e: Throwable?) {
                                Log.d("MessageViewModel", "onError: ${e?.message}")
                            }
                        })
                }
                override fun onError(e: Throwable?) {
                    Log.e("MessageViewModel", "onError: ${e?.message}")
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
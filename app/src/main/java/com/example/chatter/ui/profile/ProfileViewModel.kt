package com.example.chatter.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatter.model.User
import com.example.chatter.repository.AuthRepository
import com.example.chatter.repository.DatabaseRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {


    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        databaseRepository.getCurrentUserInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }
                override fun onNext(user: User?) {
                    if (user != null) {
                        onUserInfoChange(user)
                    }
                }
                override fun onError(e: Throwable?) {
                }

                override fun onComplete() {
                }
            })
    }

    fun onSingOut() {
        authRepository.signOut()
    }

    private fun onUserInfoChange(user: User) {
        _user.value = user
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
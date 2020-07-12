package com.example.chatter.ui.register

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatter.repository.AuthRepository
import com.example.chatter.repository.DatabaseRepository
import com.example.chatter.utils.RequestState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _state = MutableLiveData<RequestState>()
    val state: LiveData<RequestState> = _state

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    
    fun registerNewUser(name: String, email: String, password: String, bitmap: Bitmap?) {
        println("debug: viewmodel: registering user")
        authRepository.registerNewUser(name, email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                    updateState(RequestState.loading())
                }

                override fun onComplete() {
                    println("debug: viewmodel: successfully registered")
                    if (bitmap != null)
                        updateProfileImage(bitmap)
                    else
                        updateState(RequestState.success())
                }

                override fun onError(e: Throwable?) {
                    println("debug: viewmodel: failed to register ${e?.message}")
                    updateState(RequestState.error(e?.message))
                }
            })
    }

    fun updateProfileImage(bitmap: Bitmap) {
        databaseRepository.updateProfileImage(bitmap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                    updateState(RequestState.loading())
                }
                override fun onComplete() {
                    println("debug: viewmodel: successfully uploaded image")
                    updateState(RequestState.success())
                }
                override fun onError(e: Throwable?) {
                    println("debug: viewmodel: failed to upload image ${e?.message}")
                    updateState(RequestState.error(e?.message))
                }
            })
    }

    private fun updateState(requestState: RequestState) {
        _state.value = requestState
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
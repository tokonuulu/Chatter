package com.example.chatter.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatter.R
import com.example.chatter.utils.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, modelFactory)
            .get(LoginViewModel::class.java)

        back_to_registration_login.setOnClickListener {
            finish()
        }

        login_button_login.setOnClickListener {
            performSignIn()
        }

        startObservingRequestState()
    }

    fun startObservingRequestState() {
        viewModel.state.observe(this, Observer {requestState ->
            if (requestState == null) return@Observer

            when (requestState.status) {
                Status.LOADING -> {
                    println("Debug: loading")
                    loading_group_login.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    println("Debug: successfully signed in")
                    loading_group_login.visibility = View.GONE
                }
                Status.ERROR -> {
                    loading_group_login.visibility = View.GONE
                    Toast.makeText(this, "Couldn't sign in: ${requestState.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun performSignIn() {
        val email = email_textlayout_login.editText?.text.toString()
        val password = password_textlayout_login.editText?.text.toString()

        viewModel.signIn(email, password)
    }
}

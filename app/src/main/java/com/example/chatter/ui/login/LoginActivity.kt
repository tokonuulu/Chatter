package com.example.chatter.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatter.R
import com.example.chatter.ui.main.MainActivity
import com.example.chatter.ui.register.RegisterActivity
import com.example.chatter.utils.Status
import dagger.android.AndroidInjection
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

        register_textview_login.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button_login.setOnClickListener {
            requestSignIn()
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
                    onSuccessfullySignedIn()
                }
                Status.ERROR -> {
                    loading_group_login.visibility = View.GONE
                    Toast.makeText(this, "Couldn't sign in: ${requestState.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun requestSignIn() {
        val email = email_textlayout_login.editText?.text.toString()
        val password = password_textlayout_login.editText?.text.toString()

        viewModel.signIn(email, password)
    }

    private fun onSuccessfullySignedIn() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

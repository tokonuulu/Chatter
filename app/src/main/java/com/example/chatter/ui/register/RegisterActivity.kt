package com.example.chatter.ui.register

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatter.R
import com.example.chatter.ui.login.LoginActivity
import com.example.chatter.utils.Status
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RegisterViewModel
    private var imageUri : Uri? = null

    companion object {
        const val PICK_IMAGE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        AndroidInjection.inject(this)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewModel = ViewModelProvider(this, modelFactory)
            .get(RegisterViewModel::class.java)

        register_button_register.setOnClickListener {
            registerNewUser()
        }

        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }

        startObservingRequestState()
    }

    private fun startObservingRequestState() {
        viewModel.state.observe(this, Observer {requestState ->
            if (requestState == null) return@Observer

            when (requestState.status) {
                Status.LOADING -> {
                    println("Debug: loading")
                    loading_group_register.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    println("Debug: successfully created user")
                    loading_group_register.visibility = View.GONE
                }
                Status.ERROR -> {
                    loading_group_register.visibility = View.GONE
                    Toast.makeText(this, "Couldn't register the user: ${requestState.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun registerNewUser() {
        val name = name_textlayout_register.editText?.text.toString()
        val email = email_textlayout_register.editText?.text.toString()
        val password = password_textlayout_register.editText?.text.toString()
        val bitmap =
            if (imageUri != null)
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri!!))
            else
                null

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill empty fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.registerNewUser(name, email, password, bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri!!))
            select_photo_image_view.setImageBitmap(bitmap)
            select_photo_register.alpha = 0f
        }
    }

}

package com.example.chatter.ui.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.chatter.R
import com.example.chatter.ui.main.MainActivity
import com.example.chatter.utils.Status
import dagger.android.AndroidInjection
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject lateinit var modelFactory: ViewModelProvider.Factory
    @Inject lateinit var requestManager: RequestManager

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
            finish()
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
                    onSuccessfullySignedIn()
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

    private fun onSuccessfullySignedIn() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri!!))
            select_photo_image_view.setImageBitmap(bitmap)

            requestManager
                .load(imageUri)
                .transform(BlurTransformation())
                .into(blur_image_register)

            select_photo_register.alpha = 0f
        }
    }

}

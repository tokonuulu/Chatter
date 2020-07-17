package com.example.chatter.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.ui.login.LoginActivity
import dagger.android.support.DaggerAppCompatActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

class ProfileActivity : DaggerAppCompatActivity() {

    @Inject lateinit var modelFactory: ViewModelProvider.Factory
    @Inject lateinit var requestManager: RequestManager
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar_other_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"

        viewModel = ViewModelProvider(this, modelFactory)
            .get(ProfileViewModel::class.java)

        logout_button_profile.setOnClickListener {
            performSignOut()
        }
        startObservingUserProfile()
    }

    private fun startObservingUserProfile() {
        viewModel.user.observe(this, Observer { user->
            if (user == null) return@Observer

            updateUserName(user.name)
            updateUserEmail(user.email)
            if ( user.image != "default" )
                updateProfileImage(user.image)
        })
    }

    private fun performSignOut() {
        /* Sign the user out */
        viewModel.onSingOut()
        /* Go to login page */
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun updateUserEmail(email: String) {
        email_other_user.text = email
    }

    private fun updateUserName(name: String) {
        name_other_user.text = name
    }

    private fun updateProfileImage(imageUrl: String) {
        requestManager
            .load(imageUrl)
            .placeholder(R.drawable.default_image_150)
            .error(R.drawable.ic_baseline_error_150)
            .into(image_other_user)

        requestManager
            .load(imageUrl)
            .transform(BlurTransformation())
            .into(blur_image_other_user)
    }
}
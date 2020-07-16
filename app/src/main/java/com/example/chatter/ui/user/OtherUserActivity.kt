package com.example.chatter.ui.user

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.chatter.R
import com.example.chatter.ui.profile.ProfileViewModel
import dagger.android.support.DaggerAppCompatActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

class OtherUserActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: OtherUserViewModel
    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)

        setSupportActionBar(toolbar_other_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"

        viewModel = ViewModelProvider(this, modelFactory)
            .get(OtherUserViewModel::class.java)

        val uid = intent.getStringExtra("uid")
        if (uid == null)
            finish()
        viewModel.setUid(uid)

        startObservingUserProfile()
    }

    private fun startObservingUserProfile() {
        viewModel.user.observe(this, Observer { user ->
            if (user == null) return@Observer

            updateUserName(user.name)
            updateUserEmail(user.email)
            if (user.image != "default")
                updateProfileImage(user.image)
        })
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
            .placeholder(R.drawable.default_image_150)
            .error(R.drawable.ic_baseline_error_150)
            .into(blur_image_other_user)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
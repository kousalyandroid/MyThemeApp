package com.kousihub.mythemeapp

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kousihub.mythemeapp.databinding.ActivityMainBinding
import com.kousihub.mythemeapp.utils.toast

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding
    private var isSearchVisible = true
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        firebaseAnalytics = Firebase.analytics
        checkAnalytics()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun checkAnalytics() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, Bundle().apply {
            
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("New group") //0
        menu?.add("New broadcast") //1
        menu?.add("Linked devices") //2
        menu?.add("Starred messages") //3
        menu?.add("Payments") //4
        menu?.add("Settings") //5
        menu?.add("search")?.setIcon(R.drawable.ic_search)?.setShowAsAction(1) //6
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(6)?.setOnMenuItemClickListener {
            "Search".toast(this)
            openSearch()
            true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun openSearch() {
        if (isSearchVisible) {
            binding.cardvwSearch.apply {
                animate().translationX(0F)
                visibility = View.VISIBLE
            }
        } else {
            binding.cardvwSearch.apply {
                animate().translationX(width.toFloat())
                visibility = View.GONE
            }
        }
        isSearchVisible = !isSearchVisible
    }
}
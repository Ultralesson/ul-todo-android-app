package com.example.ul_todo_android_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // common variable declaration
    private val activityResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
            }
        }

    // locator variable declaration
    private var btnRegister: Button? = null
    private var btnLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // locator assignment
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)

        // ++++++++++++++++++++++++++++++++
        // OVERRIDDEN ACTION LISTENERS CALL
        // ++++++++++++++++++++++++++++++++

        // 1. click listeners
        btnLogin?.setOnClickListener(this)
        btnRegister?.setOnClickListener(this)
    }

    // ON CLICK OVERRIDING
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRegister -> {
                val registerIntent = Intent(applicationContext, RegisterActivity::class.java)
                activityResultLauncher.launch(registerIntent)
            }

            R.id.btnLogin -> {
                val loginIntent = Intent(applicationContext, LoginActivity::class.java)
                activityResultLauncher.launch(loginIntent)
            }
        }
    }
}
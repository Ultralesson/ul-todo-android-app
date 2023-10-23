package com.example.ul_todo_android_app

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class LoadingActivity : AppCompatActivity() {
    // Random delay duration between 3 to 10 seconds (3000 to 10000 milliseconds)
    private val minDelayMillis: Long = 3000 // 3 seconds
    private val maxDelayMillis: Long = 10000 // 10 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Get the target activity class name from extras
        val targetActivityClass = intent.getSerializableExtra("targetActivityClass") as? Class<*>

        // Check if the "dynamicMessage" extra exists in the Intent
        val dynamicMessage = intent.getStringExtra("dynamicMessage")

        // Generate a random delay between minDelayMillis and maxDelayMillis
        val randomDelayMillis = Random.nextLong(minDelayMillis, maxDelayMillis)

        // Create a CountDownTimer with the random delay
        object : CountDownTimer(randomDelayMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // This method is called every second (1000 milliseconds) until the delay is reached
            }

            override fun onFinish() {
                // When the random delay is finished, navigate to the target activity
                targetActivityClass?.let {
                    val intent = Intent(this@LoadingActivity, it)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }
}

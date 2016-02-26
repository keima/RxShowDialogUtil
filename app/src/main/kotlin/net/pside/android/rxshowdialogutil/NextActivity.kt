package net.pside.android.rxshowdialogutil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class NextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, NextActivity::class.java)
        }
    }
}

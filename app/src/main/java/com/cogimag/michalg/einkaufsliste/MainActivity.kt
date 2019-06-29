package com.cogimag.michalg.einkaufsliste

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    companion object {
        val CLASS_LOG_TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
        sharedViewModel.ladenId.observe(this, Observer<Long> {ladenId ->
//            Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG observe shared view model laden id $ladenId")
        })
    }

    override fun onResume() {
        super.onResume()
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG on resume")

    }

//    fun main_btn_test_view_model_Click(view : View) {
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG click on main_btn_test_view_model")
//        val sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
////        sharedViewModel.ladenId.observe(this, Observer<Long> {ladenId ->
////            Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG observe shared view model laden id $ladenId")
////        })
//        Log.i(AppKonstante.APP_LOG_TAG, "$CLASS_LOG_TAG laden id = ${sharedViewModel.ladenId.value}")
//    }


}

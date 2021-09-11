package com.digitalgenius.androidservices

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyIntentService : IntentService("MyIntentService") {

    private val TAG="MyIntentService"
    init {
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onStartCommand: thread name ${Thread.currentThread().name}")
        val songName=intent!!.getStringExtra(Veriables.MESSAGE_KEY)
        downloadSomething(songName)

        val intent=Intent(Veriables.MAIN_ACTIVITY_BROADCAST_ACTION)
        intent.putExtra(Veriables.MESSAGE_KEY,"From Intent Service I am coming")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }


    private fun downloadSomething(songName: String?) {
        Log.d(TAG, "downloadSomething: ${Thread.currentThread().name}")
        Log.d(TAG, "downloadSomething: $songName download started")
        Log.d(TAG, "downloadSomething: $songName download working")
        Log.d(TAG, "downloadSomething: $songName download finished")
    }

}
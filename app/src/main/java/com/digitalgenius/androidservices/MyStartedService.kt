package com.digitalgenius.androidservices

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.ResultReceiver
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyStartedService : Service() {

//    startService()
//    onCreate()
//    onStartCommand()
//    Service Running
//    onDestroy()

    private val TAG="MyStartedService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: called")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: called")
        val songName=intent!!.getStringExtra(Veriables.MESSAGE_KEY)
        CoroutineScope(Dispatchers.Default).launch {
            downloadSomething(songName)
        }
        val intent=Intent(Veriables.MAIN_ACTIVITY_BROADCAST_ACTION)
        intent.putExtra(Veriables.MESSAGE_KEY,"From Started Intent I am coming")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        stopSelfResult(startId)


        return START_REDELIVER_INTENT;
    }
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind: called")
        return null
    }

    private fun downloadSomething(songName: String?) {
        Log.d(TAG, "downloadSomething: ${Thread.currentThread().name}")
        Log.d(TAG, "downloadSomething: $songName download started")
        Log.d(TAG, "downloadSomething: $songName download working")
        Log.d(TAG, "downloadSomething: $songName download finished")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }
}
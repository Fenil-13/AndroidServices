package com.digitalgenius.androidservices.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.digitalgenius.androidservices.R
import com.digitalgenius.androidservices.Veriables

class MusicPlayerService : Service() {

//    startService()
//    onCreate()
//    onBind()
//    Clients are bound to service
//    onUnBind()
//    onDestroy()
//    Service Shut down

    private val TAG = "MusicPlayerService"
    private val mBinder = MyServiceBinder()

    private lateinit var mPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: calling")
        mPlayer = MediaPlayer.create(this, R.raw.samplemusic)
        mPlayer.setOnCompletionListener {
            val intent=Intent(Veriables.MUSIC_ACTIVITY_BROADCAST_ACTION)
            intent.putExtra(Veriables.MESSAGE_KEY,"done")
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: calling")

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: calling")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: calling")
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: calling")
        mPlayer.release()
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: calling")
    }

    inner class MyServiceBinder : Binder() {

        fun getService(): MusicPlayerService {
            return this@MusicPlayerService;
        }

    }


    //Client method

    fun isPlaying(): Boolean = mPlayer.isPlaying

    fun play() = mPlayer.start()

    fun pause() = mPlayer.pause()


}
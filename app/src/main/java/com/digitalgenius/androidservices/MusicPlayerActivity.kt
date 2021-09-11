package com.digitalgenius.androidservices

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.digitalgenius.androidservices.databinding.ActivityMusicPlayerBinding
import com.digitalgenius.androidservices.services.MusicPlayerService

class MusicPlayerActivity : AppCompatActivity() {

    private val TAG = "MusicPlayerActivity"

    private lateinit var binding: ActivityMusicPlayerBinding

    private lateinit var musicPlayerService: MusicPlayerService
    private var mBound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            Log.d(TAG, "onServiceConnected: calling")

            val myServiceBinder: MusicPlayerService.MyServiceBinder =
                service as MusicPlayerService.MyServiceBinder

            musicPlayerService = myServiceBinder.getService()

            mBound = true

            if (musicPlayerService.isPlaying()) {
                binding.btnToggleMusic.text = "PAUSE"
            } else {
                binding.btnToggleMusic.text = "PLAY"
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: calling")
            mBound = false
        }

    }

    private val uiBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent!!.getStringExtra(Veriables.MESSAGE_KEY)
            when(message){
                "done"->binding.btnToggleMusic.text = "PLAY"
                "play"->binding.btnToggleMusic.text = "PLAY"
                "pause"->binding.btnToggleMusic.text = "PAUSE"
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        with(binding) {
            btnToggleMusic.setOnClickListener {
                if (mBound) {
                    if (musicPlayerService.isPlaying()) {
                        musicPlayerService.pause()
                    } else {
                        val intent=Intent(this@MusicPlayerActivity,MusicPlayerService::class.java)
                        intent.setAction(Veriables.MUSIC_SERVICE_ACTION_START)
                        startService(intent)
                        musicPlayerService.play()
                    }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this@MusicPlayerActivity, MusicPlayerService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        LocalBroadcastManager.getInstance(this@MusicPlayerActivity)
            .registerReceiver(
                uiBroadcastReceiver,
                IntentFilter(Veriables.MUSIC_ACTIVITY_BROADCAST_ACTION)
            )
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(serviceConnection)
            mBound = false
        }

        LocalBroadcastManager.getInstance(this@MusicPlayerActivity)
            .unregisterReceiver(uiBroadcastReceiver)
    }
}
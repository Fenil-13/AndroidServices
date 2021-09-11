package com.digitalgenius.androidservices.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.digitalgenius.androidservices.R
import com.digitalgenius.androidservices.Veriables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            val intent = Intent(Veriables.MUSIC_ACTIVITY_BROADCAST_ACTION)
            intent.putExtra(Veriables.MESSAGE_KEY, "done")
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            stopForeground(true)
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: calling")

        when (intent!!.action) {
            Veriables.MUSIC_SERVICE_ACTION_PLAY -> {
                play()
            }
            Veriables.MUSIC_SERVICE_ACTION_PAUSE -> {
                pause()
            }
            Veriables.MUSIC_SERVICE_ACTION_STOP -> {
                stopForeground(true)
                stopSelf()
            }
            Veriables.MUSIC_SERVICE_ACTION_START -> {
                showNotification()
            }
            else -> {
                stopForeground(true)
                stopSelf()
            }
        }

        showNotification()
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

    fun play() {
        val intent = Intent(Veriables.MUSIC_ACTIVITY_BROADCAST_ACTION)
        intent.putExtra(Veriables.MESSAGE_KEY, "pause")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        mPlayer.start()
    }

    fun pause() {
        val intent = Intent(Veriables.MUSIC_ACTIVITY_BROADCAST_ACTION)
        intent.putExtra(Veriables.MESSAGE_KEY, "play")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        mPlayer.pause()
    }

    private fun showNotification() {

        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, "123456");
        val playIntent = Intent(this, MusicPlayerService::class.java)
        playIntent.setAction(Veriables.MUSIC_SERVICE_ACTION_PLAY)

        val playPendingIntent =
            PendingIntent.getService(this, 100, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(this, MusicPlayerService::class.java)
        pauseIntent.setAction(Veriables.MUSIC_SERVICE_ACTION_PAUSE)

        val pausePendingIntent =
            PendingIntent.getService(this, 100, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val stopIntent = Intent(this, MusicPlayerService::class.java)
        stopIntent.setAction(Veriables.MUSIC_SERVICE_ACTION_STOP)

        val stopPendingIntent =
            PendingIntent.getService(this, 100, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentText("Awesome Music ...fill it")
            .setContentTitle("Pro Music Player")
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_play,
                    "Play",
                    playPendingIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_pause,
                    "Pause",
                    pausePendingIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_ff,
                    "Stop",
                    stopPendingIntent
                )
            )

        val notification = builder.build()

        startForeground(123, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "MusicPlayer"
            val descriptionText = "Use for Playing Music"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("123456", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}
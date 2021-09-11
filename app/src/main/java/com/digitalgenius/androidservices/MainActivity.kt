package com.digitalgenius.androidservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.digitalgenius.androidservices.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG="MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val uiBroadcastReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val message=intent!!.getStringExtra(Veriables.MESSAGE_KEY)
            Toast.makeText(this@MainActivity, "$message", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(uiBroadcastReceiver,
            IntentFilter( Veriables.MAIN_ACTIVITY_BROADCAST_ACTION))

        var intent: Intent?=null
        binding.btnStartStartedService.setOnClickListener {

            intent = Intent(this@MainActivity,MyStartedService::class.java)
            intent!!.putExtra(Veriables.MESSAGE_KEY,"Dil mange More")
            startService(intent)
        }

        binding.btnStopStartedService.setOnClickListener {
            stopService(intent)
        }


        binding.btnStartIntentService.setOnClickListener {
            intent = Intent(this@MainActivity,MyIntentService::class.java)
            intent!!.putExtra(Veriables.MESSAGE_KEY,"Dil mange More")
            startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(uiBroadcastReceiver)
    }
}
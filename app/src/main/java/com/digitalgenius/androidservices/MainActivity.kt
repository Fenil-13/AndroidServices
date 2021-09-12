package com.digitalgenius.androidservices

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.digitalgenius.androidservices.databinding.ActivityMainBinding
import com.digitalgenius.androidservices.jobschedularApi.MyDownloadJob
import com.digitalgenius.androidservices.services.MyIntentService
import com.digitalgenius.androidservices.services.MyStartedService

class MainActivity : AppCompatActivity() {
    private val TAG="MainActivity"
    private val JOB_ID=101
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
        val jobScheduler=getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

        binding.btnStartStartedService.setOnClickListener {

            intent = Intent(this@MainActivity, MyStartedService::class.java)
            intent!!.putExtra(Veriables.MESSAGE_KEY,"Dil mange More")
            startService(intent)
        }

        binding.btnStopStartedService.setOnClickListener {
            stopService(intent)
        }


        binding.btnStartIntentService.setOnClickListener {
            intent = Intent(this@MainActivity, MyIntentService::class.java)
            intent!!.putExtra(Veriables.MESSAGE_KEY,"Dil mange More")
            startService(intent)
        }

        binding.btnStartMusicPlayerService.setOnClickListener {
            intent = Intent(this@MainActivity, MusicPlayerActivity::class.java)
            startActivity(intent)
        }

        binding.btnStartJobScheduler.setOnClickListener {
            
            val componentName=ComponentName(this,MyDownloadJob::class.java)
            val jobInfo=JobInfo.Builder(JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(3000)
                .setOverrideDeadline(15*60*1000L)
                .setPersisted(true)
                .build()

            val result = jobScheduler.schedule(jobInfo)
            if(result==JobScheduler.RESULT_SUCCESS){
                Log.d(TAG, "onCreate: Job Scheduled")
            }else{
                Log.d(TAG, "onCreate: Job not Scheduled")
            }
        }

        binding.btnStopJobScheduler.setOnClickListener {
            jobScheduler.cancel(JOB_ID)
            Log.d(TAG, "onCreate: Job Cancelled")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(uiBroadcastReceiver)
    }
}
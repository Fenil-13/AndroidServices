package com.digitalgenius.androidservices.jobschedularApi

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyDownloadJob : JobService() {

    private val TAG="MyDownloadJob"
    private var isJobCancelled=false
    private var mSuccess=false

    override fun onStartJob(params: JobParameters?): Boolean {

        Log.d(TAG, "onStartJob: called")
        Log.d(TAG, "onStartJob: Thread Name ${Thread.currentThread().name}")

        CoroutineScope(Dispatchers.IO).launch {
            if(!isJobCancelled){
                Log.d(TAG, "onStartJob: inside CoroutineScope Thread Name ${Thread.currentThread().name}")
                Log.d(TAG, "onStartJob: Download Started")
                delay(1000)
                Log.d(TAG, "onStartJob: Download Completed")
                mSuccess=true
            }
            jobFinished(params,false)
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        isJobCancelled=true

        return false
    }
}
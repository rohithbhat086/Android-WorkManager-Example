package com.rgb.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class SampleOneTimeWork(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object{
        const val KEY_OUTPUT = "MY_OUTPUT_Key"
    }

    override fun doWork(): Result {
        Log.i("WM_sample", "Thread : ${Thread.currentThread().name}")
        val data = inputData.getInt(MainActivity.KEY_INPUT, 0)
        for (i in 1 until data){
            Log.d("WM_sample", "running: $i")
        }
        val outData = Data.Builder()
            .putString(KEY_OUTPUT, "Re:Done")
            .build()
        return Result.success(outData)
    }

}
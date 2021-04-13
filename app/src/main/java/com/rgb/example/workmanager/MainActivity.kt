package com.rgb.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.*
import com.rgb.example.workmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object{
        const val KEY_INPUT = "MY_INPUT_Key"
    }

    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        mBinding.buttonOneTime.setOnClickListener {
//            runOneTimeRequests()
            runPeriodicWork()
        }
    }

    fun runPeriodicWork(){
        Log.i("WM_sample", "runPeriodicWork Thread ----: ${Thread.currentThread().name}")
        val contraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
        val inData = Data.Builder()
                .putInt(KEY_INPUT, 50000)
                .build()
        var work = PeriodicWorkRequest.Builder(SampleOneTimeWork::class.java, 16, TimeUnit.MINUTES)
                .setConstraints(contraints)
                .setInputData(inData)
                .build()
        WorkManager.getInstance(applicationContext).enqueue(work)
    }


    fun runOneTimeRequests(){
        Log.i("WM_sample", "Thread ----: ${Thread.currentThread().name}")
        val contraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
        val inData = Data.Builder()
                .putInt(KEY_INPUT, 50000)
                .build()
        var oneTimeWork = OneTimeWorkRequest.Builder(SampleOneTimeWork::class.java)
                .setConstraints(contraints)
                .setInputData(inData)
                .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(oneTimeWork)
//            var oneTimeWork2 = OneTimeWorkRequest.Builder(SampleOneTimeWork::class.java)
//                    .setConstraints(contraints)
//                    .setInputData(inData)
//                    .build()
//            var oneTimeWork3 = OneTimeWorkRequest.Builder(SampleOneTimeWork::class.java)
//                    .setConstraints(contraints)
//                    .setInputData(inData)
//                    .build()
//            var oneTimeWork4 = OneTimeWorkRequest.Builder(SampleOneTimeWork::class.java)
//                    .setConstraints(contraints)
//                    .setInputData(inData)
//                    .build()
//
//            val parallelWorks = mutableListOf<OneTimeWorkRequest>()
//            parallelWorks.add(oneTimeWork)
//            parallelWorks.add(oneTimeWork4)
//
//            workManager.beginWith(parallelWorks)
//                    .then(oneTimeWork2)
//                    .then(oneTimeWork3)
//                    .enqueue()

        workManager.getWorkInfoByIdLiveData(oneTimeWork.id).observe(this, Observer {
            Log.i("WM_sample", "State: ${it.state.name}")
            mBinding.textState.text = it.state.name
            if(it.state.isFinished){
                val output = it.outputData.getString(SampleOneTimeWork.KEY_OUTPUT) ?: ""
                mBinding.textState.append("  $output")
            }
        })
    }
}
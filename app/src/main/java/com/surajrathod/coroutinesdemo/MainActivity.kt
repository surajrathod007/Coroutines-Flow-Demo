package com.surajrathod.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.surajrathod.coroutinesdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var job: Job
    var isStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        printData()
        //loadAns()
    }

    fun printData(){
        CoroutineScope(Dispatchers.Main).launch {
            //printMyData()
            printMyDataAsync()
        }
    }

    private suspend fun printMyDataAsync() {
        var data = "EMpty"
        val job = CoroutineScope(Dispatchers.Main).async {
            getData()
        }
        data = job.await()
        Log.d("COROUTINE","$data")
    }

    private suspend fun printMyData() {
        var data = "EMpty"
        val job = CoroutineScope(Dispatchers.Main).launch {
            data = getData()
        }
        job.join()
        Log.d("COROUTINE","$data")
    }

    suspend fun getData() : String{
        delay(3000)
        return "My Data"
    }
    fun loadAns(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            binding.txtAns1.text = "Im set"
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            binding.txtAns2.text = "Im set"
        }
    }

    fun longRunningTask() {
        for (i in 1..1000000000L) {

        }
    }

    fun updateCount(view: View) {
        binding.txtAns.text = (binding.txtAns.text.toString().toInt() + 1).toString()
    }

    fun getMyJob() : Job{
        return CoroutineScope(Dispatchers.Default).launch {
            for (i in 1..100) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    binding.txtAns.text = i.toString()
                }
            }
        }
    }
    fun counter(view: View) {
        if(!this::job.isInitialized){
            job = getMyJob()
        }
        if (isStarted) {
            job.cancel()
            isStarted = false
        } else {
            job = getMyJob()
            job.start()
            isStarted = true
        }
    }

    fun longTask(view: View) {
        thread(start = true) {
            longRunningTask()
        }
    }
}
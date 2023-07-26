package com.surajrathod.coroutinesdemo

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class FlowsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flows)
//        lifecycleScope.launch(Dispatchers.IO) {
//            getUserList().forEach {
//                Log.d("FLOWSURAJ","$it $coroutineContext")
//            }
//        }
//        lifecycleScope.launch{
//            delay(5000)
//            getFlowUserList().collect{
//                Log.d("FLOWSURAJ","Consumer 1 : $it $coroutineContext")
//            }
//        }

//        lifecycleScope.launch{
//            getFlowUserList().collect{
//                Log.d("FLOWSURAJ","Consumer 2 : $it $coroutineContext")
//            }
//        }

//        lifecycleScope.launch {
//            getSharedUserList().collect {
//                Log.d("FLOWSURAJ", "Consumer 1 : $it $coroutineContext")
//            }
//        }
//        lifecycleScope.launch {
//            delay(5000)
//            getSharedUserList().collect {
//                Log.d("FLOWSURAJ", "Consumer 2 : $it $coroutineContext")
//            }
//        }

        CoroutineScope(Dispatchers.Main).launch {
            val p = ProgressDialog(this@FlowsActivity)

            val data = getProgressFlow()
            data.onCompletion {
                p.dismiss()
            }.collect {
                Log.d("FLOWSURAJ", "Collect on : $coroutineContext")
                if (it) {
                    p.show()
                } else {
                    p.hide()
                }
            }
        }
    }


    suspend fun getProgressFlow(): Flow<Boolean> {
        val state = MutableSharedFlow<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {

            for (i in 1..6) {
                delay(2000)
                Log.d("FLOWSURAJ", "Emit on : $coroutineContext")
                if (i % 2 == 0) {
                    state.emit(true)
                } else {
                    state.emit(false)
                }
            }
        }

        return state
    }

    suspend fun getFlowUserList() = flow<String> {
        for (i in 1..10) {
            delay(1000)
            emit(i.toString())
        }
    }

    suspend fun getSharedUserList(): MutableSharedFlow<Int> {
        val data = MutableSharedFlow<Int>()
        GlobalScope.launch {
            for (i in 1..10) {
                delay(1000)
                data.emit(i)
            }
        }
        return data
    }

    suspend fun getUserList(): List<String> {
        val list = mutableListOf<String>()
        list.add(getUser())
        list.add(getUser())
        list.add(getUser())
        return list
    }

    suspend fun getUser(): String {
        delay(1000)
        return "User"
    }
}
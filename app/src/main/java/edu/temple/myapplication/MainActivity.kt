package edu.temple.myapplication

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var isConnected = false
    lateinit var timerBinder: TimerService.TimerBinder
    val initialValue = 100

    val timerHandler = Handler(Looper.getMainLooper()) {
        findViewById<TextView>(R.id.textView).text = it.what.toString()
        true
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bindService()
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerBinder = service as TimerService.TimerBinder
                timerBinder.setHandler(timerHandler)
                isConnected = true
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                isConnected = false
            }
        }

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE)

        var paused = true
        with (findViewById<Button>(R.id.startButton)){
            setOnClickListener {
                if (text == "Start") {
                    timerBinder.start(initialValue)
                    text = "Pause"
                }
                else if (text == "Resume") {
                    timerBinder.pause()
                    text = "Pause"
                }
                else {
                    timerBinder.pause()
                    text = "Resume"
                    paused = true
                }
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (isConnected) {
                timerBinder.stop()
                findViewById<Button>(R.id.startButton).text = "Start"
            }
        }
    }
}
package edu.temple.myapplication

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {
    var isConnected = false
    lateinit var timerBinder: TimerService.TimerBinder

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bindService()
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerBinder = service as TimerService.TimerBinder
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
                if (paused) {
                    if (text == "Start") {
                        timerBinder.start(100)
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
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (isConnected) {
                timerBinder.stop()
                isConnected = false
                findViewById<Button>(R.id.startButton).text = "Start"
            }
        }
    }
}
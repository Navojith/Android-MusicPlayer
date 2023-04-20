package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    lateinit var background: ImageView
    private lateinit var playPauseButton:ImageView
    private lateinit var skipButton: ImageView
    private lateinit var tvMusicTitle: TextView
    private var isPlaying = false
    private var isBound = false
    private lateinit var musicPlayerService: MusicPlayerService
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playPauseButton = findViewById(R.id.btnPlayPause)
        skipButton = findViewById(R.id.btnSkip)
        tvMusicTitle = findViewById(R.id.tvMusicTitle)
        background = findViewById(R.id.bg)

        playPauseButton.setOnClickListener {

            if (!isPlaying){
                playPauseButton.setImageResource(R.drawable.baseline_pause_circle_24)
                musicPlayerService.play()
                tvMusicTitle.text = musicPlayerService.nowPlaying
                isPlaying = true
            }else{
                playPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
                musicPlayerService.pauseTrack()
                isPlaying = false
            }
        }
        skipButton.setOnClickListener {
            when (index) {
                0 -> {
                    background.setImageResource(R.drawable.download2)
                    index ++
                }
                1 -> {
                    background.setImageResource(R.drawable.download3)
                    index ++
                }
                2 -> {
                    background.setImageResource(R.drawable.download)
                    index = 0
                }
            }
            musicPlayerService.skipTrack()
            tvMusicTitle.text = musicPlayerService.nowPlaying
        }
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicPlayerService.LocalBinder
            musicPlayerService = binder.getService()
            isBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicPlayerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d("MainActivity","Service Started")
    }
    override fun onStop() {
        super.onStop()
        if (isBound) {
           // unbindService(serviceConnection)
            isBound = false
        }
    }

}
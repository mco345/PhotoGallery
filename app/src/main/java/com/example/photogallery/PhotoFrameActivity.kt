package com.example.photogallery

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0

    private var timer: Timer? = null

    private val photoImageView: ImageView by lazy {
        findViewById(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent(){
        val size = intent.getIntExtra("photoListSize", 0)
        for(i in 0..size) {
            intent.getStringExtra("photo$i")?.let{
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer(){
        timer = timer(period = 5 * 1000){
            runOnUiThread {
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f // 투명도(0f : 완전투명)
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()    // 애니메이션
                    .alpha(1.0f)
                    .setDuration(1000) //1초
                    .start()

                currentPosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }
}
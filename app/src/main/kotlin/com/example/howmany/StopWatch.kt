package com.example.howmany

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stop_watch.*
import java.util.*
import kotlin.concurrent.timer

class StopWatch : AppCompatActivity() {
    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var Lap = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_watch)

        lapButton.setOnClickListener {
            recordLapTime()
        }

        resetFab.setOnClickListener {
            reset()
        }


        fab.setOnClickListener {
            isRunning = !isRunning

            if(isRunning){
                start()
            } else {
                pause()
            }
        }
    }

    private fun recordLapTime(){
        val lapTime = this.time
        val textView = TextView(this)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30F)
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        textView.text = "$Lap 세트 : ${lapTime / 6000}" + "분 " + "${(lapTime/100) % 60}" + "초"
        //맨 위에 랩타임 추가가
        lapLayout.addView(textView, 0)
        Lap++
    }


    private fun pause(){
        fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        timerTask?.cancel()
    }

    private fun start(){
        fab.setImageResource(R.drawable.ic_baseline_pause_24)

        timerTask = timer(period = 10){
            time++
            var min = time / 6000 //분
            val sec = (time / 100)%60    //초

            Log.d("TAG","time : " + time + " " + "min :  " + min + " " + "sec :  " + sec)

            runOnUiThread {
                secTextView.text = "$min" //분
                milliTextView.text = "$sec" //초
            }

        }

    }

    private fun reset(){
        timerTask?.cancel()

        //모든 변수 초기화
        time = 0
        isRunning = false
        fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        secTextView.text = "0" // 분
        milliTextView.text = "0" //초

        lapLayout.removeAllViews()
        Lap = 1
    }




}
package com.stacker.training_timer

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    var timerTask: Timer? = null
    var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = btn_main_start
        val stopButton = btn_main_stop
        val pauseButton = btn_main_pause
        var isStart = false

        startButton.setOnClickListener {
            if(isStart == false){
                isStart = true
                var training_min = SixteenCheck(Integer.parseInt(nullCheck(ntv_main_trainingMin.text.toString())))
                var training_sec = SixteenCheck(Integer.parseInt(nullCheck(ntv_main_trainingSec.text.toString())))
                var training_milSec = MilliCheck(Integer.parseInt(nullCheck(ntv_main_trainingMilSec.text.toString())))
                var training_rotate = RotateCheck(Integer.parseInt(nullCheck(ntv_main_rotate.text.toString())))
                var break_min = SixteenCheck(Integer.parseInt(nullCheck(ntv_main_breakMin.text.toString())))
                var break_sec = SixteenCheck(Integer.parseInt(nullCheck(ntv_main_breakSec.text.toString())))
                var break_milSec = MilliCheck(Integer.parseInt(nullCheck(ntv_main_breakMilSec.text.toString())))

                start(training_min, training_sec, training_milSec, training_rotate,break_min,break_sec,break_milSec)
            }

        }

        stopButton.setOnClickListener {
            stop()
        }

        pauseButton.setOnClickListener {
            pause()
        }

    }

    private fun nullCheck(str:String):String{
        if(str==""){
            return "0"
        }
        else{
            return str
        }
    }

    private fun SixteenCheck(par:Int):Int{
        if(par > 60){
            return 60
        }
        else if(par < 0){
            return 0
        }
        else{
            return par
        }
    }

    private fun MilliCheck(par:Int):Int{
        if(par > 1000){
            return 1000
        }
        else if(par < 0){
            return 0
        }
        else{
            return par
        }
    }

    private fun RotateCheck(par:Int):Int{
        if(par < 0){
            return 1
        }
        else{
            return par
        }
    }

    private fun pause() {
        timerTask?.purge()
        timerTask?.cancel()
    }

    private fun stop() {
        timerTask?.cancel()
    }

    private fun start(training_min:Int, training_sec:Int, training_milSec:Int, training_rotate:Int, break_min:Int, break_sec:Int, break_milSec:Int):Unit {
        val tone = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        var tMin = training_min
        var tSec = training_sec
        var tMilSec = training_milSec
        var tRotate = training_rotate
        var bMin = break_min
        var bSec = break_sec
        var bMilSec = break_milSec
        var flag:Boolean = true

        timerTask?.purge()

        timerTask = timer(period = 10, initialDelay = 10) {
            time = 10
            if(flag == true){
                if(tMilSec == 0){
                    if(tSec == 0){
                        if(tMin == 0){
                            if(tRotate == 1){
                                timerTask?.cancel()
                            }
                            else{
                                flag = false
                                tone.startTone(ToneGenerator.TONE_DTMF_S, 500)
                                tRotate--
                                tMin = training_min
                                tSec = training_sec
                                tMilSec = training_milSec
                            }

                        }
                        else{
                            tMin--
                            tSec = 60
                            tMilSec = 1000
                        }
                    }
                    else{
                        tSec--
                        tMilSec = 1000
                    }
                }

                tMilSec -= time
                if(tMilSec < 0){
                    tMilSec = 0
                }

            }

            else{

                if(bMilSec <= 0){
                    if(bSec == 0){
                        if(bMin == 0){
                            bMin = break_min
                            bSec = break_sec
                            bMilSec = break_milSec
                            flag = true
                            tone.startTone(ToneGenerator.TONE_DTMF_S, 100)
                        }
                        else{
                            bMin--
                            bSec = 60
                            bMilSec = 1000
                        }
                    }
                    else{
                        bSec--
                        bMilSec = 1000
                    }
                }

                bMilSec -= time
                if(bMilSec < 0){
                    bMilSec = 0
                }
            }

            runOnUiThread {
                ntv_main_trainingMin.setText(tMin.toString())
                ntv_main_trainingSec.setText(tSec.toString())
                ntv_main_trainingMilSec.setText(tMilSec.toString())
                ntv_main_rotate.setText(tRotate.toString())
                ntv_main_breakMin.setText(bMin.toString())
                ntv_main_breakSec.setText(bSec.toString())
                ntv_main_breakMilSec.setText(bMilSec.toString())
            }

        }
    }

}
package com.example.recipes

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.sqrt

class TimerFragment : Fragment(), View.OnClickListener {
    private var secondsStart = 0
    private var seconds = 0
    private var running = false
    private var wasRunning = false
    private var name = ""
    private var btnStart: ImageButton? = null
    private var btnStop: ImageButton? = null
    private var btnReset: ImageButton? = null
    private var btnTurnOff: ImageButton? = null
    private var alertSound: MediaPlayer? = null

    fun initTimer(n: String, s: Int) {
        name = n
        secondsStart = s
        seconds = secondsStart
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            secondsStart = savedInstanceState.getInt("secondsStart")
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
            name = savedInstanceState.getString("name").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        SavedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_timer, container, false)
        runTimer(layout)
        btnStart = layout.findViewById<View>(R.id.start_button) as ImageButton
        btnStop = layout.findViewById<View>(R.id.stop_button) as ImageButton
        btnReset = layout.findViewById<View>(R.id.reset_button) as ImageButton
        btnTurnOff = layout.findViewById<View>(R.id.turnoff_button) as ImageButton
        btnStart?.setOnClickListener(this)
        btnStop?.setOnClickListener(this)
        btnReset?.setOnClickListener(this)
        btnTurnOff?.setOnClickListener(this)
        val textTimerName: TextView = layout.findViewById(R.id.textTimerName) as TextView
        textTimerName.text = name
        return layout
    }

    override fun onPause() {
        super.onPause()
        alertSound?.stop()
        wasRunning = running
        running = false
        setBtnVisibility(showBtnStart =  true, showBtnReset =  wasRunning)
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) running = true
        if(seconds <= 0) {
            setBtnVisibility(showBtnTurnOff = true)
            onFinish()
        }
        else if(running) setBtnVisibility(showBtnStop = wasRunning)
        else if(seconds != secondsStart) setBtnVisibility(showBtnStart = true, showBtnReset = true)
        else setBtnVisibility(showBtnStart = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        alertSound?.stop()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("secondsStart", secondsStart)
        savedInstanceState.putInt("seconds", seconds)
        savedInstanceState.putBoolean("running", running)
        savedInstanceState.putBoolean("wasRunning", wasRunning)
        savedInstanceState.putString("name", name)
        alertSound?.stop()
    }

    private fun onClickStart() {
        running = true
        setBtnVisibility(showBtnStop =  true)
    }

    private fun onClickStop() {
        running = false
        setBtnVisibility(showBtnStart = true, showBtnReset = (seconds != secondsStart))
    }

    private fun onClickReset() {
        seconds = secondsStart
        setBtnVisibility(showBtnStart = true)
    }

    private fun onClickTurnOff() {
        running = false
        seconds = secondsStart
        alertSound?.stop()
        getAnimation(false).start()
        setBtnVisibility(showBtnStart = true)
    }

    private fun runTimer(view: View) {
        val timeView = view.findViewById<View>(R.id.time_view) as TextView
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if(running) seconds--
                if(seconds > 0) setTimeInTimeView(timeView, seconds)
                else setTimeInTimeView(timeView, seconds)

                if(running && seconds <= 0) {
                    if(seconds == 0) {
                        setBtnVisibility(showBtnTurnOff = true)
                        onFinish()
                    }
                    else {
                        var speed = log(((-seconds).toDouble()), 10.0).toFloat()
                        if(speed < 1) speed = 1.0F
                        var volume = 1F
                        if(sqrt(-seconds.toFloat()) < 15) {
                            volume = log10(sqrt(-seconds.toFloat())) / log10(15F)
                        }
                        if(volume < 0.05F) volume = 0.05F
                        alertSound?.playbackParams = PlaybackParams().setSpeed(speed)
                        alertSound?.setVolume(volume, volume)

                        if((-seconds) % 2 == 0) getAnimationScale().start()
                    }
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.start_button -> onClickStart()
            R.id.stop_button -> onClickStop()
            R.id.reset_button -> onClickReset()
            R.id.turnoff_button -> onClickTurnOff()
        }
    }

    private fun setBtnVisibility(
        showBtnStart: Boolean = false,
        showBtnStop: Boolean = false,
        showBtnReset: Boolean = false,
        showBtnTurnOff: Boolean = false
    ) {
        btnStart?.visibility = if (showBtnStart) View.VISIBLE else View.GONE
        btnStop?.visibility = if (showBtnStop) View.VISIBLE else View.GONE
        btnReset?.visibility = if (showBtnReset) View.VISIBLE else View.GONE
        btnTurnOff?.visibility = if (showBtnTurnOff) View.VISIBLE else View.GONE
    }

    private fun onFinish() {
        alertSound = MediaPlayer.create(context, R.raw.timer)
        alertSound?.isLooping = true
        alertSound?.setVolume(0.05F, 0.05F)
        alertSound?.start()
        getAnimation(true).start()
    }

    private fun getAnimation(finish: Boolean): AnimatorSet {
        val timeView = requireView().findViewById<TextView>(R.id.time_view)
        val colorNormal = resources.getColor(R.color.timer_normal)
        val colorAlarm = resources.getColor(R.color.timer_alarm)
        val vC = if(finish) arrayListOf(colorNormal, colorAlarm) else arrayListOf(colorAlarm, colorNormal)
        val colorAnimation = ObjectAnimator.ofArgb(timeView, "textColor", vC[0], vC[1]).setDuration(1000)
        val animationSet = AnimatorSet()
        if(!finish) {
            animationSet.play(colorAnimation)
            return animationSet
        }
        animationSet.play(colorAnimation).with(getAnimationScale())
        return animationSet
    }

    private fun getAnimationScale(): AnimatorSet {
        val timeView = requireView().findViewById<TextView>(R.id.time_view)
        val vS = arrayListOf(1f, 0.75f)
        val scaleXAnimation1 = ObjectAnimator.ofFloat(timeView, "scaleX", vS[0], vS[1]).setDuration(500)
        val scaleYAnimation1 = ObjectAnimator.ofFloat(timeView, "scaleY", vS[0], vS[1]).setDuration(500)
        val scaleXAnimation2 = ObjectAnimator.ofFloat(timeView, "scaleX", vS[1], vS[0]).setDuration(500)
        val scaleYAnimation2 = ObjectAnimator.ofFloat(timeView, "scaleY", vS[1], vS[0]).setDuration(500)
        val animationSet = AnimatorSet()
        animationSet.play(scaleXAnimation1).with(scaleYAnimation1).before(scaleXAnimation2).before(scaleYAnimation2)
        return animationSet
    }

    private fun setTimeInTimeView(timeView: TextView, sec: Int) {
        var seconds = sec
        var sign = " "
        if(seconds < 0) {
            sign = "-"
            seconds *= -1
        }
        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        val secs = seconds % 60
        val time = String.format("%d:%02d:%02d", hours, minutes, secs)
        val txt = "$sign $time"
        timeView.text = txt
    }
}

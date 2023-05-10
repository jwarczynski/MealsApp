package com.example.recipes

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment


class TopFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAnimationSet(true).start()
        val imgPizza = requireView().findViewById<ImageView>(R.id.imgPizza)
        imgPizza.setOnClickListener {
            val animationSetFirst = getAnimationSet(false)
            val animationSetSecond = getAnimationSet(true)
            val animatorSet = AnimatorSet()
            animatorSet.play(animationSetFirst).before(animationSetSecond)
            animatorSet.start()
        }
    }

    private fun getAnimationSet(isStart: Boolean): AnimatorSet{
        val vR = if (isStart) arrayOf(0f, 360f) else arrayOf(360f, 0f)
        val vS = if (isStart) arrayOf(0.3f, 1f) else arrayOf(1f, 0.3f)
        val imgPizza = requireView().findViewById<ImageView>(R.id.imgPizza)
        val rotation = ObjectAnimator.ofFloat(imgPizza, "rotation",vR[0], vR[1]).setDuration(2500)
        val scaleX = ObjectAnimator.ofFloat(imgPizza, "scaleX", vS[0], vS[1]).setDuration(2500)
        val scaleY = ObjectAnimator.ofFloat(imgPizza, "scaleY", vS[0], vS[1]).setDuration(2500)
        val animatorSet = AnimatorSet()
        animatorSet.play(rotation).with(scaleX).with(scaleY)
        animatorSet.addListener(
            object: Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {imgPizza.isClickable = false}
                override fun onAnimationEnd(p0: Animator) {imgPizza.isClickable = true }
                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
            }
        )
        return animatorSet
    }
}

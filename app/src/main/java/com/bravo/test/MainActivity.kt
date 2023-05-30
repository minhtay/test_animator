package com.bravo.test

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewbinding.ViewBinding
import com.bravo.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var isTransitioned = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var customView: View
    private var customViewHeight = 0

    private lateinit var viewToShrink: View
    private var isExpanded = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewToShrink = binding.viewToShrink
        viewToShrink.setOnClickListener {
            performTransition()
          /*  shrinkView()
            expandCustomView()*/
        }
        customView =
            LayoutInflater.from(this).inflate(R.layout.custom, binding.parentLayout, false)

        binding.parentLayout.addView(customView)
        customView.measure(
            View.MeasureSpec.makeMeasureSpec(binding.parentLayout.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        customViewHeight = customView.measuredHeight

        // Set the initial state of the custom view
        val layoutParams = customView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = 0
        customView.layoutParams = layoutParams
        // Trigger the shrinking animation
    }

    private fun expandCustomView() {
        if (isExpanded) return

        val animator = ValueAnimator.ofInt(0, binding.rootView.height - viewToShrink.height)
        animator.duration = 500

        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = customView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.height = value
            customView.layoutParams = layoutParams
        }

        animator.start()
        isExpanded = true
    }

    private fun performTransition() {
        // Calculate the final position for the transition
        val finalY = binding.rootView.top - viewToShrink.top

        // Create an ObjectAnimator to animate the view's translationY property
        val animator = ObjectAnimator.ofFloat(viewToShrink, "translationY", finalY.toFloat())

        // Set the animation duration
        animator.duration = 500 // milliseconds

        // Start the animation
        animator.start()
    }

    private fun shrinkView() {
        val initialWidth = viewToShrink.width
        val targetWidth = 700  // The desired final width of the view

        val animator = ValueAnimator.ofInt(initialWidth, targetWidth)
        animator.duration = 500 // Animation duration in milliseconds

        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int

            val layoutParams = viewToShrink.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = value

            // Set the horizontal bias to 0.5f to keep the view centered
            layoutParams.horizontalBias = 0.5f

            viewToShrink.layoutParams = layoutParams
        }

        animator.start()
    }
}

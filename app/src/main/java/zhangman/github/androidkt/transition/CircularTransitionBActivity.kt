package zhangman.github.androidkt.transition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_b_circular_transition.*
import zhangman.github.androidkt.R

/**
 * Created by admin on 2020/6/29 09:54.
 * Email: zhangman523@126.com
 */
class CircularTransitionBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_circular_transition)
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            container.visibility = View.INVISIBLE
            var revealX = intent.getIntExtra("revealX", 0)
            var revealY = intent.getIntExtra("revealY", 0)
            var viewTreeObserver = container.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener {
                    revealActivity(revealX, revealY)
                    container.viewTreeObserver.removeOnGlobalLayoutListener { this }
                }
            }
        } else {
            container.visibility = View.VISIBLE
        }
    }

    private fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var finalRadius = container.width.coerceAtLeast(container.height).toFloat() * 1.1f
            var circularReveal =
                ViewAnimationUtils.createCircularReveal(container, x, y, 25f, finalRadius)
            circularReveal.duration = 400
            circularReveal.interpolator = AccelerateInterpolator()
            container.visibility = View.VISIBLE
            circularReveal.start()
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var revealX = intent.getIntExtra("revealX", 0)
            var revealY = intent.getIntExtra("revealY", 0)
            var finalRadius = container.width.coerceAtLeast(container.height).toFloat() * 1.1f
            var circularReveal =
                ViewAnimationUtils.createCircularReveal(
                    container,
                    revealX,
                    revealY,
                    finalRadius,
                    25f
                )
            circularReveal.duration = 400
            circularReveal.interpolator = AccelerateInterpolator()
            circularReveal.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    container.visibility = View.INVISIBLE
                    finish()
                }
            })
            circularReveal.start()
        } else {
            super.onBackPressed()
        }
    }
}
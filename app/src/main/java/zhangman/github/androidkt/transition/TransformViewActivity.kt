package zhangman.github.androidkt.transition

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.activity_transform_view.*
import zhangman.github.androidkt.R

class TransformViewActivity : AppCompatActivity() {

    lateinit var startView: View
    lateinit var endView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform_view)
        addTransitionableTarget(start_fab)
        addTransitionableTarget(contact_card)
    }

    private fun addTransitionableTarget(target: View) {
        ViewCompat.setTransitionName(target, target.id.toString())

        if (target.id == R.id.contact_card) {
            target.setOnClickListener {
                showStartView(it)
            }
        } else {
            target.setOnClickListener {
                showEndView(it)
            }
        }
    }

    private fun showStartView(endView: View) {
        startView = start_fab

        // Construct a container transform transition between two views.

        // Construct a container transform transition between two views.
        val transition =
            buildContainerTransform(false)
        transition.startView = endView
        transition.endView = startView

        // Add a single target to stop the container transform from running on both the start
        // and end view.

        // Add a single target to stop the container transform from running on both the start
        // and end view.
        transition.addTarget(startView)

        // Trigger the container transform transition.

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(root_view, transition)
        startView.visibility = View.VISIBLE
        endView.visibility = View.INVISIBLE
    }

    private fun showEndView(startView: View) {
        this.endView = contact_card
        // Construct a container transform transition between two views.

        // Construct a container transform transition between two views.
        val transition: MaterialContainerTransform =
            buildContainerTransform(true)
        transition.startView = startView
        transition.endView = endView

        transition.addTarget(endView)

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(root_view, transition)
        startView.visibility = View.INVISIBLE
        endView.visibility = View.VISIBLE
    }

    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        val transform =
            MaterialContainerTransform()
        transform.scrimColor = Color.TRANSPARENT
        transform.drawingViewId = root_view.getId()
        val duration: Long = 1L
        transform.duration = duration
        transform.interpolator = FastOutSlowInInterpolator()
//        if (isArcMotionEnabled()) {
//            transform.setPathMotion(MaterialArcMotion())
//        }
        transform.fadeMode =
            MaterialContainerTransform.FADE_MODE_IN
        transform.isDrawDebugEnabled = false
        return transform
    }
}
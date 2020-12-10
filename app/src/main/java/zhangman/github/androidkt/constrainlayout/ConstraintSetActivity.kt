package zhangman.github.androidkt.constrainlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_constraintset.*
import zhangman.github.androidkt.R

class ConstraintSetActivity : AppCompatActivity() {
    var change = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraintset)

        view_1.setOnClickListener {
            switchViews()
        }
        view_2.setOnClickListener {
            switchViews()
        }


    }

    private fun switchViews() {
        var constraintSet = ConstraintSet()
        constraintSet.clone(root_view)
        constraintSet.clear(R.id.view_1, ConstraintSet.START)
        constraintSet.clear(R.id.view_1, ConstraintSet.END)
        constraintSet.clear(R.id.view_1, ConstraintSet.TOP)
        constraintSet.clear(R.id.view_1, ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.view_2, ConstraintSet.START)
        constraintSet.clear(R.id.view_2, ConstraintSet.END)
        constraintSet.clear(R.id.view_2, ConstraintSet.TOP)
        constraintSet.clear(R.id.view_2, ConstraintSet.BOTTOM)
        if (change) {
            constraintSet.apply {
                connect(
                    R.id.view_1,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                connect(
                    R.id.view_1,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                connect(
                    R.id.view_2,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )
                connect(
                    R.id.view_2,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                applyTo(root_view)
            }
        } else {
            constraintSet.apply {
                connect(
                    R.id.view_2,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                connect(
                    R.id.view_2,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                connect(
                    R.id.view_1,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )
                connect(
                    R.id.view_1,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                applyTo(root_view)
            }
        }
        change = !change

    }

}
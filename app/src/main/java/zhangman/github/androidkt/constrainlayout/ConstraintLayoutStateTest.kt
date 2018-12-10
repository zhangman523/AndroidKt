package zhangman.github.androidkt.constrainlayout

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import kotlinx.android.synthetic.main.activity_constraint_layout_states_start.*
import zhangman.github.androidkt.R


class ConstraintLayoutStateTest : AppCompatActivity() {
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout_states_start)
        stateConstraintLayout.loadLayoutDescription(R.xml.constraintlayout_states_exmaple)
        var change = false

        button.setOnClickListener {
            stateConstraintLayout.setState(R.id.loading, 0, 0)
            HandlerCompat.postDelayed(handler, {
                stateConstraintLayout.setState(if (change) R.id.start else R.id.end, 0, 0)
                change = !change
            }, null, 3000)
        }
    }
}
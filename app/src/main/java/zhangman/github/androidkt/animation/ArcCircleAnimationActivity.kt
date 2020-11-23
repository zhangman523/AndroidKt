package zhangman.github.androidkt.animation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import zhangman.github.androidkt.R

/**
 * Created by admin on 2020/11/23 15:36.
 * Email: zhangman523@126.com
 */
class ArcCircleAnimationActivity : AppCompatActivity() {
    lateinit var arcCircleView: ArcCircleView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arc_cricle_animation)
        arcCircleView = findViewById(R.id.arcCircleView);
    }

    fun startAnimation(view: View?) {
        arcCircleView.startAnimation()
    }
}
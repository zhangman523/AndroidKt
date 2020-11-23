package zhangman.github.androidkt.transition

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_circular_transition.*
import zhangman.github.androidkt.R

/**
 * Created by admin on 2020/6/29 09:54.
 * Email: zhangman523@126.com
 */
class CircularTransitionAActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular_transition)
        image_button.setOnClickListener(View.OnClickListener {
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, it, "transition")
            var revealX = (it.x + it.width / 2).toInt()
            var revealY = (it.y + it.height / 2).toInt()
            var intent = Intent(this, CircularTransitionBActivity::class.java)
            intent.putExtra("revealX", revealX)
            intent.putExtra("revealY", revealY)
            ActivityCompat.startActivity(this, intent, options.toBundle())
        })
    }
}
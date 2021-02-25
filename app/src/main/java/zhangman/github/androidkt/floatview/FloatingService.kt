package zhangman.github.androidkt.floatview

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.RequiresApi

class FloatingService : Service(), View.OnTouchListener {

    lateinit var layoutParams: WindowManager.LayoutParams
    lateinit var windowManager: WindowManager
    lateinit var button: Button
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra("close", false) == false) {
            showFloatingWindow()
        } else {
            hideFloatingWindow()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                windowManager =
                    getSystemService(Context.WINDOW_SERVICE) as WindowManager
                button = Button(application)
                button.text = "Floating window"
                button.setBackgroundColor(Color.BLUE)
                button.setOnTouchListener(this)
                layoutParams = WindowManager.LayoutParams()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
                }
                layoutParams.format = PixelFormat.RGBA_8888
                layoutParams.width = 500
                layoutParams.height = 200
                layoutParams.x = 300
                layoutParams.y = 300
                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                windowManager.addView(button, layoutParams)
            }
        }
    }

    private fun hideFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (windowManager != null) {
                windowManager.removeView(button)
            }
        }
    }

    private var x: Int = 0
    private var y: Int = 0
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.rawX.toInt()
                y = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = event.rawX.toInt()
                val nowY = event.rawY.toInt()

                val moveX = nowX - x
                val moveY = nowY - y
                x = nowX
                y = nowY
                layoutParams.x = layoutParams.x + moveX
                layoutParams.y = layoutParams.y + moveY
                windowManager.updateViewLayout(v, layoutParams)
            }

        }
        return false
    }
}
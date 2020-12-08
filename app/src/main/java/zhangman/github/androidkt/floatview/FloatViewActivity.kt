package zhangman.github.androidkt.floatview

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_float_view.*
import zhangman.github.androidkt.R

class FloatViewActivity : AppCompatActivity() {

    companion object {
        const val RC_OVERLAY_PERMISSON = 0x12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_float_view)

        show_button.setOnClickListener {
            // Overlay permission is only required for Marshmallow (API 23) and above. In previous APIs this permission is provided by default.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    AlertDialog.Builder(this).apply {
                        setTitle("PERMISSION DENY")
                        setMessage("Need System Alert Window Permission")
                        setPositiveButton("grand") { dialog, which ->
                            dialog.dismiss()
                            startActivityForResult(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + packageName)
                                ), RC_OVERLAY_PERMISSON
                            )
                        }
                        setNegativeButton("deny") { dialog, which ->
                            dialog.dismiss()
                        }
                    }.show()
                } else {
                    startService(Intent(this, FloatingService::class.java))
                }
            } else {
                startService(Intent(this, FloatingService::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_OVERLAY_PERMISSON) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, " permission deny!", Toast.LENGTH_SHORT).show()
                    startService(Intent(this, FloatingService::class.java))
                } else {
                    Toast.makeText(this, "permission grand!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
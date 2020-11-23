package zhangman.github.androidkt.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_notification_o_test.*
import zhangman.github.androidkt.R

/**
 * Created by admin on 2020/6/19 10:11.
 * Email: zhangman523@126.com
 */
class NotificationOTest : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_o_test)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channelId = "chat"
            var channelName = "chat messages"
            var importance = NotificationManagerCompat.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)

            channelId = "subscribe"
            channelName = "subscribe messages"
            importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance)
        }
        chat_messages.setOnClickListener {
            //send chat messages
            var notificationManager = NotificationManagerCompat.from(this);
            var notification =
                NotificationCompat.Builder(this, "chat")
                    .setContentTitle("received a new chat message")
                    .setContentText("How are you?").setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setAutoCancel(true).build()
            notificationManager.notify(1, notification)
        }
        subscribe_messages.setOnClickListener {
            //send subscribe message
            var notificationManager = NotificationManagerCompat.from(this);
            var notification =
                NotificationCompat.Builder(this, "chat")
                    .setContentTitle("received a new subscribe message")
                    .setContentText("How are you?").setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setAutoCancel(true).build()
            notificationManager.notify(2, notification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        var channel = NotificationChannel(channelId, channelName, importance)
        var notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
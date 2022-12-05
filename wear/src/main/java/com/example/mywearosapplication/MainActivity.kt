package com.example.mywearosapplication

import android.R
import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mywearosapplication.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            Toast.makeText(this, "Confirmado", Toast.LENGTH_SHORT).show()
            binding.txtTitle.text = "Confirmado"
            sendNotification()
        }

        binding.btnList.setOnClickListener {
            startActivity(Intent(this, RecycleActivity::class.java))
        }

        binding.btnClose.setOnClickListener {
            Toast.makeText(this, "Denegado", Toast.LENGTH_SHORT).show()
            binding.txtTitle.text = "Denegado"
        }
    }

    private fun sendNotification() {
        val random = Random.nextInt(0,100)
        val group_key = "group_messages"
        val group_channel = "12345"
        val message = "This is an example with random number: $random"
        val notification_id = random
        val notificationManager = NotificationManagerCompat.from(this)

        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel =
            NotificationChannel(group_channel, group_key, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, group_channel)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentTitle(title)
            .setContentText(message)

        val resultIntent = Intent(this, MainActivity::class.java)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)
        notificationManager.notify(notification_id, builder.build())
    }
}
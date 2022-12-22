package com.example.mywearosapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlin.random.Random

const val VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription"

class MainActivity : AppCompatActivity() {

    private var transcriptionNodeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.btn_notify)
        btn.setOnClickListener {
            sendNotification()
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
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
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
        requestTranscription(null)
    }

    private fun requestTranscription(voiceData: ByteArray?) {
        transcriptionNodeId?.also { nodeId ->
            val sendTask: Task<*> = Wearable.getMessageClient(this).sendMessage(
                nodeId,
                VOICE_TRANSCRIPTION_MESSAGE_PATH,
                voiceData
            ).apply {
                addOnSuccessListener {
                    Toast.makeText(this@MainActivity,"Success", Toast.LENGTH_SHORT).show()
                }
                addOnFailureListener {
                    Toast.makeText(this@MainActivity,"Fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTranscriptionCapability(capabilityInfo: CapabilityInfo) {
        transcriptionNodeId = pickBestNodeId(capabilityInfo.nodes)
    }

    private fun pickBestNodeId(nodes: Set<Node>): String? {
        // Find a nearby node or pick one arbitrarily
        return nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
    }
}
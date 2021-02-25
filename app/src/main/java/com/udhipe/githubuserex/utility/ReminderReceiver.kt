package com.udhipe.githubuserex.utility

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.userlist.UserListActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "message"

        private const val ID_REMINDER = 100
        private const val TIME_FORMAT = "HH:mm"

    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "-"
        val notifId = ID_REMINDER

        showNotificationReminder(context, "Github Explorer", message, notifId)
    }

    private fun showNotificationReminder(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val channelId = "reminder_1"
        val channelName = "reminder channel"

        // Create an Intent for the activity you want to start
        val resultIntent = Intent(context, UserListActivity::class.java)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntent(resultIntent)
//            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(notifSound)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true).setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId("reminder_1")

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = ID_REMINDER
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Reminder dibatalkan", Toast.LENGTH_SHORT).show()
    }

    fun setReminder(context: Context, time: String, message: String) {

        // Validasi inputan waktu terlebih dahulu
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Reminder berhasil diset", Toast.LENGTH_SHORT).show()
    }

    // Metode ini digunakan untuk validasi date dan time
    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}
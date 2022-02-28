package com.ginzburgworks.filmfinder.view.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ginzburgworks.filmfinder.R
import com.ginzburgworks.filmfinder.view.MainActivity
import com.ginzburgworks.local_module.Film
import com.ginzburgworks.remote_module.entity.ApiConstants

private const val CONTENT_TITLE = "Не забудьте посмотреть!"
private const val IMG_SIZE = "w500"
const val NOTIFICATION_WATCH_LATER_NAME = "watchLaterNotification"

object NotificationHelper {
    fun createNotification(context: Context, film: Film) {
        val mIntent = Intent(context, MainActivity::class.java)
        mIntent.putExtra(NOTIFICATION_WATCH_LATER_NAME, film)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_watch_later_24)
            setContentTitle(CONTENT_TITLE)
            setContentText(film.title)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        Glide.with(context).asBitmap().load(ApiConstants.IMAGES_URL + IMG_SIZE + film.poster)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    notificationManager.notify(film.id, builder.build())
                }
            })
        notificationManager.notify(film.id, builder.build())
    }
}
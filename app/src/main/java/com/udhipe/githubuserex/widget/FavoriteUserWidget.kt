package com.udhipe.githubuserex.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.udhipe.githubuserex.R

/**
 * Implementation of App Widget functionality.
 */
class FavoriteUserWidget : AppWidgetProvider() {

    companion object {
        const val EXTRA_ITEM = "com.udhipe.githubuserex.widget.EXTRA_ITEM"
        const val TOAST_ACTION = "com.udhipe.githubuserex.widget.TOAST_ACTION"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val intent = Intent(context, FavoriteUserService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

    // Construct the RemoteViews object
    val remoteViews = RemoteViews(context.packageName, R.layout.favorite_user_widget)
    remoteViews.setRemoteAdapter(R.id.stack_favorite_user, intent)
    remoteViews.setEmptyView(R.id.stack_favorite_user, R.id.tv_empty)

    val toastIntent = Intent(context, FavoriteUserWidget::class.java)
    toastIntent.action = FavoriteUserWidget.TOAST_ACTION
    toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
    val toastPendingIntent =
        PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    remoteViews.setPendingIntentTemplate(R.id.stack_favorite_user, toastPendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
}

private const val REMOTE_VIEW_COUNT: Int = 10

internal class FavoriteUserRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()

    private lateinit var widgetItems: List<FavoriteUserWidget>

    override fun onCreate() {
        widgetItems = List(REMOTE_VIEW_COUNT) { index -> FavoriteUserWidget() }
    }

    override fun onDataSetChanged() {
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.star_wars_logo))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.darth_vader))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.falcon))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.storm_trooper))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.starwars))

//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_alarm))
//        mWidgetItems.add(
//            BitmapFactory.decodeResource(
//                mContext.resources,
//                R.drawable.ic_favorite_red
//            )
//        )
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_favorite))
//        mWidgetItems.add(
//            BitmapFactory.decodeResource(
//                mContext.resources,
//                R.drawable.ic_app_launcher_foreground
//            )
//        )
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_language))
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(mContext.packageName, R.layout.item_favorite_user)
        remoteView.setImageViewBitmap(R.id.img_favorite_user, mWidgetItems[position])

        val extras = bundleOf(
            FavoriteUserWidget.EXTRA_ITEM to position
        )

        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        remoteView.setOnClickFillInIntent(R.id.img_favorite_user, fillIntent)

        return remoteView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}
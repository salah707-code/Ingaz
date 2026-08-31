package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.EnjazApplication
import com.example.MainActivity
import com.example.R
import com.example.ui.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EnjazAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_enjaz_tasks)

            // Intent to open Main App
            val mainIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val mainPendingIntent = PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, mainPendingIntent)
            views.setOnClickPendingIntent(R.id.widget_btn_add, mainPendingIntent)

            // Query pending tasks count from Room database asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val app = context.applicationContext as? EnjazApplication
                    if (app != null) {
                        val taskList = app.taskRepository.allTasks.firstOrNull() ?: emptyList()
                        val todayStart = DateTimeUtils.getTodayStartMillis()
                        val todayEnd = DateTimeUtils.getTodayEndMillis()
                        val pendingToday = taskList.count { !it.isCompleted && it.date in todayStart..todayEnd }

                        views.setTextViewText(
                            R.id.widget_tasks_count,
                            "مهام اليوم: $pendingToday متبقية"
                        )
                        views.setTextViewText(
                            R.id.widget_status_desc,
                            if (pendingToday == 0) "لا توجد مهام متبقية لليوم 🎉" else "اضغط هنا لإنجاز ومتابعة مهامك"
                        )
                    }
                } catch (ignored: Exception) {}

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        fun sendUpdateBroadcast(context: Context) {
            val intent = Intent(context, EnjazAppWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                ComponentName(context, EnjazAppWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(intent)
        }
    }
}

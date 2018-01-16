package com.surfacesoft.yaj.satransferui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Created by Yulio on 15/08/2015.
 */
public class WidgetSaldo extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //  Iteramos la lista de widgets en ejecución
        for (int i = 0; i < appWidgetIds.length; i++) {
            //  ID del widget actual
            int widgetId = appWidgetIds[i];
            //  ejecutamos el widget actual
            executeUSSD(context, appWidgetManager, widgetId);
        }
    }

    public void executeUSSD(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        //Obtenemos la lista de controles del widget actual
        RemoteViews controles = new RemoteViews(context.getPackageName(), R.layout.widget_saldo);

        String encodedNumSimbol = Uri.encode("#");
        String ussd = "*" + "222" + encodedNumSimbol;

        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.preview_saldo, pendingIntent);

        //  Notificamos al manager de la actualización del widget actual
        appWidgetManager.updateAppWidget(widgetId, controles);
    }

}

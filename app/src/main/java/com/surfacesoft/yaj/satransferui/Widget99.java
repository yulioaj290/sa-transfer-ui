package com.surfacesoft.yaj.satransferui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Created by Yulio on 15/08/2015.
 */
public class Widget99 extends AppWidgetProvider {
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

//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//        PendingIntent pendInt = PendingIntent.getActivity(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        controles.setOnClickPendingIntent(R.id.preview_99, pendInt);
//        (new MainActivity()).Service_99(new View(context));


        //  Notificamos al manager de la actualización del widget actual
        appWidgetManager.updateAppWidget(widgetId, controles);
    }
}

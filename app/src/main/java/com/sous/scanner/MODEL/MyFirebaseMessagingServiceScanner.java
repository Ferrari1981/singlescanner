package com.sous.scanner.MODEL;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

public class MyFirebaseMessagingServiceScanner extends FirebaseMessagingService {
    private     Long version=0l;
    // TODO: 02.12.2021
    public MyFirebaseMessagingServiceScanner() {
        super();
        try{
        Log.w(this.getClass().getName(), " MyFirebaseMessagingServiceScanner " );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки=new ContentValues();
            valuesЗаписываемОшибки.put("Error",e.toString().toLowerCase());
            valuesЗаписываемОшибки.put("Klass",this.getClass().getName());
            valuesЗаписываемОшибки.put("Metod",Thread.currentThread().getStackTrace()[2].getMethodName());
            valuesЗаписываемОшибки.put("LineError",   Thread.currentThread().getStackTrace()[2].getLineNumber());
            final Object ТекущаяВерсияПрограммы = version;
            Integer   ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
            valuesЗаписываемОшибки.put("whose_error",ЛокальнаяВерсияПОСравнение);
            new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }

   @Override
    public void handleIntent(@NonNull Intent intent) {
       super.handleIntent(intent);
       Log.d(this.getClass().getName(), " handleIntent ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ  С САЙТА ONESIGNAL !!!!!!!!!!!!  " +
               " MyFirebaseMessagingService  protected Intent public void handleIntent");
    }
    @NonNull
    @Override
    protected Intent getStartCommandIntent(@NonNull Intent intent) {
        Log.d(this.getClass().getName(), " getStartCommandIntent ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ  С САЙТА ONESIGNAL !!!!!!!!!!!!  " +
                " MyFirebaseMessagingService  protected Intent getStartCommandIntent");
         return super.getStartCommandIntent(intent);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
      super.onMessageReceived(remoteMessage);
        try{
        Log.d(this.getClass().getName(), " onMessageReceived ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ SCANNER  С САЙТА ONESIGNAL !!!!!!!!!!!!  " +
                " MyFirebaseMessagingService   metod onNewToken "+remoteMessage.getMessageId()+"\n"+
              "  remoteMessage.getMessageType() "+  remoteMessage.getMessageType()+"\n"+
                "  remoteMessage.getRawData() "+remoteMessage.getRawData()+"\n"+
                 " remoteMessage.getSenderId()  " +remoteMessage.getSenderId());
        // TODO: 02.12.2021  ПРИШЛИ ДАННЫЕ ОТ FIREBASE CLOUD  НОВО СООБЕЩЕНИ И ИХ
            //  АНАЛИЗИРУЕМ И ЕСЛИ СООБЩЕНИЕ НЕ ПУСТОЕ МЫ УДАЛЯЕМ НОВТИВИКАЦИО ВИСЕВШЕЕ И СОЩЗДАЕНМ НВОЕ ЕЛСИ НЕСТЬ СООБЩЕНИ ТЕСТ СООБЩЕНИЯ
     Map<String, String> Уведоелмение= remoteMessage.getData();
        for (Map.Entry<String, String> entry: Уведоелмение.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(this.getClass().getName(), " key "+key+"\n"+
                    " value "+value+"\n" );
            Log.d(this.getClass().getName(),  "ПРИШЛО СООБЩЕНИЕ ОТ FIREBASE CLOUD И СМОТРИМ СОДЕРЖИМОЕ "+"\n"
                    +value+"\n"+ " key " +key+"\n"+
                    " entry.getValue() " +entry.getValue());
            // TODO: 21.12.2022  запускас ONESINGLE FIREBASE
         if (entry.getValue().matches("(.*)scanner(.*)")) {
             Log.d(this.getClass().getName(),  "ПРИШЛО СООБЩЕНИЕ ОТ FIREBASE CLOUD И СМОТРИМ СОДЕРЖИМОЕ "+"\n"
                     +value+"\n"+ " key " +key+"\n"+
                     " entry.getValue() " +entry.getValue());
                    break;
           }
        }
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = pInfo.getLongVersionCode();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки=new ContentValues();
            valuesЗаписываемОшибки.put("Error",e.toString().toLowerCase());
            valuesЗаписываемОшибки.put("Klass",this.getClass().getName());
            valuesЗаписываемОшибки.put("Metod",Thread.currentThread().getStackTrace()[2].getMethodName());
            valuesЗаписываемОшибки.put("LineError",   Thread.currentThread().getStackTrace()[2].getLineNumber());
            final Object ТекущаяВерсияПрограммы = version;
            Integer   ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
            valuesЗаписываемОшибки.put("whose_error",ЛокальнаяВерсияПОСравнение);
            new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        ////
        Log.d(this.getClass().getName(), " onDeletedMessages ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ  С САЙТА ONESIGNAL !!!!!!!!!!!!   MyFirebaseMessagingService   metod onNewToken " );
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);

        try{
        /////

/*        String КлючДляFirebaseNotification="2a1819db-60c8-4ca3-a752-1b6cd9cadfa1";

   FirebaseMessaging.getInstance().subscribeToTopic("adroid");

            RemoteMessage rm = new RemoteMessage.Builder(КлючДляFirebaseNotification)
                    .setMessageId("myApp_" + + System.currentTimeMillis())
                    .addData("action", "chat")
                    .addData("destinataire", "ee")
                    .addData("emetteur","ee")
                    .setTtl(0)
                    .build();
            FirebaseMessaging.getInstance().send(rm);*/
        ////
        Log.d(this.getClass().getName(), " onMessageSent ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ  С САЙТА ONESIGNAL !!!!!!!!!!!!   MyFirebaseMessagingService   metod onNewToken "+s.toString() );

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки=new ContentValues();
            valuesЗаписываемОшибки.put("Error",e.toString().toLowerCase());
            valuesЗаписываемОшибки.put("Klass",this.getClass().getName());
            valuesЗаписываемОшибки.put("Metod",Thread.currentThread().getStackTrace()[2].getMethodName());
            valuesЗаписываемОшибки.put("LineError",   Thread.currentThread().getStackTrace()[2].getLineNumber());
            final Object ТекущаяВерсияПрограммы = version;
            Integer   ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
            valuesЗаписываемОшибки.put("whose_error",ЛокальнаяВерсияПОСравнение);
            new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }


    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
        Log.e(this.getClass().getName(), " onSendError  ОШИБКА  ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ  С САЙТА ONESIGNAL " +
                "!!!!!!!!!!!!  MyFirebaseMessagingService   metod onNewToken e  "+e.toString()+"\n"+
                " s " +s.toString());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(this.getClass().getName(), " onNewToken  ПРИШЛО СООБЩЕНИЕ УВЕДОМЛЕНИЯ  С САЙТА ONESIGNAL !!!!!!!!!!!!   MyFirebaseMessagingService   metod onNewToken "+s.toString());

    }


}

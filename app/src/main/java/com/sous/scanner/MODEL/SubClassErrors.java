package com.sous.scanner.MODEL;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Optional;

public class SubClassErrors {
    Context context;
    public SubClassErrors(@NonNull Context context) {
        this.context =context;
    }
    public  void МетодЗаписиОшибок(@NonNull   ContentValues contentValuesДляЗаписиОшибки) {
        try {
            Log.i( context.getClass().getName(), "contentValuesДляЗаписиОшибки  " + contentValuesДляЗаписиОшибки);
            Uri uri = Uri.parse("content://com.sous.scanner.providerscanner/" +"errordsu1" + "");
       //     Uri uri = Uri.parse("content://dsu1.scanner.myapplication.contentproviderfordatabasescanner/" +"errordsu1" + "");
            ContentResolver resolver = context.getContentResolver();
        Uri    insertData=   resolver.insert(uri, contentValuesДляЗаписиОшибки);
        Integer РезультатВставки= Optional.ofNullable(insertData.toString().replaceAll("content://","")).map(Integer::new).orElse(0);
            Log.w(context.getClass().getName(), " РЕЗУЛЬТАТ insertData  ВСТАВКИ ЗНАЧЕНИЯ  " +  insertData.toString()+ " РезультатВставки " +РезультатВставки );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e( context.getClass().getName(), "SubClassErrors ДЛЯ SCANNER error " + e +
                    " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber()+ " date " +new Date().toGMTString());
        }

    }

}

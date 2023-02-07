package com.sous.scanner.MODEL;


import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



import org.jetbrains.annotations.NotNull;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;


//этот класс создает базу данных SQLite
public class CREATE_DATABASEScanner extends SQLiteOpenHelper{ ///SQLiteOpenHelper
    static final int VERSION =      18;//ПРИ ЛЮБОМ ИЗМЕНЕНИЕ В СТРУКТУРЕ БАЗЫ ДАННЫХ НУЖНО ДОБАВИТЬ ПЛЮС ОДНУ ЦИФРУ К ВЕРСИИ 1=1+1=2 ИТД.1
    private   Context context;
    private      SQLiteDatabase ССылкаНаСозданнуюБазу;
    private     CopyOnWriteArrayList<String> ИменаТаблицыОтАндройда;
    private     Long version=0l;


    public SQLiteDatabase getССылкаНаСозданнуюБазу() {
        Log.d(this.getClass().getName()," get () БАЗА  ДАННЫХ   ДСУ-1 ОТКРЫТА ССылкаНаСозданнуюБазу.isOpen()  " +ССылкаНаСозданнуюБазу);
        return ССылкаНаСозданнуюБазу;
    }
    ///////КОНСТРУКТОР главного класса по созданию базы данных
    public CREATE_DATABASEScanner(@NotNull Context context) {/////КОНСТРУКТОР КЛАССА ПО СОЗДАНИЮ БАЗЫ ДАННЫХ
        super(context, "DatabaseScanner.db", null, VERSION ); // определяем имя базы данных  и ее версию
        try{
            this.context =context;
            if (ССылкаНаСозданнуюБазу==null  ) {
                ССылкаНаСозданнуюБазу = this.getWritableDatabase(); //ссылка на схему базы данных;//ссылка на схему базы данных ГЛАВНАЯ ВСТАВКА НА БАЗУ ДСУ-1
                Log.d(this.getClass().getName()," БАЗА  ДАННЫХ   ДСУ-1 ОТКРЫВАЕМ  ССылкаНаСозданнуюБазу==null   "
                        +ССылкаНаСозданнуюБазу.isOpen());
            }else{
                //TODO connection  else is onen false
                if (!ССылкаНаСозданнуюБазу.isOpen()) {
                    ССылкаНаСозданнуюБазу = this.getWritableDatabase(); //ссылка на схему базы данных;//ссылка на схему базы данных ГЛАВНАЯ ВСТАВКА НА БАЗУ ДСУ-1
                    Log.d(this.getClass().getName()," БАЗА  ДАННЫХ   ДСУ-1 ОТКРЫВАЕМ  ССылкаНаСозданнуюБазу.isOpen()  "
                            +ССылкаНаСозданнуюБазу.isOpen());
                }
            }
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
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
            new SubClassErrors(context).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }
    //  Cоздание ТАблиц
    @Override
    public void onCreate(SQLiteDatabase ССылкаНаСозданнуюБазу) {
        try {
            Log.d(this.getClass().getName(), "сработала ... НАЧАЛО  СОЗДАНИЯ ТАЛИЦ ");
            // TODO: 24.10.2022 Генерируем Список Таблиц
            ИменаТаблицыОтАндройда=new CopyOnWriteArrayList<>();
            ИменаТаблицыОтАндройда.add("errordsu1");
            ИменаТаблицыОтАндройда.add("scannerandroid");
            ИменаТаблицыОтАндройда.add("scannerpublic");
            // TODO: 03.06.2022  создаение тригера
            МетодСозданиеТаблицДляСканирования(ССылкаНаСозданнуюБазу,ИменаТаблицыОтАндройда);
            Log.d(this.getClass().getName(), " сработала ... КОНЕЦ СОЗДАНИЯ ТАБЛИЦ ВИЮ ТРИГЕР " +new Date().toGMTString());
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
            new SubClassErrors(context).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase ССылкаНаСозданнуюБазу, int oldVersion, int newVersion) {
        try{
            Log.d(this.getClass().getName(), " после СЛУЖБА  содание базы newVersion==  652   (например)   " + new Date() + " newVersion " + newVersion);
            if (newVersion > oldVersion) {
                // TODO: 08.06.2021 создание Базы Данных
                onCreate(ССылкаНаСозданнуюБазу);
                Log.d(this.getClass().getName(), " СЛУЖБА  содание базы newVersion > oldVersion   " + new Date());
            }
            Log.i(this.getClass().getName(), "  Создана/Изменена База Данных !!! "+new Date());
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
            new SubClassErrors(context).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }
    @Override
    public void onDowngrade(SQLiteDatabase ССылкаНаСозданнуюБазу, int oldVersion, int newVersion) {
        onCreate(ССылкаНаСозданнуюБазу);
    }
    private void МетодСозданиеТаблицДляСканирования(@NotNull SQLiteDatabase ССылкаНаСозданнуюБазу,
                                                    @NotNull CopyOnWriteArrayList ИменаТаблицыОтАндройда) {//BEFORE   INSERT , UPDATE , DELETE
        try{
            // TODO: 06.12.2022 удаление старых таблиц
            ССылкаНаСозданнуюБазу.execSQL("drop table  if exists tablescannerandroid ");//ТАБЛИЦА ГЕНЕРАЦИИ ОШИБОК
            ССылкаНаСозданнуюБазу.execSQL("drop table  if exists tablescannerpublic ");//ТАБЛИЦА ГЕНЕРАЦИИ ОШИБОК
            ССылкаНаСозданнуюБазу.execSQL("drop table  if exists MODIFITATION_Client ");//ТАБЛИЦА ГЕНЕРАЦИИ ОШИБОК
            // TODO: 30.11.2022 создаени таблицы ошибок
            ССылкаНаСозданнуюБазу.execSQL("drop table  if exists errordsu1 ");//ТАБЛИЦА ГЕНЕРАЦИИ ОШИБОК
            ССылкаНаСозданнуюБазу.execSQL("Create table if not exists errordsu1 (" +
                    "ID_Table_ErrorDSU1 INTEGER PRIMARY KEY AUTOINCREMENT  ," +
                    " Error TEXT      ," +
                    "Klass TEXT  ," +
                    "Metod TEXT ," +
                    "LineError INTEGER ," +
                    "date_update NUMERIC  ,"+
                    "whose_error INTEGER  )");
            Log.d(this.getClass().getName(), " сработала ...  создание таблицы ErrorDSU1 ");
            ИменаТаблицыОтАндройда.forEach(new Consumer() {
                @Override
                public void accept(Object НазваниеТаблицыДляТригера) {
                    String ФиналНазваниеТаблицыДляЗаполения =
                            new StringBuffer().append("'").append(НазваниеТаблицыДляТригера).append("'").toString();
                    if (!НазваниеТаблицыДляТригера.equals("errordsu1")) {
                        ССылкаНаСозданнуюБазу.execSQL("drop table  if exists   "+НазваниеТаблицыДляТригера+"");//test
                        ССылкаНаСозданнуюБазу.execSQL("Create table if not exists   "+НазваниеТаблицыДляТригера+" (" +
                                "id  INTEGER     ," +
                                " namedevice TEXT ," +
                                " macdevice TEXT  ," +
                                " ipdevice  TEXT ," +
                                " control_in  TEXT ," +
                                " control_out  TEXT ," +
                                " date_update NUMERIC  ," +
                                " user_update INTEGER  ," +
                                " gps TEXT ," +
                                " uuid NUMERIC UNIQUE ,"+
                                " current_table NUMERIC UNIQUE )");
                        Log.d(this.getClass().getName(), " сработала ...  создание таблицы   НазваниеТаблицыДляТригера   "+НазваниеТаблицыДляТригера );
                    }
                    // TODO: 30.11.2022 Тригеры для Сканироваение
                    //TODO INSERT
                    ССылкаНаСозданнуюБазу.execSQL("  drop TRIGGER  if exists ScannerTableINSERT" + НазваниеТаблицыДляТригера + "");
                    //TODO INSERT
                    ССылкаНаСозданнуюБазу.execSQL(" CREATE TRIGGER IF NOT EXISTS ScannerTableINSERT" + НазваниеТаблицыДляТригера + "" +
                            "  AFTER INSERT   ON " + НазваниеТаблицыДляТригера +
                            " BEGIN " +
                            " UPDATE "+НазваниеТаблицыДляТригера+" SET  date_update= datetime() "
                            + "; " +
                            " END ;");//test
                    // TODO: 03.06.2022
                    Log.d(this.getClass().getName(), " сработала ... создание тригера MODIFITATION_Client   TODO INSERT ФиналНазваниеТаблицыДляЗаполения " + ФиналНазваниеТаблицыДляЗаполения);
                    //TODO UPDATE
                    ССылкаНаСозданнуюБазу.execSQL("  drop TRIGGER  if exists ScannerTableUpdate" + НазваниеТаблицыДляТригера + "");
                    //TODO INSERT
                    ССылкаНаСозданнуюБазу.execSQL(" CREATE TRIGGER IF NOT EXISTS ScannerTableUpdate" + НазваниеТаблицыДляТригера + "" +
                            "  AFTER UPDATE   ON " + НазваниеТаблицыДляТригера +
                            " BEGIN " +
                            " UPDATE "+НазваниеТаблицыДляТригера+" SET  date_update= datetime() "
                            + "; " +
                            " END ;");//test
                    Log.d(this.getClass().getName(), " сработала ... создание тригера MODIFITATION_Client   TODO UPDATE  ФиналНазваниеТаблицыДляЗаполения " + ФиналНазваниеТаблицыДляЗаполения);
                }
                // TODO: 22.11.2022 end all triger
            });
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
            new SubClassErrors(context).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }
}// todo конец класса для создание таблицы для Scanner






































































































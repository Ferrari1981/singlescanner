package com.sous.scanner.Services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.onesignal.OneSignal;
import com.sous.scanner.Database.CREATE_DATABASEScanner;
import com.sous.scanner.Firebase.MyFirebaseMessagingServiceScanner;
import com.sous.scanner.Errors.SubClassErrors;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServiceClientBLE extends IntentService {
    private SQLiteDatabase sqLiteDatabase;
    private CREATE_DATABASEScanner createDatabaseScanner;
    public LocalBinderСканнер binder = new LocalBinderСканнер();
    private Context context;
    private Activity activity;
    private String TAG;
    private Handler handler;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    public ServiceClientBLE() {
        super("ServiceClientBLE");
    }
    private   MutableLiveData<String> mediatorLiveDataGATT;
    private     Long version=0l;
    private  String ДействиеДляСервераGATTОТКлиента;
    private  UUID uuidКлючСервераGATTЧтениеЗапись;
    private  BluetoothGatt gatt;
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: 07.02.2023 клиент сервер
        try {
            Log.d(context.getClass().getName(), "\n"
                    + " время: " + new Date() + "\n+" +
                    " Класс в процессе... " + this.getClass().getName() + "\n" +
                    " метод в процессе... " + Thread.currentThread().getStackTrace()[2].getMethodName());
            this.createDatabaseScanner = new CREATE_DATABASEScanner(context);
            this.sqLiteDatabase = createDatabaseScanner.getССылкаНаСозданнуюБазу();
            TAG = getClass().getName().toString();
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.getLongVersionCode();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки = new ContentValues();
            valuesЗаписываемОшибки.put("Error", e.toString().toLowerCase());
            valuesЗаписываемОшибки.put("Klass", this.getClass().getName());
            valuesЗаписываемОшибки.put("Metod", Thread.currentThread().getStackTrace()[2].getMethodName());
            valuesЗаписываемОшибки.put("LineError", Thread.currentThread().getStackTrace()[2].getLineNumber());
            final Object ТекущаяВерсияПрограммы = version;
            Integer ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
            valuesЗаписываемОшибки.put("whose_error", ЛокальнаяВерсияПОСравнение);
            new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }

    }

    public class LocalBinderСканнер extends Binder {
        public ServiceClientBLE getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServiceClientBLE.this;
        }

        public void linkToDeath(DeathRecipient deathRecipient) {
            Log.i(this.getClass().getName(),  "deathRecipient " +Thread.currentThread().getStackTrace()[2].getMethodName()+ " время " +new Date().toLocaleString() );
            deathRecipient.binderDied();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(context.getClass().getName(), "\n"
                + " время: " + new Date() + "\n+" +
                " Класс в процессе... " + this.getClass().getName() + "\n" +
                " метод в процессе... " + Thread.currentThread().getStackTrace()[2].getMethodName());
        //   return super.onBind(intent);
        return binder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            if (rootIntent.getAction().equalsIgnoreCase("КлиентЗакрываетСлужбу")) {
                Log.d(context.getClass().getName(), "\n"
                        + " время: " + new Date() + "\n+" +
                        " Класс в процессе... " + this.getClass().getName() + "\n" +
                        " метод в процессе... " + Thread.currentThread().getStackTrace()[2].getMethodName());
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки = new ContentValues();
            valuesЗаписываемОшибки.put("Error", e.toString().toLowerCase());
            valuesЗаписываемОшибки.put("Klass", this.getClass().getName());
            valuesЗаписываемОшибки.put("Metod", Thread.currentThread().getStackTrace()[2].getMethodName());
            valuesЗаписываемОшибки.put("LineError", Thread.currentThread().getStackTrace()[2].getLineNumber());
            final Object ТекущаяВерсияПрограммы = version;
            Integer ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
            valuesЗаписываемОшибки.put("whose_error", ЛокальнаяВерсияПОСравнение);
            new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(context.getClass().getName(), "\n"
                + " время: " + new Date() + "\n+" +
                " Класс в процессе... " + this.getClass().getName() + "\n" +
                " метод в процессе... " + Thread.currentThread().getStackTrace()[2].getMethodName());
        // TODO: 13.02.2023  Разрываем сокедение с сервером
        МетодРазрываСоедениесGAttServer();
    }

    @SuppressLint("MissingPermission")
    private void МетодРазрываСоедениесGAttServer() {
        try {
            if (gatt!=null) {
                gatt.disconnect();
                gatt.close();
            }
            // TODO: 30.06.2022 сама не постредствено запуск метода
    } catch (Exception e) {
        e.printStackTrace();
        Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                + Thread.currentThread().getStackTrace()[2].getLineNumber());
        ContentValues valuesЗаписываемОшибки = new ContentValues();
        valuesЗаписываемОшибки.put("Error", e.toString().toLowerCase());
        valuesЗаписываемОшибки.put("Klass", this.getClass().getName());
        valuesЗаписываемОшибки.put("Metod", Thread.currentThread().getStackTrace()[2].getMethodName());
        valuesЗаписываемОшибки.put("LineError", Thread.currentThread().getStackTrace()[2].getLineNumber());
        final Object ТекущаяВерсияПрограммы = version;
        Integer ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
        valuesЗаписываемОшибки.put("whose_error", ЛокальнаяВерсияПОСравнение);
        new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
    }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(getApplicationContext().getClass().getName(), "\n"
                    + " время: " + new Date() + "\n+" +
                    " УДАЛЕНИЕ СТАТУСА Удаленная !!!!!" + "\n" +
                    " УДАЛЕНИЕ СТАТУСА Удаленная !!!!! " + "\n" +
                    " УДАЛЕНИЕ СТАТУСА Удаленная !!!!!   Класс в процессе... " + this.getClass().getName() + "\n" +
                    " метод в процессе... " + Thread.currentThread().getStackTrace()[2].getMethodName());
            this.context = getApplicationContext();
            // МетодЗапускаОбщиеКоды(getApplicationContext(),intent);
// TODO: 30.06.2022 сама не постредствено запуск метода
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки = new ContentValues();
            valuesЗаписываемОшибки.put("Error", e.toString().toLowerCase());
            valuesЗаписываемОшибки.put("Klass", this.getClass().getName());
            valuesЗаписываемОшибки.put("Metod", Thread.currentThread().getStackTrace()[2].getMethodName());
            valuesЗаписываемОшибки.put("LineError", Thread.currentThread().getStackTrace()[2].getLineNumber());
            final Object ТекущаяВерсияПрограммы = version;
            Integer ЛокальнаяВерсияПОСравнение = Integer.parseInt(ТекущаяВерсияПрограммы.toString());
            valuesЗаписываемОшибки.put("whose_error", ЛокальнаяВерсияПОСравнение);
            new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
        }

    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.d(newBase.getClass().getName(), "\n"
                + " время: " + new Date() + "\n+" +
                " Класс в процессе... " + newBase.getClass().getName() + "\n" +
                " метод в процессе... " + Thread.currentThread().getStackTrace()[2].getMethodName());
        this.context = newBase;
        super.attachBaseContext(newBase);
    }
    // TODO: 30.11.2022 сервер СКАНИРОВАНИЯ
    public  String МетодGattРезультатServerClient( @NonNull   BluetoothGattCharacteristic     characteristics) {
        String ОтветСамПингGATTServerClient = new String();
        try{
            Log.w(this.getClass().getName(), "  МетодGattРезультатServerClient characteristics  "+characteristics+ " ОтветСамПингGATTServerClient " +ОтветСамПингGATTServerClient);

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
        return  ОтветСамПингGATTServerClient;
    }
    // TODO: 30.11.2022 сервер СКАНИРОВАНИЯ
    public  void МетодКлиентЗапускСканера(@NonNull Handler handler, @NonNull Activity activity,
                                          @NonNull   BluetoothManager     bluetoothManager,
                                          @NonNull  MutableLiveData<String> mediatorLiveDatagatt,
                                          @NonNull String ДействиеДляСервераGATTОТКлиента ) {
        this.context = activity;
        this.activity = activity;
        this.handler = handler;
        this.bluetoothManager=bluetoothManager;
        this.mediatorLiveDataGATT=mediatorLiveDatagatt;
        this.ДействиеДляСервераGATTОТКлиента=ДействиеДляСервераGATTОТКлиента;
        // TODO: 08.12.2022 уснатавливаем настройки Bluetooth
        Log.w(this.getClass().getName(), "   bluetoothManager  "+bluetoothManager+ " bluetoothAdapter " +bluetoothAdapter + "mediatorLiveDataGATT " +mediatorLiveDataGATT);
        try{

            МетодЗапускаСканированиеКлиент();
            Log.w(this.getClass().getName(), "   МетодКлиент  characteristics  ");
            Log.w(this.getClass().getName(), "   МетодКлиент  characteristics  ");
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
    // TODO: 08.12.2022 Метод Сервер

    @SuppressLint("MissingPermission")
    private void  МетодЗапускаСканированиеКлиент(){
        try{
            Message message = Message.obtain(handler);
            Bundle bundle=new Bundle();
            bundle.putString("КакоеДейтвие","ВыключаемPrograssbar");
            message.setData(bundle);
            message.setAsynchronous(true);
            handler.sendMessageDelayed(message,1000);
            Log.d(TAG, "МетодЗапускаСканированиеКлиент: Запускаем.... Метод Сканирования Для Android");
            Log.d(TAG, "1МетодЗапускаСканиваронияДляАндройд: Запускаем.... Метод Сканирования Для Android binder.isBinderAlive()  "+"\n+" +
                    ""+binder.isBinderAlive()+ " date "+new Date().toString().toString()+"" +
                    "\n"+" POOL "+Thread.currentThread().getName() +
                    "\n" + " ALL POOLS  " +Thread.getAllStackTraces().entrySet().size());//
            // TODO: 08.12.2022 сканирование Bluetooth
            bluetoothAdapter = bluetoothManager.getAdapter();
            while (bluetoothAdapter.isEnabled()==false){
                bluetoothAdapter.enable();
            }

            // TODO: 12.02.2023 адреса разыне колиентов
            uuidКлючСервераGATTЧтениеЗапись =        ParcelUuid.fromString("20000000-0000-1000-8000-00805f9b34fb").getUuid();
            LinkedHashMap<String,UUID> BluetoothСерверов =new LinkedHashMap<>() ;///TODO  служебный xiaomi "BC:61:93:E6:F2:EB", МОЙ XIAOMI FC:19:99:79:D6:D4  //////      "BC:61:93:E6:E2:63","FF:19:99:79:D6:D4"
            UUID   UuidГлавныйКлючСерверGATT =        ParcelUuid.fromString("10000000-0000-1000-8000-00805f9b34fb").getUuid();
            // TODO: 11.02.2023 СПИСОК СЕРВЕРОВ
            BluetoothСерверов.put("BC:61:93:E6:F2:EB",UuidГлавныйКлючСерверGATT);
        //48:59:A4:5B:C1:F5  //  BC:61:93:E6:F2:EB   //  FC:19:99:79:D6:D4  XIAOMI 9A
          //  BluetoothСерверов.put("48:59:A4:5B:C1:F5",uuidКлючСервераZTE);//48:59:A4:5B:C1:F5  //  BC:61:93:E6:F2:EB   //  FC:19:99:79:D6:D4  XIAOMI 9A
          //  BluetoothСерверов.put("48:59:A4:5B:C1:F5",uuidКлючСервераZTE);//48:59:A4:5B:C1:F5  //  BC:61:93:E6:F2:EB   //  FC:19:99:79:D6:D4  XIAOMI 9A
            ///  Set<BluetoothDevice> bluetoothDevicesДополнительный = bluetoothAdapter.getBondedDevices();
            /*         BluetoothСерверов.offer("FC:19:99:79:D6:D4");//48:59:A4:5B:C1:F5  //  BC:61:93:E6:F2:EB   //  FC:19:99:79:D6:D4  XIAOMI 9NTC*/
            /// BluetoothСерверов.offer("48:59:A4:5B:C1:F5");//48:59:A4:5B:C1:F5  //  BC:61:93:E6:F2:EB   //  FC:19:99:79:D6:D4  ZTE
            //ExecutorService esМенеджерПотоковСканер=Executors.newFixedThreadPool(BluetoothСерверов.size());
            CompletionService esМенеджерПотоковСканер= new ExecutorCompletionService<>(Executors.newFixedThreadPool(BluetoothСерверов.size()) );
            Log.d(this.getClass().getName(), "\n" + " pairedDevices.size() " + BluetoothСерверов.size());
            BluetoothСерверов.forEach(new BiConsumer<String, UUID>() {
                @Override
                public void accept(String АдресаBluetoothСерверов, UUID UuidГлавныйКлючСерверGATT) {
                    try{
                    // TODO: 26.01.2023 начало сервера GATT
                        esМенеджерПотоковСканер.submit(()->{
                        BluetoothDevice bluetoothDevice=bluetoothAdapter.getRemoteDevice(АдресаBluetoothСерверов);
                        Log.d(this.getClass().getName()," bluetoothDevice " +bluetoothDevice  );
                            // TODO: 12.02.2023  запускаем задачу в потоке
                            МетодРаботыСТекущийСерверомGATT(bluetoothDevice, UuidГлавныйКлючСерверGATT);
                            Log.d(TAG, "  МетодЗапускаЦиклаСерверовGATT()....  UuidГлавныйКлючСерверGATT "+ UuidГлавныйКлючСерверGATT
                                    +"uuidКлючСервераGATTЧтениеЗапись " +uuidКлючСервераGATTЧтениеЗапись);
                            return UuidГлавныйКлючСерверGATT;
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
                    new SubClassErrors(getApplicationContext()).МетодЗаписиОшибок(valuesЗаписываемОшибки);
                }

                }

                private void МетодРаботыСТекущийСерверомGATT(@NonNull  BluetoothDevice bluetoothDevice,@NonNull  UUID UuidГлавныйКлючСерверGATT) {
                    // TODO: 25.01.2023 ПЕРВЫЙ ВАРИАНТ СЕРВЕР gatt
                    try{
                    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
                        @Override
                        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                            int newState) {
                            try{
                                switch (newState){
                                    case BluetoothProfile.STATE_CONNECTED :
                                        Log.i(TAG, "Connected to GATT client. BluetoothProfile.STATE_CONNECTED ###1 onConnectionStateChange  " +
                                                ""+new Date().toLocaleString());
                                        handler.post(()->{
                                            mediatorLiveDataGATT.setValue("SERVER#SERVER#SouConnect");
                                        });
                                        Boolean ДанныеОТGATTССевромGATT=         gatt.discoverServices();
                                        Log.d(TAG, "Trying to ДанныеОТGATTССевромGATT " + ДанныеОТGATTССевромGATT);
                                        break;
                                    case BluetoothProfile.STATE_DISCONNECTED :
                                        Log.i(TAG, "Connected to GATT client. BluetoothProfile.STATE_DISCONNECTED ###2  onConnectionStateChange" +
                                                "  "+new Date().toLocaleString());
                                        break;
                                }
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
                        // TODO: 26.01.2023
                        @Override
                        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                            super.onServicesDiscovered(gatt, status);
                            try{
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    BluetoothGattService services = gatt.getService(UuidГлавныйКлючСерверGATT);
                                    if (services!=null) {
                                        Boolean КоннектССевромGATT = gatt.connect();
                                        Log.d(TAG, "Trying КоннектССевромGATT " + КоннектССевромGATT);
                                        BluetoothGattCharacteristic characteristics = services.getCharacteristic(uuidКлючСервераGATTЧтениеЗапись);
                                        gatt.setCharacteristicNotification(characteristics, true);
                                        characteristics.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                                        ///Once you have a characteristic object, you can perform read/write
                                        Log.i(TAG, "GATT CLIENT Proccessing from GATT server.");
                                        if (characteristics != null) {
                                            handler.post(() -> {
                                                mediatorLiveDataGATT.setValue("GATTCLIENTCALLBACK");
                                                Log.i(TAG, "GATT CLIENT Proccessing from GATT server.GATTCLIENTProccessing " + new Date().toLocaleString());
                                            });
                                            // TODO: 01.02.2023 отправляем двннеы
                                            characteristics.setValue("действие:" + ДействиеДляСервераGATTОТКлиента);
                                            Boolean successОтправка = gatt.writeCharacteristic(characteristics);
                                        } else {
                                            Log.i(TAG, "Error from GATT server. НЕт ДЕвайса НУжного ");
                                            handler.post(() -> {
                                                mediatorLiveDataGATT.setValue("SERVER#SousAvtoDONTDIVICE");
                                            });
                                        }
                                    }

                                }else{
                                    Log.i(TAG, "GATT CLIENT Proccessing from GATT server.GATTCLIENTProccessing " + new Date().toLocaleString());
                                }
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
                        public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
                            super.onCharacteristicRead(gatt, characteristic, value, status);
                            byte[] newValueПришлиДАнныеОтКлиента= characteristic.getValue();
                            Log.i(TAG, "Connected to GATT server  newValueПришлиДАнныеОтКлиента."+new String(newValueПришлиДАнныеОтКлиента));
                        }

                        @Override
                        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                            super.onCharacteristicChanged(gatt, characteristic);
                            try{
                                byte[] newValueОтветОтСервера = characteristic.getValue();
                                if (newValueОтветОтСервера!=null) {
                                    String ОтветОтСервераОбратно=new String(newValueОтветОтСервера);
                                    Log.i(TAG, "Connected to GATT server  ОтветОтСервераОбратно."+ОтветОтСервераОбратно);
                                    // TODO: 30.01.2023  ПОСЫЛАЕМ ОТВЕТ ОТ СЕРВЕРА СТАТУСА
                                    handler.post(()->{
                                        mediatorLiveDataGATT.setValue(ОтветОтСервераОбратно);
                                    });
                                }
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
                        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
                            Log.d(this.getClass().getName(), "\n" + " gatt" + gatt);
                            super.onServiceChanged(gatt);
                        }
                    };
                    // TODO: 26.01.2023  конец сервера GATT
                        МетодЗапускаGATTКлиента(bluetoothDevice, bluetoothGattCallback);
                        // TODO: 13.02.2023  делаем принудительный таймант по выключение сервервер через 10 секунд
                        МетодВыключениеКлиентаGatt();

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

                private void МетодЗапускаGATTКлиента(@NonNull BluetoothDevice bluetoothDevice, BluetoothGattCallback bluetoothGattCallback) {
                    try{
                    gatt =      bluetoothDevice.connectGatt(context, false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_AUTO,BluetoothDevice.PHY_OPTION_S8,handler);
                    Log.d(this.getClass().getName(), "\n" + " bluetoothDevice" + bluetoothDevice);
                    gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                    //gatt.setPreferredPhy(BluetoothDevice.PHY_LE_2M_MASK,BluetoothDevice.PHY_LE_2M_MASK,BluetoothDevice.PHY_OPTION_S2);
                    int bondstate = bluetoothDevice.getBondState();
                    Log.d(TAG, "Trying to write characteristic..., first bondstate " + bondstate);
                    switch (bondstate) {
                        case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                            Log.i(TAG, "BluetoothDevice.DEVICE_TYPE_UNKNOWN" + bondstate);//Указывает, что удаленное устройство не связано (сопряжено).
                            bluetoothDevice.createBond();
                            handler.post(()->{
                                mediatorLiveDataGATT.setValue("SERVER#SousAvtoDONTDIVICE");
                            });
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            Log.i(TAG, "BluetoothDevice.BOND_BONDING" + bondstate);//Указывает, что удаленное устройство не связано (сопряжено).
                            break;
                        case BluetoothDevice.BOND_NONE://Указывает, что удаленное устройство не связано (сопряжено).
                            bluetoothDevice.createBond();
                            handler.post(()->{
                                mediatorLiveDataGATT.setValue("SERVER#SousAvtoDONTDIVICE");
                            });
                            Log.i(TAG, "BluetoothDevice.BOND_NONE" + bondstate);//Указывает, что удаленное устройство не связано (сопряжено).
                            break;
                        case BluetoothDevice.BOND_BONDED://Указывает, что удаленное устройство связано (сопряжено).
                            Log.i(TAG, "BluetoothDevice.BOND_BONDED " + bondstate);
                            break;
                    }
                    // TODO: 14.02.2023  Слушатель
                    МетодСлушательGattКлиента();
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
                     private void МетодСлушательGattКлиента(){
                          BluetoothProfile.ServiceListener listener= new BluetoothProfile.ServiceListener(){
                             @Override
                             public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                                 // TODO Auto-generated method stub
                                 Log.i(TAG, "BluetoothDevice.bluetoothProfile " + bluetoothProfile);
                                 switch(i){
                                     case BluetoothProfile.HEADSET:
                                         Log.e("found","headset");
                                         break;
                                     case BluetoothProfile.A2DP:
                                         Log.e("found","a2dp");
                                         break;
                                     case BluetoothProfile.HEALTH:
                                         Log.e("found","HEALTH");
                                         break;
                                 }
                             }
                             @Override
                             public void onServiceDisconnected(int i) {
                                 Log.i(TAG, "BluetoothDevice.i " + i);
                             }
                         };
                     }


                private void МетодВыключениеКлиентаGatt() {
                    handler.postDelayed(()->{
                        BluetoothGattService services = gatt.getService(UuidГлавныйКлючСерверGATT);
                        Log.i(TAG, "GATT CLIENT Proccessing from GATT server.SERVER#SousAvtoEXIT " +
                                new Date().toLocaleString() + ДействиеДляСервераGATTОТКлиента + " BluetoothGatt.GATT_SUCCESS "+BluetoothGatt.GATT_SUCCESS+ " services "+services);
                        if (services==null) {
                            mediatorLiveDataGATT.setValue("SERVER#SERVER#SousAvtoNULL");
                        }

                    },10000);
                }
            });
            // TODO: 11.02.2023
                esМенеджерПотоковСканер.poll();
            Log.i(TAG, " ОтветОтGattServer  " +new Date().toLocaleString());
           /// mediatorLiveDataGATT
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


    // TODO: 01.02.2023  класс Запуск OneSignala



    // TODO: 14.11.2021  ПОВТОРЫЙ ЗАПУСК ВОРК МЕНЕДЖЕР

    public String МетодПолучениеНовогоДляСканеарКлюча_OndeSignal(@NonNull String КлючДляFirebaseNotification) {
        final String[] ВозврящаетсяКлючScannerONESIGNAl = {null};
        try {
            // TODO: 23.12.2021 ЧЕТЫРЕ ПОПЫТКИ ПОДКЛЮЧЕНИЕ В СЕВРЕРУONESIGNAL
            Observable observableПолученияКлючаОтСервераOneSignal=  Observable.interval(5, TimeUnit.SECONDS)
                    .delay(3,TimeUnit.SECONDS)
                    .take(3,TimeUnit.MINUTES)
                    .subscribeOn(Schedulers.single())
                    .doOnNext(new io.reactivex.rxjava3.functions.Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Throwable {
                            // TODO: 01.02.2023 Получение Новго Ключа Для Сканера
                            ВозврящаетсяКлючScannerONESIGNAl[0] =     МетодПолучениеКлючаОтСлужбыONESIGNALAndFirebase(КлючДляFirebaseNotification);
                            Log.d(context.getClass().getName(), "  Observable.interval    ВозврящаетсяКлючScannerONESIGNAl[0] " +   ВозврящаетсяКлючScannerONESIGNAl[0]);
                        }
                    })
                    .doOnError(new io.reactivex.rxjava3.functions.Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Throwable {
                            Log.e(context.getClass().getName(), "  doOnError МетодПолучениеНовгоКлюча_OndeSignal "  +"\n" +throwable.getMessage().toString());
                        }
                    })
                    .takeWhile(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Throwable {
                            // TODO: 26.12.2021
                            Log.w(context.getClass().getName(), "   takeWhile ВозврящаетсяКлючScannerONESIGNAl " +
                                    "" +Thread.currentThread().getName()+ "  ВозврящаетсяКлючScannerONESIGNAl " + ВозврящаетсяКлючScannerONESIGNAl[0]);
                            if (   ВозврящаетсяКлючScannerONESIGNAl[0] !=null) {
                                Log.w(context.getClass().getName(), "  ДЛЯ ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ (телефона)Ключ ПришелОтСЕРВЕРА SUCEESSSSSS !!!@!  " +
                                        " takeWhile МетодПовторногоЗапускаFacebaseCloud_And_OndeSignal САМ КЛЮЧ ::::"
                                        + ВозврящаетсяКлючScannerONESIGNAl[0] +"\n"+
                                        " Thread.currentThread().getName() " +Thread.currentThread().getName());
                                return false;
                            }else {
                                return true;
                            }
                        }
                    })
                    .doOnComplete(new Action() {
                        @Override
                        public void run() throws Throwable {
                            Log.w(context.getClass().getName(), " doOnTerminate  ВозврящаетсяКлючScannerONESIGNAl" + ВозврящаетсяКлючScannerONESIGNAl[0]);
                            // TODO: 06.01.2022
                            // TODO: 06.01.2022
                            Log.w(context.getClass().getName(), "  onComplete МетодПовторногоЗапускаFacebaseCloud_And_OndeSignal"  +"\n"+
                                    " Thread.currentThread().getName() " +Thread.currentThread().getName());
                        }
                    });
// TODO: 07.01.2022 GREAT OPERATIONS подпииска на данные
            observableПолученияКлючаОтСервераOneSignal.subscribe();
            // TODO: 05.01.2022  ДЕЛАЕМ ПОДПИСКУ НА ОСУЩЕСТВЛЛЕНУЮ ДАННЫХ
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

        return ВозврящаетсяКлючScannerONESIGNAl[0];
    }

    private String МетодПолучениеКлючаОтСлужбыONESIGNALAndFirebase(@NonNull String КлючДляFirebaseNotification) {
        String ПоулчаемДляТекущегоПользователяIDОтСЕРВРЕРАOneSignal = null;
        try{
            //TODO srating......  oneSignal
            Log.d(this.getClass().getName(), "  КЛЮЧ ДЛЯ Scanner  OneSignal........ "+ КлючДляFirebaseNotification +"\n");
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            // todo OneSignal Initialization
            OneSignal.initWithContext(context);
            ///////todo srating Google Notifications wits PUblic Key
            OneSignal.setAppId(КлючДляFirebaseNotification);
            OneSignal.disablePush(false);
            //TODO srating.......... firebase cloud --ПРИШЛО СООБЩЕНИЕ
            FirebaseMessagingService firebaseMessagingService =new MyFirebaseMessagingServiceScanner();
            //TODO srating......  oneSignal
            Log.d(this.getClass().getName(), "  FirebaseMessagingService"  );
            // TODO: 07.12.2021
            firebaseMessagingService.onNewToken("Сообщения от Firebase Cloud Google ");
            Log.d(this.getClass().getName(), "  КЛЮЧ ДЛЯ  КОНЕЦ  OneSignal........  56bbe169-ea09-43de-a28c-9623058e43a2 " );
            // TODO: 15.12.2021 настройки onesigmnal
            Map<String, String> params = new HashMap<String, String>();
            OneSignal.sendTag("Authorization", "Basic 56bbe169-ea09-43de-a28c-9623058e43a2");
            OneSignal.sendTag("Content-type", "application/json");
            OneSignal.sendTag("grp_msg", "scanner");
            OneSignal.sendTag("android_background_data", "true");
            OneSignal.sendTag("content_available", "true");
            //TODO srating......  oneSignal
          String  PushОнеСигнала = OneSignal.getDeviceState().getPushToken();

            ПоулчаемДляТекущегоПользователяIDОтСЕРВРЕРАOneSignal = OneSignal.getDeviceState().getUserId();
            // TODO: 15.12.2021
            Log.d(this.getClass().getName(), "  ПОСЛЕ КЛЮЧ ДЛЯ  OneSignal........  56bbe169-ea09-43de-a28c-9623058e43a2 "+"\n"+
                    "   OneSignal.getTriggerValueForKey(\"GT_PLAYER_ID\"); " + OneSignal.getTriggerValueForKey("GT_PLAYER_ID")+
                    "     OneSignal.getTriggers() " +   OneSignal.getTriggers()+"\n"+
                    "    ПоулчаемДляТекущегоПользователяIDОтСЕРВРЕРАOneSignal ОТ СЕРВЕРА ::: " + ПоулчаемДляТекущегоПользователяIDОтСЕРВРЕРАOneSignal);
            // TODO: 13.12.2021
            // TODO: 05.01.2022  ДЕЛАЕМ ПОДПИСКУ НА ОСУЩЕСТВЛЛЕНУЮ ДАННЫХ
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
        return ПоулчаемДляТекущегоПользователяIDОтСЕРВРЕРАOneSignal;
    }

}

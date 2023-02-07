package com.sous.scanner.VIEW;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;
import com.sous.scanner.CONTROL.ServiceControllerКлиент;
import com.sous.scanner.MODEL.SubClassErrors;
import com.sous.scanner.R;

import org.jetbrains.annotations.NotNull;





public class MainActivityNewScanner extends AppCompatActivity  {
    private ServiceControllerКлиент.LocalBinderСканнер binderСканнер;
    private ServiceConnection connectionСканирование;
    private String TAG;
    private Handler handler;
    private NavigationBarView bottomNavigationView;
    private BottomNavigationItemView bottomNavigationItemViewВыход;
    private BottomNavigationItemView bottomNavigationItemViewИстория;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private LinearLayout linearLayou;
    private     Long version=0l;
    private MaterialTextView materialTextViewToolBar;
    private  MutableLiveData<Binder> event;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_newscanner);
            TAG = getClass().getName().toString();
            getSupportActionBar().hide(); ///скрывать тул бар
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // TODO: 08.12.2022
            bottomNavigationView = (NavigationBarView) findViewById(R.id.BottomNavigationViewScanner);
            bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);
            materialTextViewToolBar=( MaterialTextView)  findViewById(R.id.text_scanner_work);
            // TODO: 05.12.2022 строчлочки
            bottomNavigationItemViewВыход = bottomNavigationView.findViewById(R.id.id_lback);
            bottomNavigationItemViewИстория = bottomNavigationView.findViewById(R.id.id_scanner_history);
            bottomNavigationItemViewВыход.setItemRippleColor(ColorStateList.valueOf(Color.RED));
            bottomNavigationItemViewИстория.setItemRippleColor(ColorStateList.valueOf(Color.RED));
            linearLayou = (LinearLayout) findViewById(R.id.activity_main_newscanner);
            Log.w(getApplicationContext().getClass().getName(), " MainActivityNewScanner onCreate  ");
            fragmentManager = getSupportFragmentManager();
            bottomNavigationView.setVisibility(View.INVISIBLE);
            materialTextViewToolBar.setText("");
            materialTextViewToolBar.setVisibility(View.INVISIBLE);
            Log.w(getApplicationContext().getClass().getName(), " MainActivityNewScanner onCreate  ");
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = pInfo.getLongVersionCode();

            МетодРАзрешенияBlurtooTКлиент();




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

    private void МетодРАзрешенияBlurtooTКлиент() {
        try{

      String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
              /*  Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,*/
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_PRIVILEGED,
              Manifest.permission.BLUETOOTH_ADVERTISE,
              Manifest.permission.BLUETOOTH_ADMIN
        };
             String[] PERMISSIONS_LOCATION = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            /*    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,*/
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_PRIVILEGED,
                     Manifest.permission.BLUETOOTH_ADVERTISE,
                     Manifest.permission.BLUETOOTH_ADMIN
        };
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
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







    public void МетодСобыытиеКнопокСканирования(@NotNull Intent intent) {
        try {
            bottomNavigationItemViewВыход.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(this.getClass().getName(), " bottomNavigationView " + bottomNavigationView);
                    if (intent.getAction().equalsIgnoreCase("fragment")) {
                        finishAndRemoveTask();
                    }
                }
            });
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


    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();
        try {
            event = new MutableLiveData<>();
            event.postValue(binderСканнер);
            LifecycleOwner lifecycleOwner =this ;
            lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    source.getLifecycle().getCurrentState();
                    event.getTargetState().name();
                }
            });
            event.observe(lifecycleOwner, binderСканнер-> {
                Log.d(this.getClass().getName(), " binderСканнер " + binderСканнер);
                if (event.getValue()!=null) {
                handler.postDelayed(() -> {
                    // TODO: 24.01.2023  переходят после получение binder
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    materialTextViewToolBar.setVisibility(View.VISIBLE);
                    materialTextViewToolBar.setText("Клиент");
                    // TODO: 25.01.2023  подключение после получение BINDER
                    МетодСобыытиеКнопокСканирования(new Intent("activity"));
                    // TODO: 07.02.2023  первый запуск фгармента

                    // TODO: 07.02.2023 второй запуск фрагмента
                    if (fragment!=null) {
                        fragment.onDetach();
                        fragmentTransaction.remove(fragment);
                    }
                    fragment=new FragmentScannerUser();
                    МетодЗапускКлиентаИлиСервера(fragment);//todo Запускам клиента или сервер фрагмент
                }, 1000);
            }
            });
            
            МетодУжеЗапускаСканнерВторойФрагмент();
            // TODO: 24.01.2023 методы для блютусаа
            Log.d(this.getClass().getName(), " binderСканнер " + binderСканнер+  " КоличестовФрагмент ");

            ОтветныйHendlerОтСлужбы();
            МетодНастрокийBlueTools();
            Log.d(this.getClass().getName(), " binderСканнер " + binderСканнер);
            // TODO: 05.12.2022  метод  разрешения  blutools
            МетодБиндингаСканирование();

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

    private void МетодУжеЗапускаСканнерВторойФрагмент() {
        try {
            // TODO: 07.02.2023 запускаем
        fragment=new FragmentBootScanner();
        МетодЗапускКлиентаИлиСервера(fragment);//todo Запускам клиента или сервер фрагмент
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


    @SuppressLint("MissingPermission")
    private void МетодНастрокийBlueTools() {
        ///todo разрешения 1
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_PRIVILEGED,
                    Manifest.permission.READ_EXTERNAL_STORAGE,}, PackageManager.PERMISSION_GRANTED);
            Intent discoverableIntent = new Intent();
            discoverableIntent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);//BluetoothAdapter.ACTION_DISCOVERY_FINISHED
            discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(discoverableIntent, 11);


/*        Intent intentвызовВидмости=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intentвызовВидмости.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           // intentвызовВидмости.putExtra("BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION",0);
             startActivity(intentвызовВидмости);*/

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: 05.12.2022  запуск клиента
    @SuppressLint("SuspiciousIndentation")
    protected void МетодЗапускКлиентаИлиСервера(@NonNull Fragment fragment) {
        try {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.framelauoutScanner, fragment);//.layout.activity_for_fragemtb_history_tasks
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            if (binderСканнер!=null) {
                Bundle bundle=new Bundle();
                bundle.putBinder("binderСканнер",binderСканнер);
                fragment.setArguments(bundle);
            }
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.show(fragment);
                Log.d(this.getClass().getName(), " fragment " + fragment+ " binderСканнер " +binderСканнер);
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




    private void ОтветныйHendlerОтСлужбы() {
        try {
            handler = new Handler(getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Log.d(TAG, "onCreate: msg " + msg);
                    return true;
                }
            });
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
    // TODO: 29.11.2022 служба сканирования
    private    void   МетодБиндингаСканирование(){
        try {
                    connectionСканирование = new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            try{
                                binderСканнер = ( ServiceControllerКлиент.LocalBinderСканнер) service;
                                if(binderСканнер.isBinderAlive()){
                                    Log.i(getApplicationContext().getClass().getName(), "    onServiceConnected  binderСогласованияbinderМатериалы.isBinderAlive()"
                                            + binderСканнер.isBinderAlive());
                                    binderСканнер.linkToDeath(new IBinder.DeathRecipient() {
                                        @Override
                                        public void binderDied() {
                                            Log.i(getApplicationContext().getClass().getName(), "    onServiceConnected  binderСогласованияbinderМатериалы.isBinderAlive()"
                                                    + binderСканнер.isBinderAlive());

                                            event.setValue(binderСканнер);

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                                        + Thread.currentThread().getStackTrace()[2].getLineNumber());
                                ContentValues valuesЗаписываемОшибки=new ContentValues();
                                valuesЗаписываемОшибки.put("НазваниеОбрабоатываемойТаблицы","ErrorDSU1");
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
                        public void onServiceDisconnected(ComponentName name) {
                            try{
                                Log.i(getApplicationContext().getClass().getName(), "    onServiceDisconnected  binderСканнер.isBinderAlive()" + binderСканнер.isBinderAlive());
                                binderСканнер =null;
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :"
                                        + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                        " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                            }
                        }
                    };
                    Intent intentБиндингсСлужбойСканирования =
                            new Intent(getApplicationContext(), ServiceControllerКлиент.class);
                    bindService(intentБиндингсСлужбойСканирования, connectionСканирование, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() + " Линия  :"
                    + Thread.currentThread().getStackTrace()[2].getLineNumber());
            ContentValues valuesЗаписываемОшибки=new ContentValues();
            valuesЗаписываемОшибки.put("НазваниеОбрабоатываемойТаблицы","ErrorDSU1");
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
}
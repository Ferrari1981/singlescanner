package For_Code_ScannerBluetoolth_old;/*
package com.dsy.dsu.For_Code_ScannerBluetoolth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsy.dsu.Business_logic_Only_Class.DATE.Class_Generation_Data;
import com.dsy.dsu.For_Code_Settings_DSU1.MainActivity_Face_App;
import com.dsy.dsu.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;



public class FragmentScannerBluetoolth extends Fragment {
    private SubClassBissneesLogicScanner subClassBissneesLogicScanner;
    private SubClassBissneesLogicScanner.MyRecycleViewAdapter myRecycleViewAdapter;
    private SubClassBissneesLogicScanner.MyViewHolder myViewHolder;
    private FragmentManager fragmentManagerДляЗадачи;
    private FragmentTransaction fragmentTransactionляЗадачи;
    private TextView textViewТекущаяЗадача;
    private Integer ПубличныйIDДляФрагмента;
    private RecyclerView recyclerView;
    private LinearLayout linearLayou;
    private Fragment fragment_ТекущийФрагментСогласованиеСписок;
    private BottomNavigationView bottomNavigationViewДляСканированияBluetooth;
    private BottomNavigationItemView bottomНазад;
    private BottomNavigationItemView bottomПингВключитьВыкючить;
    private ProgressBar progressBarСканирование;
    private Boolean РезультатИзмененияСтатусаСогласованияОтказаИлиУспешноеСогласования;
    private Boolean ФлагПриНажатииНаКнопкуОтменаПоискаУстровйстBluetooth = false;
    private LayoutAnimationController layoutAnimationController;
    private Animation animationДляСогласования;
    private ArrayList<ConcurrentSkipListMap<String, String>> ArrayListДанныеОтСканироваиниеДивайсов = new ArrayList();
    private ConcurrentSkipListMap<String, String> ХэшДааныеСтрока= new ConcurrentSkipListMap<>();
    private ConcurrentSkipListMap<String, String> ХэшДанныхПроверкаЕслиУже = new ConcurrentSkipListMap<>();
    private LocationManager locationМанажер;
    private LocationListener LocationСлушатель;
    private BluetoothAdapter adapter;
    private  ScanCallback scanCallback;
    private BluetoothLeScanner scanner;
    private      Handler handlerСканирование;
    private boolean КнопкаНажималасьУжеИлинетONOFF=false;

    public FragmentScannerBluetoolth() {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d(this.getClass().getName(), "  onViewCreated  Fragment1_One_Tasks view   " + view);
            recyclerView = view.findViewById(R.id.recycleviewcommitpay);
            recyclerView.setVisibility(View.VISIBLE);
            textViewТекущаяЗадача = view.findViewById(R.id.TextView);
            Log.d(this.getClass().getName(), "  Fragment1_One_Tasks  textViewТекущаяЗадача " + textViewТекущаяЗадача + " view " + view);
            textViewТекущаяЗадача.setText("Устройства".toUpperCase());
            fragmentManagerДляЗадачи = getActivity().getSupportFragmentManager();
            linearLayou = getActivity().findViewById(R.id.activityfragmentscannerss);
            bottomNavigationViewДляСканированияBluetooth = view.findViewById(R.id.bottomnavigationActivicommit);
            bottomNavigationViewДляСканированияBluetooth.setItemHorizontalTranslationEnabled(true);
            bottomНазад = bottomNavigationViewДляСканированияBluetooth.findViewById(R.id.id_scannerback);
            bottomПингВключитьВыкючить = bottomNavigationViewДляСканированияBluetooth.findViewById(R.id.id_scannerooff);
            bottomНазад.setTitle("Выйти");
            bottomНазад.setSelected(true);
            bottomПингВключитьВыкючить.setTitle("Пинг (вкл./выкл)");
            progressBarСканирование = view.findViewById(R.id.prograessbarcommitpaydown); /////КНОПКА ТАБЕЛЬНОГО УЧЕТА
            layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animal);
            animationДляСогласования = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_row_vibrator1);
            subClassBissneesLogicScanner = new SubClassBissneesLogicScanner(getContext(), getActivity());
            subClassBissneesLogicScanner.МетодИнициализацииRecycleViewДляЗадач();
            locationМанажер = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            adapter = BluetoothAdapter.getDefaultAdapter();
            if (!adapter.isEnabled()) {
                adapter.enable();
            }
            subClassBissneesLogicScanner.МетодBluetoolthBroadReceivier(getActivity());
            subClassBissneesLogicScanner.      МетодCallBack();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
            ///метод запись ошибок в таблицу
            Log.e(getContext().getClass().getName(),
                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = null;
        try {
            view = inflater.inflate(R.layout.fragment_scanner, container, false);
            Log.d(this.getClass().getName(), " onCreateView  viewДляПервойКнопкиHome_Задания  Fragment1_One_Tasks  onCreateView " +
                    "" + view);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
            ///метод запись ошибок в таблицу
            Log.e(getContext().getClass().getName(),
                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
  ХэшДааныеСтрока.putIfAbsent("hf12:gt23:ty67:tt00","Samsung");
            Log.d(this.getClass().getName(), "ХэшДааныеСтрока " + ХэшДааныеСтрока);
            ArrayListДанныеОтСканироваиниеДивайсов.add(ХэшДааныеСтрока);
            ХэшДааныеСтрока=new ConcurrentSkipListMap<>();
            ХэшДааныеСтрока.putIfAbsent("GG12:Tt23:ty67:ST85","RRedmi");
            ArrayListДанныеОтСканироваиниеДивайсов.add(ХэшДааныеСтрока);

            //  ArrayListДанныеОтСканироваиниеДивайсов.add(ХэшДааныеСтрока);
            subClassBissneesLogicScanner.МетодСканированиеBluetooht(getContext(), adapter);
            Log.d(this.getClass().getName(), "ArrayListДанныеОтСканироваиниеДивайсов " + ArrayListДанныеОтСканироваиниеДивайсов);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
            ///метод запись ошибок в таблицу
            Log.e(getContext().getClass().getName(),
                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }

    }

    // TODO: 12.03.2022  метод с бизнес логикой
    @Override
    public void onResume() {
        super.onResume();
        try {
            Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1 imageView  Fragment1_One_Tasks  onStart");
            subClassBissneesLogicScanner.МетодЗаполенияRecycleViewДляЗадач();
            subClassBissneesLogicScanner.МетодСозданиеНавигаторКнопок();
            subClassBissneesLogicScanner.МетодКпопкаСоЗачкомКраснымДополнительныйСтатус(ArrayListДанныеОтСканироваиниеДивайсов);
            subClassBissneesLogicScanner.МетодКпопкаВозвращениеНазадИзСогласованиии();
            subClassBissneesLogicScanner.МетодКпопкаПринидительноОбмена();
            subClassBissneesLogicScanner.МетодСлушательObserverДляRecycleView();
            subClassBissneesLogicScanner.МетодПерегрузкаRecyceView();
//TODO
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
            ///метод запись ошибок в таблицу
            Log.e(getContext().getClass().getName(),
                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            scanner.stopScan(scanCallback);
            adapter.cancelDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
            ///метод запись ошибок в таблицу
            Log.e(getContext().getClass().getName(),
                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
    }

    // TODO: 10.03.2022 БИЗНЕС-КОД ПЕРЕНЕСЕН ИЗ АКТИВТИ
    protected class SubClassBissneesLogicScanner {
        Context context;
        Activity activity;
        public SubClassBissneesLogicScanner(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
            Log.d(this.getClass().getName(), "  public SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1(Context context, Activity activity)   " + context);
        }
        void МетодСлушательObserverДляRecycleView() {  // TODO: 04.03.2022  класс в котором находяться слушатели
            try {
                myRecycleViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        try {
                            Log.d(this.getClass().getName(), "onChanged ");
                            //TODO
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                            ///метод запись ошибок в таблицу
                            Log.e(getContext().getClass().getName(),
                                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        }
                    }

                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount) {
                        super.onItemRangeChanged(positionStart, itemCount);
                        // TODO: 05.03.2022  СТАТУС ЗНАЧКА С ДОПОЛНИТЕЛЬНЫЙ СТАТУСОМ
                        try {
                            Log.d(this.getClass().getName(), "onItemRangeChanged ");
                            //TODO
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                            ///метод запись ошибок в таблицу
                            Log.e(getContext().getClass().getName(),
                                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        }
                    }

                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                        super.onItemRangeChanged(positionStart, itemCount, payload);
                        try {
                            Log.d(this.getClass().getName(), "onItemRangeChanged ");
                            //TODO
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                            ///метод запись ошибок в таблицу
                            Log.e(getContext().getClass().getName(),
                                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        }
                    }

                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        try {
                            Log.d(this.getClass().getName(), "onItemRangeInserted ");
                            //TODO
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                            ///метод запись ошибок в таблицу
                            Log.e(getContext().getClass().getName(),
                                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        }
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                        try {
                            Log.d(this.getClass().getName(), "onItemRangeRemoved ");
                            //TODO
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                            ///метод запись ошибок в таблицу
                            Log.e(getContext().getClass().getName(),
                                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        }
                    }

                    @Override
                    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                        try {
                            Log.d(this.getClass().getName(), "     onItemRangeMoved ");
                            //TODO
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                            ///метод запись ошибок в таблицу
                            Log.e(getContext().getClass().getName(),
                                    "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                            " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                        }
                    }
                });
                //TODO
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }

        // TODO: 02.03.2022 выход

        private void МетодКпопкаВозвращениеНазадИзСогласованиии()
                throws ExecutionException, InterruptedException {
            try {
                Log.d(this.getClass().getName(), "  выходим из задания МетодКпопкаВозвращениеНазадИзСогласованиии");
                bottomНазад.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Интент_BackВозвращаемАктивти = getActivity().getIntent();
                        Интент_BackВозвращаемАктивти.setClass(getContext(), MainActivity_Face_App.class); // Т
                        Интент_BackВозвращаемАктивти.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle gameData = new Bundle();
                        gameData.putString("ФлагСтатусИзФрагментаСканирования", "ЗакрываетИзСканирования");
                        Интент_BackВозвращаемАктивти.putExtras(gameData);
                        Log.d(this.getClass().getName(), "  выходим из задания МетодКпопкаВозвращениеНазадИзСогласованиии");
                        startActivity(Интент_BackВозвращаемАктивти);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }

        }

        private void МетодКпопкаПринидительноОбмена()
                throws ExecutionException, InterruptedException {
            try {
                Log.d(this.getClass().getName(), "  принудительный обмен  МетодКпопкаВозвращениеНазадИзСогласованиии");
                // TODO: 09.03.2022
                bottomПингВключитьВыкючить.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(this.getClass().getName(), "  принудительный обмен  МетодКпопкаВозвращениеНазадИзСогласованиии");

                        if (КнопкаНажималасьУжеИлинетONOFF==false) {
                            progressBarСканирование.setVisibility(View.INVISIBLE);
                            scanner.stopScan(scanCallback);
                            adapter.cancelDiscovery();
                            КнопкаНажималасьУжеИлинетONOFF=true;
                            Log.d(this.getClass().getName(), "  выключили сканирование bottomПингВключитьВыкючить OFF");
                        } else {
                            progressBarСканирование.setVisibility(View.VISIBLE);
                            adapter.startDiscovery();
                            scanner.startScan(scanCallback);
                            Log.d(this.getClass().getName(), "  включили  сканирование bottomПингВключитьВыкючить ON");

                        }



                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }

        // TODO: 04.03.2022 прозвомжность Заполения RecycleView
        void МетодЗаполенияRecycleViewДляЗадач() {
            try {
                Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1  ARRAYJSONПолучениеДанныхОт1с  "
                        + ArrayListДанныеОтСканироваиниеДивайсов);
                myRecycleViewAdapter = new SubClassBissneesLogicScanner.MyRecycleViewAdapter(ArrayListДанныеОтСканироваиниеДивайсов);
                recyclerView.setAdapter(myRecycleViewAdapter);
                Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1 recyclerView   " + recyclerView);
                //TODO
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }


        // TODO: 04.03.2022 прозвомжность инициализации RecycleView
        void МетодИнициализацииRecycleViewДляЗадач() {
            try {
                Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1  БуферПолучениеДанныхОт1с  "
                        + ArrayListДанныеОтСканироваиниеДивайсов);
                Log.d(this.getClass().getName(), " БуферПолучениеДанныхОт1с " + ArrayListДанныеОтСканироваиниеДивайсов);
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);//TODO new LinearLayoutManager(getContext()) // TODO: 28.02.2022 создаем наш первый RecyclerView recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1 recyclerView   " + recyclerView);
                ;
                //TODO
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }

        }

        // TODO: 05.03.2022 метод создание кнопок снизу навигатор
        void МетодСозданиеНавигаторКнопок() {
            try {
                bottomNavigationViewДляСканированияBluetooth.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        fragmentTransactionляЗадачи = fragmentManagerДляЗадачи.beginTransaction();
                        Log.d(this.getClass().getName(), "  bottomNavigationViewДляTasks " + bottomNavigationViewДляСканированияBluetooth + " fragment_ТекущийФрагмент " + fragment_ТекущийФрагментСогласованиеСписок);
                        Log.d(this.getClass().getName(), "  item.getItemId() " + item.getItemId());
                        switch (item.getItemId()) {
                            // TODO: 14.03.2022
                            case R.id.id_scannerback:
                                item.setChecked(true);
                                Log.d(this.getClass().getName(), " bottomNavigationViewДляTasks.getChildCount() " + bottomNavigationViewДляСканированияBluetooth.getChildCount());
                                break;
                            case R.id.id_scannerooff:
                                item.setChecked(true);
                                Log.d(this.getClass().getName(), " R.id.id_taskHome отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1  fragmentTransactionляЗадачи  "
                                        + fragmentTransactionляЗадачи + " R.id.id_taskHome  item.getItemId() " + item.getItemId());
                                break;
                            default:
                                Log.d(this.getClass().getName(), "  никакой не выбрали  item.getItemId() ");
                                return false;
                        }
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }


        // TODO: 14.03.2022
        private void МетодКпопкаСоЗачкомКраснымДополнительныйСтатус(@NonNull ArrayList ArrayListДанныеОтСканироваиниеДивайсов)
                throws ExecutionException, InterruptedException {
            try {
                Log.d(this.getClass().getName(), "  ArrayListДанныеОтСканироваиниеДивайсов .length() " + ArrayListДанныеОтСканироваиниеДивайсов.size());
                if (ArrayListДанныеОтСканироваиниеДивайсов.size() > 0) {
                    bottomNavigationViewДляСканированияBluetooth.getOrCreateBadge(R.id.id_scannerooff).setNumber(ArrayListДанныеОтСканироваиниеДивайсов.size());//.getOrCreateBadge(R.id.id_taskHome).setVisible(true);
                    bottomNavigationViewДляСканированияBluetooth.getOrCreateBadge(R.id.id_scannerooff).setBackgroundColor(Color.parseColor("#15958A"));
                } else {
                    bottomNavigationViewДляСканированияBluetooth.getOrCreateBadge(R.id.id_scannerooff).setNumber(ArrayListДанныеОтСканироваиниеДивайсов.size());//.getOrCreateBadge(R.id.id_taskHome).setVisible(true);
                    bottomNavigationViewДляСканированияBluetooth.getOrCreateBadge(R.id.id_scannerooff).setBackgroundColor(Color.RED);
                }
                bottomNavigationViewДляСканированияBluetooth.requestLayout();
                bottomNavigationViewДляСканированияBluetooth.forceLayout();
                //TODO
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }

        // TODO: 28.02.2022 начало  MyViewHolderДляЧата
        protected class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewMACадрес, textViewВремяСканирование, textViewНазваниеDivice;
            private MaterialCardView materialCardBlueScanner;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                try {
                    МетодИнициализацииКомпонетовЗаданияCardView(itemView);
                    Log.d(this.getClass().getName(), "  private class MyViewHolderДляЧата extends RecyclerView.ViewHolder  itemView   " + itemView);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
            }

            private void МетодИнициализацииКомпонетовЗаданияCardView(@NonNull View itemView) {
                try {
                    Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1 itemView   " + itemView);
                    textViewMACадрес = itemView.findViewById(R.id.text3_valuepay);
                    textViewВремяСканирование = itemView.findViewById(R.id.text2_valuepay);
                    textViewНазваниеDivice = itemView.findViewById(R.id.text0_valuepay);
                    materialCardBlueScanner = itemView.findViewById(R.id.cardviewmatireacommitpay);
                    Log.d(this.getClass().getName(), " отработоатл new SubClassBuccessLogin_ГлавныйКлассБизнесЛогикиФрагмент1 materialCardView   " + materialCardBlueScanner);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
            }
        }

        class MyRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
            private ArrayList ArrayListДанныеОтСканироваиниеДивайсов;

            public MyRecycleViewAdapter(@NotNull ArrayList ArrayListДанныеОтСканироваиниеДивайсов) {
                this.ArrayListДанныеОтСканироваиниеДивайсов = ArrayListДанныеОтСканироваиниеДивайсов;
                if (ArrayListДанныеОтСканироваиниеДивайсов.size() > 0) {
                    Log.i(this.getClass().getName(), " ArrayListДанныеОтСканироваиниеДивайсов  " + ArrayListДанныеОтСканироваиниеДивайсов.size());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
                Log.i(this.getClass().getName(), "   onBindViewHolder  position" + position + " ArrayListДанныеОтСканироваиниеДивайсов " + ArrayListДанныеОтСканироваиниеДивайсов);
                try {
                    ///todo ЩЕЛКАЕМ КАЖДУЮ СТРОЧКУ ОТДЕЛЬНО
                    if (ArrayListДанныеОтСканироваиниеДивайсов.size() > 0) {
                        ХэшДааныеСтрока = (ConcurrentSkipListMap<String, String>) ArrayListДанныеОтСканироваиниеДивайсов.get(position);
                        Log.d(this.getClass().getName(), "ХэшДааныеСтрока " + ХэшДааныеСтрока +
                                " ArrayListДанныеОтСканироваиниеДивайсов " + ArrayListДанныеОтСканироваиниеДивайсов + " position " + position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
                super.onBindViewHolder(holder, position, payloads);
            }

            @Override
            public void setHasStableIds(boolean hasStableIds) {
                super.setHasStableIds(hasStableIds);
            }

            @Override
            public void onViewRecycled(@NonNull MyViewHolder holder) {
                super.onViewRecycled(holder);
            }

            @Override
            public boolean onFailedToRecycleView(@NonNull MyViewHolder holder) {
                return super.onFailedToRecycleView(holder);
            }

            @Override
            public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
                super.onViewAttachedToWindow(holder);
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
                super.onViewDetachedFromWindow(holder);
            }

            @Override
            public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

                recyclerView.removeAllViews();

                recyclerView.getRecycledViewPool().clear();
                super.onAttachedToRecyclerView(recyclerView);
            }

            @Override
            public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
                super.onDetachedFromRecyclerView(recyclerView);
            }

            @Override
            public int getItemViewType(int position) {
                Log.i(this.getClass().getName(), "      holder.textView1  position " + position);
                try {
                    Log.i(this.getClass().getName(), "   getItemViewType  position" + position);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
                return super.getItemViewType(position);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewГлавныйВидДляRecyclleViewДляСогласования = null;
                try {
                    Log.i(this.getClass().getName(), "   ХэшДааныеСтрока" + ХэшДааныеСтрока);
                    if (ХэшДааныеСтрока.size() > 0) {
                        viewГлавныйВидДляRecyclleViewДляСогласования = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_for_scannerbuetooth, parent, false);//todo old simple_for_takst_cardview1
                        Log.i(this.getClass().getName(), "   viewГлавныйВидДляRecyclleViewДляСогласования" + viewГлавныйВидДляRecyclleViewДляСогласования);
                    } else {
                        if (ФлагПриНажатииНаКнопкуОтменаПоискаУстровйстBluetooth == false) {
                            viewГлавныйВидДляRecyclleViewДляСогласования = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_for_scanner_searchiing, parent, false);//todo old simple_for_takst_cardview1
                            Log.i(this.getClass().getName(), "   viewГлавныйВидДляRecyclleViewДляСогласования" + viewГлавныйВидДляRecyclleViewДляСогласования);
                        } else {
                            viewГлавныйВидДляRecyclleViewДляСогласования = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_for_dont_diveci, parent, false);//todo old simple_for_takst_cardview1
                            Log.i(this.getClass().getName(), "   viewГлавныйВидДляRecyclleViewДляСогласования" + viewГлавныйВидДляRecyclleViewДляСогласования);
                        }
                    }
                    myViewHolder = new SubClassBissneesLogicScanner.MyViewHolder(viewГлавныйВидДляRecyclleViewДляСогласования);
                    Log.i(this.getClass().getName(), "   myViewHolder" + myViewHolder + "  ХэшДааныеСтрока " + ХэшДааныеСтрока);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
                return myViewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                try {
                    Log.i(this.getClass().getName(), "   создание согласования" + myViewHolder + " ХэшДааныеСтрока " + ХэшДааныеСтрока);
                    if (ХэшДааныеСтрока.size() > 0) {
                        МетодЗаполенияИмениBlueToolth(holder, ХэшДааныеСтрока);
                        МетодЗаполненияIPBlueTooth(holder, ХэшДааныеСтрока);
                        МетодЗаполнененияДатыСканирование(holder, ХэшДааныеСтрока);
                    } else {
                        Log.i(this.getClass().getName(), "   создание согласования" + myViewHolder + " ХэшДааныеСтрока " + ХэшДааныеСтрока);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
            }

            private void МетодАнимации(MyViewHolder holder) {
                try {
                    //holder.itemView.setAnimation(animationДляСогласования);
                    // holder.materialCardViewCommit.setAnimation(animationДляСогласования);
                    holder.materialCardBlueScanner.startAnimation(animationДляСогласования);

                    //TODO
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
            }


            ///todo первый метод #1

            private void МетодЗаполенияИмениBlueToolth(@NonNull MyViewHolder holder, @NonNull ConcurrentSkipListMap<String, String> ХэшДааныеСтрока) {
                try {
                    if (ХэшДааныеСтрока != null && holder.textViewMACадрес != null) {
                        ХэшДааныеСтрока.entrySet().forEach(new Consumer<Map.Entry<String, String>>() {
                            @Override
                            public void accept(Map.Entry<String, String> stringStringEntry) {
                                String ПерваяСтрочкаЗначения = stringStringEntry.getKey();
                                Log.i(this.getClass().getName(), "  Ndoc  ПерваяСтрочкаЗначения " + ПерваяСтрочкаЗначения);
                                holder.textViewMACадрес.setText(ПерваяСтрочкаЗначения);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
            }


            ///todo первый метод #3

            private void МетодЗаполнененияДатыСканирование(@NonNull MyViewHolder holder, @NonNull ConcurrentSkipListMap<String, String> ХэшДааныеСтрока) {
                try {
                    if (ХэшДааныеСтрока != null && holder.textViewВремяСканирование != null) {
                        String     СгенерированованныйДата=
                                new Class_Generation_Data(getContext()).ГлавнаяДатаИВремяОперацийСБазойДанных();
                        holder.textViewВремяСканирование.setText(СгенерированованныйДата);
                        holder.textViewВремяСканирование.requestLayout();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }

            }


            ///todo первый метод #4

            private void МетодЗаполненияIPBlueTooth(@NonNull MyViewHolder holder, @NonNull ConcurrentSkipListMap<String, String> ХэшДааныеСтрока) {
                try {
                    if (ХэшДааныеСтрока != null && holder.textViewНазваниеDivice != null) {
                        ХэшДааныеСтрока.entrySet().forEach(new Consumer<Map.Entry<String, String>>() {
                            @Override
                            public void accept(Map.Entry<String, String> stringStringEntry) {
                                String ПерваяСтрочкаЗначения = stringStringEntry.getValue();
                                Log.i(this.getClass().getName(), "  Ndoc  ПерваяСтрочкаЗначения " + ПерваяСтрочкаЗначения);
                                holder.textViewНазваниеDivice.setText(ПерваяСтрочкаЗначения);
                                holder.textViewНазваниеDivice.requestLayout();
                                holder.textViewНазваниеDivice.forceLayout();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }

            }

            @Override
            public long getItemId(int position) {
                // TODO: 04.03.2022
                Log.i(this.getClass().getName(), "     getItemId holder.position " + position);
                return super.getItemId(position);

            }

            @Override
            public int getItemCount() {
                int КоличесвоСтрок = 0;
                try {
                    if (ArrayListДанныеОтСканироваиниеДивайсов.size() > 0) {
                        КоличесвоСтрок = ArrayListДанныеОтСканироваиниеДивайсов.size();
                        Log.d(this.getClass().getName(), "ArrayListДанныеОтСканироваиниеДивайсов " + ArrayListДанныеОтСканироваиниеДивайсов + " КоличесвоСтрок " + КоличесвоСтрок);
                    } else {
                        КоличесвоСтрок = 1;
                        Log.d(this.getClass().getName(), "ArrayListДанныеОтСканироваиниеДивайсов "
                                + ArrayListДанныеОтСканироваиниеДивайсов + " КоличесвоСтрок" + КоличесвоСтрок);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                    ///метод запись ошибок в таблицу
                    Log.e(getContext().getClass().getName(),
                            "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                    " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                }
                return КоличесвоСтрок;
            }
        }

        //TODO метод делает callback с ответом на экран
        private void МетодПерегрузкаRecyceView() {
            try {
                bottomNavigationViewДляСканированияBluetooth.requestLayout();
                bottomNavigationViewДляСканированияBluetooth.forceLayout();
                recyclerView.requestLayout();
                recyclerView.forceLayout();
                linearLayou.requestLayout();
                linearLayou.forceLayout();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }

        }


        private void МетодBluetoolthBroadReceivier(Activity activity) {
            try {
                BroadcastReceiver broadcastReceiverСканДивайсовBlueTooth = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String actionn = intent.getAction();
                        if (BluetoothDevice.ACTION_FOUND.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }
                        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }

                        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                            onResume();
                        }
                        if (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }
                        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }
                        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }
                        if (         BluetoothAdapter.ACTION_STATE_CHANGED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }
                        if (          BluetoothDevice.ACTION_ACL_CONNECTED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }

                        if (       BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(actionn)) {
                            МетодПолучениеДанныхПослеСканированиеBluetooth(intent);
                            Log.i(context.getClass().getName(), "    onServiceConnected  service_Async_СинхронизацияОБЩАЯ1С");
                        }

                    }


                };
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter(BluetoothDevice.ACTION_UUID));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter( BluetoothAdapter.ACTION_STATE_CHANGED));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter( BluetoothDevice.ACTION_ACL_CONNECTED));
                activity.registerReceiver(broadcastReceiverСканДивайсовBlueTooth, new IntentFilter(   BluetoothDevice.ACTION_ACL_DISCONNECTED));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }
        // TODO: 18.09.2022
        private void МетодПолучениеДанныхПослеСканированиеBluetooth( @NonNull Intent intent) {
            try {
                BluetoothDevice bluetoothDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bluetoothDevice != null) {
                    String названиеУстройствоBluetool=intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                    // UUID uuidУстройствоBluetool= UUID.fromString(intent.getStringExtra(BluetoothDevice.EXTRA_UUID));
                    // Boolean СтатусПолучилосьСвязятьДваДивайса = bluetoothDevice.createBond();
                    Log.d(this.getClass().getName()," bluetoothDevice.getDevice().getAddress() " +bluetoothDevice.getAddress()  +
                            "bluetoothDevice.getDevice().getName()"+bluetoothDevice.getName()+
                            " названиеУстройствоBluetool " +названиеУстройствоBluetool);
                    //  Boolean СтатусПолученияUUIDОбщегоДЛяДвуУстройств=  bluetoothDevice.fetchUuidsWithSdp();
                    //      if(bluetoothDevice.getName().toString().matches("(.*)Redmi(.*)")){
                    Log.i(context.getClass().getName(), "   НАшли " +bluetoothDevice.getName()+ " bluetoothDevice " +bluetoothDevice.getAddress()+
                            "            bluetoothDevice.getUuids()  "+           bluetoothDevice.getUuids() );

  Toast.makeText(getContext(), " НАШЛИ " +bluetoothDevice.getName()+ " bluetoothDevice " +bluetoothDevice.getAddress()+
                            "            bluetoothDevice.getUuids()  "+           bluetoothDevice.getUuids()
                            + "\n", Toast.LENGTH_LONG).show();


                    if(bluetoothDevice.getAddress()!=null){
                        МетодЗаполениеДаннымиDivice(bluetoothDevice);
                        Log.i(context.getClass().getName(), "   ArrayListДанныеОтСканироваиниеДивайсов " +ArrayListДанныеОтСканироваиниеДивайсов);
                    }
                }else{
// TODO: 20.09.2022 вторая часть сканирование
                    String[] АдресаBluetoothСерверов = {"BC:61:93:E6:E2:63","FF:19:99:79:D6:D4"};
                    for (int i = 0; i < АдресаBluetoothСерверов.length; i++) {
                        BluetoothDevice deviceтестовый = adapter.getRemoteDevice(АдресаBluetoothСерверов[i]);//"FF:19:99:79:D6:D4"
                        UUID uuid=        ParcelUuid.fromString("00000000-0000-1000-8000-00805f9b34fb").getUuid();
                        Log.d(this.getClass().getName()," bluetoothDevice " +bluetoothDevice  + "uuid "+uuid );
                        BluetoothSocket bluetoothSocket=deviceтестовый.createRfcommSocketToServiceRecord(uuid);
                        Log.d(this.getClass().getName()," deviceтестовый "+ bluetoothSocket + "uuid "+uuid );
                        try {
                            bluetoothSocket.connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        }
                        BluetoothDevice     bluetoothDeviceВторая = null;
                        if ( bluetoothSocket.isConnected()) {
                            bluetoothDeviceВторая=      bluetoothSocket.getRemoteDevice();
                            Log.d(this.getClass().getName(),"  isConnected Успешный Коннеткт deviceтестовый "+ bluetoothSocket + "uuid "+uuid+ " bluetoothDeviceВторая "+bluetoothDeviceВторая );
                        }else {
                            Log.d(this.getClass().getName()," deviceтестовый "+ bluetoothSocket + "uuid "+uuid + " bluetoothDeviceВторая "+bluetoothDeviceВторая);
                        }
                        bluetoothSocket.close();
                        Log.d(this.getClass().getName()," deviceтестовый "+ bluetoothSocket + "uuid "+uuid );
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }

        private void МетодЗаполениеДаннымиDivice(@NotNull BluetoothDevice bluetoothDevice) {
            try{
                if (bluetoothDevice!=null) {
                    Optional<Map.Entry<String, String>> ПроверкавХэщЕслиТакойКлючУКже=
                            ХэшДанныхПроверкаЕслиУже.entrySet()
                                    .stream()
                                    .filter(e->e.getKey()
                                            .equalsIgnoreCase(bluetoothDevice.getAddress().toString())).findFirst();   // ХэшДааныеСтрокаЗаполениеДанных.containsKey(bluetoothDevice.getAddress().toString());
                    if (ПроверкавХэщЕслиТакойКлючУКже.isPresent()==false) {
                        String НазваниеДивайса= Optional.ofNullable(bluetoothDevice.getName()).map(String::new).orElse("").trim();
                        ConcurrentSkipListMap<String, String>         ХэшДааныеСтрокаТолькоВставка= new ConcurrentSkipListMap<>();
                        // TODO: 20.09.2022  для праверки
                        ХэшДанныхПроверкаЕслиУже.putIfAbsent(bluetoothDevice.getAddress().toString(),НазваниеДивайса);
                        // TODO: 20.09.2022 само заполение
                        ХэшДааныеСтрокаТолькоВставка.putIfAbsent(bluetoothDevice.getAddress().toString(),НазваниеДивайса);
                        Log.d(this.getClass().getName(), "ХэшДааныеСтрокаЗаполениеДанных " + ХэшДааныеСтрокаТолькоВставка + " ХэшДааныеСтрокаТолькоВставка " +ХэшДааныеСтрокаТолькоВставка);
                        ArrayListДанныеОтСканироваиниеДивайсов.add(ХэшДааныеСтрокаТолькоВставка);
                        // TODO: 19.09.2022
                        Message message = new Message();
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("ArrayListДанныеОтСканироваиниеДивайсов",ArrayListДанныеОтСканироваиниеДивайсов);
                        message.setData(bundle);
                        handlerСканирование.sendMessage(message);
                        //  adapter.cancelDiscovery();
                    }else {
                        Log.d(this.getClass().getName(), "ХэшДааныеСтрока " + ХэшДааныеСтрока);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e(getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        }

        void МетодCallBack(){

            handlerСканирование=          new Handler(Looper.getMainLooper(),new Handler.Callback(){
                @Override
                public boolean handleMessage(@NonNull android.os.Message  msg) {

                    try{
                        onResume();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                        ///метод запись ошибок в таблицу
                        Log.e(getContext().getClass().getName(),
                                "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                        " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());
                    }
                    return true;
                }


            });
        }
        // TODO: 05.09.2022  тест метод
        void МетодСканированиеBluetooht(@NonNull Context context, @NonNull BluetoothAdapter adapter) {
            try {
                Log.d(this.getClass().getName(), "МетодСканированиеBluetooht: ");
                if (adapter != null) {
                    if (!adapter.isDiscovering()) {
                        adapter.startDiscovery();
                    }
                    scanner = adapter.getBluetoothLeScanner();
                    List<ScanFilter> scanFilters = new ArrayList<>();
                    ScanSettings scanSettings = new ScanSettings.Builder()
                            .setReportDelay(10000)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
                            .setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
                    scanCallback = new ScanCallback() {
                        @Override
                        public void onScanResult(int callbackType, ScanResult result) {
                            super.onScanResult(callbackType, result);
                            String MAcBluetoots = result.getDevice().getAddress();
                            Log.d(this.getClass().getName(), "result" + result + " MAcBluetoots " + MAcBluetoots);
                            BluetoothDevice deviceтестовый = adapter.getRemoteDevice(MAcBluetoots);
                            // UUID muuid = result.getDevice().getUuids()[result.getDevice().getUuids().length-1].getUuid();
                            Log.d(this.getClass().getName(), "result" + result + " deviceтестовый " + deviceтестовый.getName());
                        }

                        @Override
                        public void onBatchScanResults(List<ScanResult> results) {
                            super.onBatchScanResults(results);
                            results.forEach(new Consumer<ScanResult>() {
                                @Override
                                public void accept(ScanResult scanResult) {
                                    BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(scanResult.getDevice().getAddress());
                                    Log.d(this.getClass().getName(), " scanResult.getDevice().getAddress() " + scanResult.getDevice().getAddress()
                                            + "scanResult.getDevice().getName()" + scanResult.getDevice().getName());
                                    if(bluetoothDevice!=null){
                                        МетодЗаполениеДаннымиDivice(bluetoothDevice);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onScanFailed(int errorCode) {

                            super.onScanFailed(errorCode);
                        }
                    };
                    scanner.startScan(scanFilters, scanSettings, scanCallback);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ///метод запись ошибок в таблицу
                System.err.println("  Ошибка в самом классе записи ошибок нет КОНТЕКСТА Class_Generation_Errors");
                ///метод запись ошибок в таблицу
                Log.e( getContext().getClass().getName(),
                        "Ошибка " + e + " Метод :" + Thread.currentThread().getStackTrace()[2].getMethodName() +
                                " Линия  :" + Thread.currentThread().getStackTrace()[2].getLineNumber());

            }
        }





    }




}






























*/

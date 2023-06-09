package com.example.spex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.spex.databinding.ActivityConnectthreadBinding;
import com.example.spex.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import pl.droidsonroids.gif.GifImageView;

public class connectthread extends AppCompatActivity {
    String TAG = "Test";

    public Button connect, getdevice,on,off;

    int count=0;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    public BluetoothSocket bluetoothSocket;
    IntentFilter intentFilter;
    InputStream inputStream;
    OutputStream outputStream;
    connectthread.RxThread rxThread;

    String rxdata="";

    private AnimationDrawable animDrawable;

    private TextView welcome,name;
    Calendar calendar = Calendar.getInstance();
    public Boolean connected=false,alarmon=false;

    Fragment fragment ;



    ActivityConnectthreadBinding binding;

    GifImageView gif;




    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectthread);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ConstraintLayout constraintLayout = findViewById(R.id.homeLayout);
        animDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();
        gif = (GifImageView) findViewById(R.id.alaramgif);





        binding = ActivityConnectthreadBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        Set<String> name2 = sharedPreferences.getStringSet("name", new LinkedHashSet<>());

        String[] array = name2.toArray(new String[0]);
        System.out.println(array[0]);
        String person = array[0];

        Fragment fragment = new MapFragment();


        binding.bottomnav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    Intent intent = new Intent(connectthread.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.map:

                    if (count>0){
                        Toast.makeText(getApplicationContext(),"Failed to Fetch GPS DATA , No Satelittes",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                    count=1;
                    break;


            }
            return true;

        });

        intentFilter = new IntentFilter();


        //buttons
        connect = findViewById(R.id.connect);
        on=findViewById(R.id.alarm);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        registerReceiver(receiver, intentFilter);
        //connect.setEnabled(false);

        rxThread= new connectthread.RxThread();

        //Welcoming code

        welcome = (TextView) findViewById(R.id.welcome);

        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            welcome.setText("Good Morning,");
            name = (TextView) findViewById(R.id.nametext);
            name.setText(person);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            welcome.setText("Good Afternoon,");
            name = (TextView) findViewById(R.id.nametext);
            name.setText(person);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            welcome.setText("Good Evening,");
            name = (TextView) findViewById(R.id.nametext);
            name.setText(person);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            welcome.setText("Good Night,");
            name = (TextView) findViewById(R.id.nametext);
            name.setText(person);
        }







        connect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {


                try {
                    @SuppressLint("MissingPermission") Set<BluetoothDevice> devices =bluetoothAdapter.getBondedDevices();

                    for (BluetoothDevice dev:devices){
                        System.out.println(3);
                        if(dev.getName().equals("HC-05")){
                            System.out.println(5);
                            bluetoothDevice = dev;
                            bluetoothAdapter.cancelDiscovery();
                            connect.setEnabled(true);
                            break;
                        }
                    }
                   try {
                       bluetoothSocket =bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                       bluetoothSocket.connect();
                   }catch (Exception e){
                       if (!bluetoothAdapter.isEnabled()) {
                           Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                           int REQUEST_ENABLE_BT = 0;
                           startActivityForResult(enableBtIntent,  REQUEST_ENABLE_BT);
                       }
                       Toast.makeText(getApplicationContext(),"Can't Connect Make sure the device is nearby/on",Toast.LENGTH_SHORT).show();
                       return;
                   }

                        inputStream=bluetoothSocket.getInputStream();
                        outputStream=bluetoothSocket.getOutputStream();
                        rxThread.start();
                        Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                        connected=true;

                        connect.setText("Connected...");
                        connect.setEnabled(false);




                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });




        on.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(!alarmon) {
                    if (connected) {
                        try {
                            outputStream.write(65);
                            on.setText("Stop");
                           gif.setVisibility(View.VISIBLE);
                            alarmon=true;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "First Connect with the Bluetooth Device", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        outputStream.write(66);
                        on.setText("BuZZ");
                        gif.setVisibility(View.INVISIBLE);
                        alarmon=false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });






    }




    class RxThread extends Thread{
        public boolean isrunning;
        byte[] rx;
        RxThread(){
            isrunning=false;
            rx = new byte[10];
        }
        @Override
        public void run(){
            while (isrunning){
                try {
                    if (inputStream.available()>2){
                        inputStream.read(rx);
                    }
                }catch (Exception e){

                }

            }
        }
    }



    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            connect.setEnabled(true);
                        }
                    });
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    rxThread.isrunning=false;
                    break;

            }
        }
    };
}
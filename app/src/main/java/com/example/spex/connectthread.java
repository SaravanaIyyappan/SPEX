package com.example.spex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class connectthread extends AppCompatActivity {
    String TAG = "Test";

    public Button connect, getdevice,on,off;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    public BluetoothSocket bluetoothSocket;
    IntentFilter intentFilter;
    InputStream inputStream;
    OutputStream outputStream;
    connectthread.RxThread rxThread;

    String rxdata="";



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectthread);
        ConstraintLayout constraintLayout = findViewById(R.id.homeLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.start();


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
                    System.out.println(0);
                    bluetoothSocket =bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    System.out.println(9);
                    bluetoothSocket.connect();

                    inputStream=bluetoothSocket.getInputStream();
                    outputStream=bluetoothSocket.getOutputStream();
                    rxThread.start();
                    Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                    connect.setText("Connected...");
                    connect.setEnabled(false);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });




        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    outputStream.write(65);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });






    }

    public void send(int i){
        try {
            outputStream.write(i);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                            System.out.println(2);
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
package com.example.spex;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Homepage extends AppCompatActivity {

    private Button alarm;

    private BluetoothSocket socket;
    OutputStream outputStream;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        /*buttons
        alarm=findViewById(R.id.alarm);

        connectthread cn = new connectthread();
        socket = cn.bluetoothSocket;

        alarm.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                try {
                    outputStream= socket.getOutputStream();
                    outputStream.write(65);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });


*/

    }
}
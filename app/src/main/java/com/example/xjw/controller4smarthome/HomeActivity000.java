package com.example.xjw.controller4smarthome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HomeActivity000 extends AppCompatActivity
{
    private final static String TAG = "HomeActivity";
    private Button btn_connect;
    private Button btn_send;
    private EditText edit_receive;
    private boolean isConnected = false;
    private EditText edit_send;
    Socket socket = null;
    private String line;
    BufferedWriter writer = null;
    BufferedReader reader = null;
    private String ipAddr = "192.168.0.112";          //your ip
    private int port = 12306;                        //your port
    String dis = "Disconnect";
    String con = "Connect";
    @Override
    protected void onCreate(Bundle savedInstanceStage)
    {
        super.onCreate(savedInstanceStage);
        setContentView(R.layout.activity_home);

        btn_connect = findViewById(R.id.btn_connect);
        btn_send =  findViewById(R.id.btn_send);
        edit_send = findViewById(R.id.edit_send);
        edit_receive = findViewById(R.id.edit_receive);

        btn_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                connect();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                send();
            }
        });

    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            edit_receive.append(line);
        }
    };

    @SuppressLint("HandlerLeak")
    public void connect()
    {
        if (!isConnected)
        {
            new Thread()
            {
                public void run()
                {
                    try {
                        socket = new Socket(ipAddr, port);
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        new Thread()
                        {
                            public void run()
                            {

                                Log.i(TAG, "66666666666");
                                while (true)
                                {
                                    try
                                    {
                                        if (isConnected)
                                        {
                                            char[] buf = new char[2048];
                                            int i;
                                            while((i= reader.read(buf,0,100))!=-1)
                                            {
                                                line = new String(buf,0,i);
                                                Message msg = handler.obtainMessage();
                                                msg.obj = line;
                                                handler.sendMessage(msg);
                                            }
                                        }
                                    }
                                    catch (IOException e)
                                    {
                                        Toast.makeText(HomeActivity000.this,"error",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        isConnected = false;
                                    }

                                }
                            }
                        }.start();
                    }

                    catch (IOException e)
                    {
                        Toast.makeText(HomeActivity000.this,"error ",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        isConnected = false;
                    }

                }
            }.start();
            isConnected = true;
            btn_connect.setText(dis);
            Toast.makeText(HomeActivity000.this,"connected successfully",Toast.LENGTH_SHORT).show();
        }
        else
        {
            isConnected = false;
            btn_connect.setText(con);
            edit_send.setText("");
            onDestroy();
            Toast.makeText(HomeActivity000.this,"disconnected",Toast.LENGTH_SHORT).show();
        }


    }

    public void send() {
        try {
            Log.i(TAG, "11111111111111");
            writer.write(edit_send.getText().toString()+"\n");
            Log.i(TAG, "22222222222");
            writer.flush();
            Log.i(TAG, "33333333333");
            edit_send.setText("");
            Log.i(TAG, "444444444444");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if(null != socket){
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.getInputStream().close();
                socket.getOutputStream().close();
                socket.close();
            }
        } catch (IOException e) {
            Log.d(TAG,e.getMessage());
        }
        btn_connect.setText(con);
        super.onDestroy();
    }
}

package com.example.xjw.controller4smarthome;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(
                new Runnable()
        {
            @Override
            public void run()
            {
                Intent toHttp = new Intent(WelcomeActivity.this, HttpActivity.class);
                startActivity(toHttp);
                WelcomeActivity.this.finish();
                finish();
            }
        },2000);
    }
}

package com.example.tim.exjobb3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {
    //bool för att hålla koll på om Activiteten körs
    static boolean active = false;

    //då aktiviteten startar setts active till sannt
    @Override
    public void onStart()
    {
        super.onStart();
        active = true;
    }

    //då aktiviteten avslutas setts active till falskt
    @Override
    public void onStop()
    {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button a_buttonSecond1 = (Button) findViewById(R.id.buttonSecond1);

        a_buttonSecond1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });
    }
}

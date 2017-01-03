package com.example.marek.zst3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView mibImage = (ImageView) findViewById(R.id.imageView6);
        final TextView t = (TextView) findViewById(R.id.textViewOID);

        mibImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    //Start of debug
                    Snackbar.make(v, "Y: " + event.getY() + " X: " + event.getX(), Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
                    //EndOfdebug

                    String input = "o" + (int) Math.floor(event.getY()/80);
                    String output = null;
                    try {
                        output = getString(getResources().getIdentifier(input, "string", getPackageName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(output != null)
                        t.setText(output);
                }
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Searching for OID.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}


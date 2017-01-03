package com.example.marek.zst3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPrefs;
    Socket socket;
    //OutputStream out;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView mibImage = (ImageView) findViewById(R.id.imageView6);
        final TextView t = (TextView) findViewById(R.id.textViewOID);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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

                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        String modifiedSentence;
                        try {
                        BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
                        Socket clientSocket = new Socket(sharedPrefs.getString("pref_key_connection_address", "1.1.1.1"), Integer.parseInt(sharedPrefs.getString("pref_key_connection_port", "9999")));
                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String sentence = "connection with android successful";
                        outToServer.writeBytes(sentence + '\n');
                        //modifiedSentence = inFromServer.readLine();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                }.execute();

//                try {
//                    socket = new Socket("192.168.2.106", 6668);
//                    //out = socket.getOutputStream();
//                    out = new PrintWriter(socket.getOutputStream(), true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                RequestModel r = new RequestModel();

                r.setAdress(sharedPrefs.getString("pref_key_connection_address", "127.0.0.1"));
//                r.setPort(sharedPrefs.getString("pref_key_connection_port", "0"));
//                r.setCommunity(sharedPrefs.getString("pref_key_connection_community", "0"));
//                r.setSnmpVersion(sharedPrefs.getString("pref_key_snmp_version", "0"));
//                Snackbar.make(view, r.getAdress(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                if(t.getText().charAt(t.getText().length() - 1) == '0')
//                {
//                    r.setHeader(Header.GET);
//                    r.setOid(t.getText().toString());
//                }
//                try {
//                    out.write(r.getAdress());
//                    socket.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //if(menu.get == R.id.action_settings)

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent modifySettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(modifySettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


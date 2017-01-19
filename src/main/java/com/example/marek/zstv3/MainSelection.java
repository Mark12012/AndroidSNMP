package com.example.marek.zstv3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainSelection extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<String> listFolders;
    HashMap<String, List<String>> listChild;
    private int lastExpandedPosition = -1;
    private ExpandableListView expandableList;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private TextView textTitle;
    private TextView textContent;
    private String currentName;
    private String currentOid;
    private int currentIndex;
    private SharedPreferences sharedPrefs;
    private Socket socket;
    public Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        textTitle = (TextView) findViewById(R.id.textView2);
        textContent = (TextView) findViewById(R.id.textContent);
        expandableList = (ExpandableListView) findViewById(R.id.expandableList);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        prepareConnection();
        prepareListData();
        ListAdapter mAdapter = new ListAdapter(this, listFolders, listChild);
        expandableList.setAdapter(mAdapter);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///LISTENERY

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "GetNext on: " + currentName, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                StringCreator sc = new StringCreator();
                if(currentOid == null)
                    currentOid = "1.3.6.1.2.1.1.1";
                connection.sendMessage(sc.createQuerry("GETNEXT", currentOid, sharedPrefs));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //String[] response = connection.getRecieveMessage().split("/");
                //currentOid = response[0];
                textContent.setText(connection.getRecieveMessage());

            }
        });

        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
                //monitoringStop();
                String name = listChild.get(listFolders.get(groupPosition)).get(childPosition);
                String oid = getResources().getString(getResources().getIdentifier(name,"string",getPackageName()));
                currentOid = oid;

                StringCreator sc = new StringCreator();
                connection.sendMessage(sc.createQuerry("GET", currentOid, sharedPrefs));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //String[] response = connection.getRecieveMessage().split("/");
//                if(!(currentOid + ".0").equals(response[0]))
//                {
//                    currentOid = response[0];
//                    textTitle.setText(currentOid);
//                }

                textContent.setText(connection.getRecieveMessage());

                currentName = name;
                currentOid = oid;
                textTitle.setText(name);
                drawer.closeDrawers();

                if(sharedPrefs.getBoolean("pref_key_monitoring", false))
                {
                    //If monitoring is turned on start thread to monitor change
                    Snackbar.make(v, name + " - " + oid + " true", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }

                return false;
            }
        });


    }

    @SuppressWarnings("unchecked")

    protected void onProgressUpdate(String item) {
        setContentText(item);
    }


    protected void onPostExecute(Void unused) {
        Toast
                .makeText(MainSelection.this, "Done!", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent modifySettings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(modifySettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setContentText(String s)
    {
        textContent.setText(s);
    }

    ///Kod Michała
    private void prepareListData() {

        listFolders = new ArrayList<>();
        listChild = new HashMap<>();

        // Adding child data
        listFolders.add("System");
        listFolders.add("Interfaces");
        listFolders.add("Ip");
        listFolders.add("Icmp");
        listFolders.add("Tcp");
        listFolders.add("Udp");
        listFolders.add("Snmp");
        listFolders.add("Host");

        List<String> system = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.System)));
        List<String> interfaces = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Interfaces)));
        List<String> ip = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Ip)));
        List<String> icmp = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Icmp)));
        List<String> tcp = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Tcp)));
        List<String> udp = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Udp)));
        List<String> snmp = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Snmp)));
        List<String> host = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Host)));

        listChild.put(listFolders.get(0), system);
        listChild.put(listFolders.get(1), interfaces);
        listChild.put(listFolders.get(2), ip);
        listChild.put(listFolders.get(3), icmp);
        listChild.put(listFolders.get(4), tcp);
        listChild.put(listFolders.get(5), udp);
        listChild.put(listFolders.get(6), snmp);
        listChild.put(listFolders.get(7), host);
    }

    private void prepareConnection()
    {
        connection = new Connection(sharedPrefs.getString("pref_key_connection_address","192.168.2.103"), Integer.parseInt(sharedPrefs.getString("pref_key_connection_port", "9999")), textContent);
        connection.execute();
    }

    //wątek z socketem
    class ClientThread implements Runnable
    {
        public void run()
        {
            //TODO odczyt IP z ustawien
            String hostname = "192.168.2.106";
            try {
                InetAddress address = InetAddress.getByName(hostname);
                socket = new Socket(address, 6668);
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

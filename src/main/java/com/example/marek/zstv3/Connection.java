package com.example.marek.zstv3;

import android.util.Log;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintStream;

/**
 * Created by Marek on 2017-01-18.
 */

public class Connection extends AsyncTask<String, String, String> {

    private PrintStream writer;
    private InputStream br;
    private String dstAddress;
    private int dstPort;
    private String receive_message = null;
    public boolean isConnected = false;

    private static Connection instance = null;
    protected Connection() {
        // Exists only to defeat instantiation.
    }
    public static Connection getInstance() {
        if(instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    Connection(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
        Log.i("BBBB","Connection");

    }

    @Override
    protected String doInBackground(String... params)
    {
        Socket socket = null;
        try
        {
            Log.i("BBB", "Proba polaczenia z" + dstAddress + " na " + dstPort);
            socket = new Socket(dstAddress, dstPort);
            Log.i("BBB", "Poloczono");
            MainSelection.connected = true;
            isConnected = true;
            writer = new PrintStream(socket.getOutputStream());
            br = socket.getInputStream();
            //sendMessage("hi pc");

            byte[] buffer = new byte[4096];
            int read = br.read(buffer, 0, 4096); //This is blocking
            while(read != -1){
                byte[] tempdata = new byte[read];

                System.arraycopy(buffer, 0, tempdata, 0, read);
                receive_message = new String(tempdata);
                Log.i("AsyncTask", receive_message);

                read = br.read(buffer, 0, 4096); //This is blocking
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally
        {
            if (socket != null)
            {
                try {
                    writer.flush();
                    writer.close();
                    br.close();
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public void sendMessage(String oid)
    {
        writer.println(oid);
        writer.flush();
        Log.i("BBB", "wysylanie");
    }
    public String getRecieveMessage()
    {
        return receive_message;
    }
    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Log.e("007", "Something failed!");
        } else {
            Log.d("OO7", "In on post execute");
            super.onPostExecute(result);
        }
        MainSelection.connected = false;
        MainSelection.isFirst = true;
    }
}


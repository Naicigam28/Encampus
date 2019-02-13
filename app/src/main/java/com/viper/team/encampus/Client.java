package com.viper.team.encampus;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends AsyncTask {
    private TextView name;
    private String username;
    private TextView pass;
    private Context context;
    private boolean connected;
    private int command;
    private String ip = "192.168.42.76";
    Bitmap[] bmp;
    //Socket socket;
    //Activity main;
    String[] courseList;
    String[] campusList;
    String[] data;
    LinearLayout marketContent;

    public Client(int command) {
        this.connected = false;
        this.command = command;
    }

    public Client(LinearLayout layout, int command) {
        this.marketContent = layout;
        this.connected = false;
        this.command = command;
    }

    String sql;

    public Client(String username, String password, String firstName, String lastName, String studID, String email, String course, String campus, int command) {
        data = new String[]{username, password, firstName, lastName, studID, email, course, campus};
        this.sql = "values ('" + username + "','" + firstName + "','" + lastName + "','" + email + "','" + course + "','" + campus + "','" + password + "','" + studID + "')";
        this.connected = false;
        this.command = command;
    }

    public Client(TextView name, TextView pass, int command) {
        this.name = name;
        this.pass = pass;
        this.username = name.getText() + "";
        this.context = context;
        this.connected = false;
        this.command = command;
    }

    boolean isConnected() {
        return this.connected;
    }

    boolean loggedIn = false;

    boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        // ProgressDialog progressDialog = ProgressDialog.show(MainActivity.getContext(), "Connecting to server","Sending Details", true);
        switch (command) {
            case 0:

                Log.i("Connecting...", " Opening connection");


                try {
                    Socket socket = new Socket(ip, 5000);

                    Scanner recv = new Scanner(socket.getInputStream());
                    PrintStream send = new PrintStream(socket.getOutputStream());
                    String s;
                    send.println(0);
                    send.println(name.getText());
                    send.println(pass.getText());
                    String con = recv.nextLine();
                    if (socket.isConnected() && con.compareTo("1") == 0) {
                        connected = true;
                        loggedIn = true;
                        send.println(1);
                    } else {

                        Log.i("Error", "Not connected");
                        connected = false;
                        loggedIn = false;
                    }
                    socket.close();
                } catch (Exception e) {
                    Log.i("Error", e + "");
                }

                break;

            case 2:
                Log.i("Connecting...", "loading market place");
                try {
                    Socket socket = new Socket(ip, 5000);

                    Scanner recv = new Scanner(socket.getInputStream());
                    PrintStream send = new PrintStream(socket.getOutputStream());


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Error", e + "");
                }
                break;

            case 3:
                Log.i("Connecting...", "loading signup page");

                Log.i("TEST", "01  -------------");
                try {

                    Socket socket = new Socket(ip, 5000);
                    if (socket.isConnected()) {
                        Log.i("TEST", "02  -------------");
                        Scanner recv = new Scanner(socket.getInputStream());
                        PrintStream send = new PrintStream(socket.getOutputStream());
                        Log.i("TEST", "03  -------------");
                        send.println(3);
                        Log.i("TEST", "04  -------------");
                        int len = recv.nextInt();
                        Log.i("TEST", "05  -------------");
                        campusList = new String[len];
                        for (int i = 0; i < len; i++) {
                            campusList[i] = recv.nextLine();
                            Log.d("Test", campusList[i]);
                        }
                        int x = 8;
                        String[] courseList = new String[x];
                        for (int i = 0; i < courseList.length; i++) {
                            courseList[i] = recv.nextLine();
                        }
                        connected = true;
                    } else Log.i("Error", "Not connect");
                    socket.close();
                    socket = null;


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Error", e + "");
                }

                break;

            case 4:
                try {
                    Socket socket = new Socket(ip, 5000);
                    if (socket.isConnected()) {
                        Scanner recv = new Scanner(socket.getInputStream());
                        PrintStream send = new PrintStream(socket.getOutputStream());
                        send.println(4);

                        //Log.i("DATA+", sql);
                        send.println(sql);
                        send.close();
                    }


                    //socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                connected = true;
                break;

            case 5:
                try {
                    Socket socket = new Socket(ip, 5000);
                    Scanner recv = new Scanner(socket.getInputStream());
                    InputStream inputStream = socket.getInputStream();
                    PrintStream send = new PrintStream(socket.getOutputStream());
                    send.println(5);
                    //int size = recv.nextInt();
                    Log.i("TEST", "100");


                    Books = recv.nextLine().replaceAll("\\[", " ").replaceAll("\\]"," ").split(",");

                    Log.i("TEST", "200");
                    connected = true;

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


        }

        return null;
    }

    String[] Books;

    String[] getBooks() {
        return Books;
    }

    String[] getCampusList() {
        return campusList;
    }
}

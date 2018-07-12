package com.example.razafaraz.newapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ListView list;
    String[] strings = {"1" , "2","3","4"};
    List<String> listContent;
    ArrayAdapter adapter;

    EditText editText_Name;
    Button btn_Send;

    int last_Item = 0;
    String name = "Helloo";
    int id;
    String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView)findViewById(R.id.dataListView);
        editText_Name = (EditText)findViewById(R.id.editText_JSON_Name);
        btn_Send = (Button)findViewById(R.id.button_send_JSON);
        Button delete_Btn = (Button)findViewById(R.id.button_delete_JSON);
        AsyncTaskExecute();

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AyscDataToServer();
                Snackbar.make(view, "Sucessfull Action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        delete_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsycDelete();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    void AsycDelete()
    {
        AsyncTask<Void,Void,Boolean> execute=new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                int pos = 0;

                try {
                    URL url = new URL("http://192.168.1.10/nrsp/sendPost.php");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 );
                    conn.setRequestMethod("DELETE");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                   // conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Customer_ID",33);
                    String message = jsonObject.toString();

                    Log.e("Objecttttt val",message.toString());

                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();

                    //JSONObject jsonObject = new JSONObject();
                    //pos = adapter.getPosition(adapter.getCount() - 1);
                    Log.e("indes is ",  Integer.toString(last_Item));

                    is = conn.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(is);

                    String data="";
                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;

                    }
                    Log.e("Result", data.toString() );
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.execute();
    }

    void AyscDataToServer()
    {
        AsyncTask<Void,Void,Boolean> execute=new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                String name = editText_Name.getText().toString();
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {

                    //constants
                    URL url = new URL("http://192.168.1.10/nrsp/sendPost.php");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Customer_Name" ,name);
                    String message = jsonObject.toString();
                    Log.e("Objecttttt val",message.toString());

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 );
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    //setup send
                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();

                    //do somehting with response
                    is = conn.getInputStream();
                    is = conn.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(is);

                    String data="";
                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;

                    }
                    Log.e("Result", data.toString() );
                    Log.e("Sending Data Start","Executed");

                } catch (IOException e) {
                    Log.e("IO EXP", e.getMessage());
                } catch (JSONException e) {
                    Log.e("JSON EXP", e.getMessage());
                } finally {
                    //clean up
                    try {
                        os.close();
                        is.close();
                        Log.e("JSON EXP", "closing connection Success");
                    } catch (IOException e) {
                        Log.e("Close Connection EXP", e.getMessage());
                    }
                    conn.disconnect();
                }


                return null;
            }
            protected void onPostExecute(Boolean aBoolean) {

                super.onPostExecute(aBoolean);
                Log.e("TAG", "Done" );

                runOnUiThread(new Runnable(){
                    public void run(){
                        AsyncTaskExecute();
                    }
                });
            }
        }.execute();
    }

    void AsyncTaskExecute()
    {
        AsyncTask<Void,Void,Boolean> execute=new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                list.setAdapter(new ArrayAdapter<String>( ListActivity.this, android.R.layout.simple_list_item_1 , listContent));
                super.onPostExecute(aBoolean);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String login_url = "http://192.168.1.10/nrsp/connect.php";

                try {
                    URL url = new URL(login_url);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Charset","UTF-8");
                    connection.connect();

                    Log.e("Hi2Activity","Start");

                    InputStream stream = new BufferedInputStream( connection.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuilder buffer = new StringBuilder();
                    /*Json array*/
                    /*What is Stringbulder*/

                    String line = "";

                    while((line = reader.readLine()) != null)
                    {
                        buffer.append(line +"\n");
                    }
                    Log.e("Hi2Activity",buffer.toString());
                    JSONArray jsonArray = new JSONArray(buffer.toString());

                    listContent = new ArrayList<String>(jsonArray.length());
                    last_Item = jsonArray.length();
                    for(int i = 0 ; i < jsonArray.length() ; i++)
                    {
                        Log.e("Hi2Activity", "Insidde Array");
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listContent.add(jsonObject.getString("Customer_Name"));
                    }

                    //return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                    try{
                        if(reader != null)
                            reader.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }

                return null;
            }
        } .execute();
    }

}

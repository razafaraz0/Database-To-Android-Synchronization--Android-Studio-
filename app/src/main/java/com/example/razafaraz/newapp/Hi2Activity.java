package com.example.razafaraz.newapp;

import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Hi2Activity extends AppCompatActivity {
    private TextView Name;
    private TextView Description;
    private Button btn;

    int id;
    String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        Name = (TextView) findViewById(R.id.textView_Name2);
        Description = (TextView) findViewById(R.id.textView_Description2);

        btn = (Button) findViewById(R.id.button_id1);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Description.setText("CLCIKED");
                AsyncTaskExecute();
                Log.e("Hi2Activity","hi" );

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    void AsyncTaskExecute()
    {
        AsyncTask <Void,Void,Boolean> execute=new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Name.setText(id+"");
                Description.setText(des);
                super.onPostExecute(aBoolean);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String login_url = "http://192.168.100.24/nrsp/connect.php";

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
                    for(int i = 0 ; i < jsonArray.length() ; i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("Customer_ID");
                        des = jsonObject.getString("Customer_Name");
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


    public class JSONTask extends AsyncTask<URL, String , String>
    {

        @Override
        protected String doInBackground(URL... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String login_url = "http://10.0.2.2/connect.php";

            try {
                URL url = new URL(login_url);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                /*Json array*/
                /*What is Stringbulder*/

                String line = "";

                while((line = reader.readLine()) != null)
                {
                    buffer.append(line +"\n");
                }

                JSONArray jsonArray = new JSONArray(buffer);

                for(int i = 0 ; i < jsonArray.length() ; i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("Customer_ID");
                    String des = jsonObject.getString("Customer_Name");
                }

            return buffer.toString();

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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Name.setText(id);
            Description.setText(des);
        }
    }

}

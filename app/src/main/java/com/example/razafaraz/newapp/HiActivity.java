package com.example.razafaraz.newapp;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HiActivity extends AppCompatActivity {

    EditText text_Box_Name;
    EditText text_Box_Decription;
    Button btn;

    private TextView Name;
    private TextView Description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text_Box_Name = (EditText) findViewById(R.id.plain_text_input_name);
        text_Box_Decription = (EditText) findViewById(R.id.plain_text_input_description);


        Name = (TextView) findViewById(R.id.Name_TextView_ActivityHI);
        Description = (TextView) findViewById(R.id.Description_TextView_ActivityHI);

        btn = (Button) findViewById(R.id.send_Info);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    sendDetails();
                    new JSONTask().execute();
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
    private void sendDetails(){
        String name = text_Box_Name.getText().toString();
        String description = text_Box_Decription.getText().toString();

        Intent intent = new Intent(HiActivity.this , Activity2.class);
        intent.putExtra("NAME" , name );
        intent.putExtra("DESCRIPTION" , description);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hi, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);

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

                }

                JSONObject jsonObject = new JSONObject();
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
            Name.setText(result);
        }
    }
}

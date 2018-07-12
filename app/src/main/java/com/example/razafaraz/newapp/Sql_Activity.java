package com.example.razafaraz.newapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Sql_Activity extends AppCompatActivity {


    EditText name_text;
    Button send_btn, del_btn , sync_btn;

    List<String> listContent;
    ArrayAdapter adapter;
    ListView list;

    dbController controller = new dbController(this);

    ArrayList<HashMap<String, String>> arrayList_Customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name_text = (EditText) findViewById(R.id.editText_sqlite_Name);

        send_btn = (Button) findViewById(R.id.button_sqlite_send);
        del_btn = (Button) findViewById(R.id.button_sqlite_Delete);
        sync_btn = (Button) findViewById(R.id.button_sync);

        list = (ListView) findViewById(R.id.listView_sqlite);

        arrayList_Customer = new ArrayList<HashMap<String, String>>();


        ArrayList<HashMap<String, String>> user_List = controller.getAllUser();

        dbController mydb = new dbController(this.getBaseContext()); //ONLINE IT SAYS .getcontext
        show_Customer(mydb);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_text.getText().toString();
                add_Customer(name);
                show_Customer();
            }
        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_Db();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sync_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean x = false;
                Log.v("TAG" , "CLIKCEDDDD");
                x = isConnected(Sql_Activity.this);

                if(!x)
                {
                    Snackbar.make(view, "No Intenet Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    Snackbar.make(view, "Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //syncMySqlWithSqlLite();
                    syncSQLLiteWithMySQL();

                    show_Customer();
                }

            }
        });
        /*Boolean is_Bol_Connection = false;
        while(!is_Bol_Connection )
        {
            is_Bol_Connection = isConnected(this);
        }*/

    }
    public void show_Customer()
    {
        dbController mydb = new dbController(this.getBaseContext()); //ONLINE IT SAYS .getcontext
        show_Customer(mydb);
    }

    public void clear_Db() {
        dbController mydb = new dbController(this.getBaseContext()); //ONLINE IT SAYS .getcontext
        mydb.deleteAll();
        show_Customer(mydb);
    }

    public void add_Customer(String name) {
        Log.e("added Value ", name);
        long i = 0;

        dbController mydb = new dbController(this.getBaseContext()); //ONLINE IT SAYS .getcontext
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Customer_Name", name);
        map.put("status", "no");
        i = mydb.insertCustomer(map);
        return;
    }

    public void add_Customer( int ID ,  String name) {
        Log.e("added Value ", "Added Customer with ID " + Integer.toString(ID));
        long i = 0;

        dbController mydb = new dbController(this.getBaseContext()); //ONLINE IT SAYS .getcontext
        HashMap<String, String> map = new HashMap<String, String>();


        map.put("Customer_ID" , String.valueOf(ID));
        map.put("Customer_Name", name);
        //map.put("status", "no");
        i = mydb.insertCustomer(map);
        return;
    }

    public void show_Customer(dbController mydb) {
        //ArrayList<HashMap<String, String>> arrayList_Customer = new ArrayList<HashMap<String, String>>();
        arrayList_Customer = mydb.getAllUser();
        HashMap<String, String> map;

        listContent = new ArrayList<String>(arrayList_Customer.size());
        for (int i = 0; i < arrayList_Customer.size(); i++) {
            map = arrayList_Customer.get(i);

            String name = map.get("Customer_Name");
            listContent.add(name.toString());
        }
        list.setAdapter(new ArrayAdapter<String>(Sql_Activity.this, android.R.layout.simple_list_item_1, listContent));

    }

    public boolean checkIFExist(int cus_ID , String cus_String)
    {
        boolean result = false;
        final dbController mydb = new dbController(this.getBaseContext());
        arrayList_Customer = mydb.getAllUser();

        HashMap<String, String> map;
        listContent = new ArrayList<String>(arrayList_Customer.size());

        for(int i = 0 ; i < arrayList_Customer.size() ; i++)
        {
            map = arrayList_Customer.get(i);
            String id = map.get("Customer_ID");
            String name = map.get("Customer_Name");
            Log.e("TAG", "Comparing mysql "+ id.toString() +  "and sqllite " + cus_ID );

            if(id.equals(Integer.toString(cus_ID))) // || cus_String.equals(name)
            {
                return true;
            }
        }

        return result;
    }

    public static boolean isConnected(Context context){
        boolean found=false;
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network=manager.getActiveNetworkInfo();
        if (network!=null && network.isAvailable() && network.isConnected())
            found=true;
        return found;
    }


    //done
    public void syncMySqlWithSqlLite()
    {
        final dbController mydb = new dbController(this.getBaseContext());


        AsyncTask<Void,Void,Boolean> execute=new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                show_Customer();
                //list.setAdapter(new ArrayAdapter<String>( Sql_Activity.this, android.R.layout.simple_list_item_1 , listContent));

                return;
                //super.onPostExecute(aBoolean);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                Log.e("Hi2Activity","Start");
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String login_url = "http://192.168.100.33/nrsp/connect.php";

                try {
                    URL url = new URL(login_url);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Charset","UTF-8");
                    connection.connect();



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
                    String from_db_name;
                    int from_db_id;
                    for(int i = 0 ; i < jsonArray.length() ; i++)
                    {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        from_db_id = jsonObject.getInt("Customer_ID");
                        from_db_name = jsonObject.getString("Customer_Name");
                        Log.e("Hi2Activity", "for ID "+ from_db_id + "name is "+ from_db_name);
                        checkIFExist(from_db_id , from_db_name);
                        if(!checkIFExist(from_db_id , from_db_name))
                        {
                            Log.e("Hi2Activity", "Doesn't exsists");
                            add_Customer(from_db_id, from_db_name);
                            listContent.add(jsonObject.getString("Customer_Name"));
                        }
                        else
                        {
                            Log.e("Hi2Activity", "Already exsists");
                        }

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

        }.execute();

        }

    public void syncSQLLiteWithMySQL()
    {
        final dbController mydb = new dbController(this.getBaseContext());

        AsyncTask<Void,Void,Boolean> execute=new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("http://192.168.100.36/nrsp/sendPost.php");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 );
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();
                    Log.e("Msg","HERE");


                    //ArrayList<HashMap<String, String>> arrayList_Customer;
                    arrayList_Customer = mydb.getAllUnsyncUser();
                    if(arrayList_Customer.size() == 0)
                    {
                        Log.e("Msg","No customer to sunc");
                        return null;
                    }
                    Log.e("Msg","Number of unsynced are :"+ arrayList_Customer.size());
                    HashMap<String, String> map;
                    //initalizing arraylist
                    listContent = new ArrayList<String>(arrayList_Customer.size());



                    //get all unsyned customer
                    //put them in a JSON array
                    // send the JSON array

                    os = new BufferedOutputStream(conn.getOutputStream());
                    JSONArray to_Send = new JSONArray();
                    //JSONObject to_Send = new JSONObject();

                    for (int i = 0; i < arrayList_Customer.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        map = arrayList_Customer.get(i);
                        String name = map.get("Customer_Name");
                        String id = map.get("Customer_ID");

                        jsonObject.put("Customer_ID" , Integer.parseInt(id) );
                        jsonObject.put("Customer_Name" , name.toString());
                        Log.e("Hi2Activity", "For Customer ID : "+ id + " The Name is "+ name);

                        String message = jsonObject.toString();
                        os.write(message.getBytes());
                        to_Send.put(jsonObject);

                       // jsonObject.put("Customer_Name",name.toString());
                        //String message = jsonObject.toString();
                        //Log.e("Msg","THE MESSAGE TO SEND IS "+ message.toString());
                        //os.write(message.getBytes());
                        //os.flush();
                    }
                    /*os.write(Integer.parseInt(to_Send.toString()));*/
                    os.flush();
                    os.close();
                    //Log.e("Customer_Name" , to_Send.toString());

                    //do somehting with response
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

                    //return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(conn != null)
                    {
                        conn.disconnect();
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
        }.execute();

    }

}

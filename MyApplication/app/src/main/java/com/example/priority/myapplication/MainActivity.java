package com.example.priority.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void consultaPedido(View v) throws ExecutionException, InterruptedException, JSONException {
        AsyncTask<Integer, Integer, String> at = new AsyncTask<Integer, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... urls) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://priorityhealth2.herokuapp.com/rest/pedidos/"+urls[0]);
                try {
                    HttpResponse response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }
                return builder.toString();

            }

            @Override
            protected void onProgressUpdate(Integer... progress) {

            }

            @Override
            protected void onPostExecute(String result) {

            }
        };

        EditText editText = (EditText) findViewById(R.id.editText);
        Integer base1 = Integer.parseInt(editText.getText().toString());
        at.execute(base1);
        ArrayList<String> pedidos = new ArrayList<>();
        Spinner tv = (Spinner)findViewById(R.id.spinner);
        String st = at.get();
        JSONArray ja = new JSONArray(st);
        for(int i=0; i<ja.length(); i++){
            JSONObject jo=ja.getJSONObject(i);
            String sd =jo.getString("idPedidos")+"-   "+jo.getString("fechaLlegada");
            pedidos.add(sd);
        }
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this, R.layout.abc_simple_dropdown_hint, pedidos);
        tv.setAdapter(aa);
    }


    public void consultaDetallePedido(View v) throws ExecutionException, InterruptedException, JSONException {
        AsyncTask<Integer, Integer, String> at = new AsyncTask<Integer, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer... urls) {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://priorityhealth2.herokuapp.com/rest/pedidos/detalle/"+urls[0]);
                try {
                    HttpResponse response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }
                return builder.toString();

            }

            @Override
            protected void onProgressUpdate(Integer... progress) {

            }

            @Override
            protected void onPostExecute(String result) {

            }
        };

        Spinner Obj = (Spinner)findViewById(R.id.spinner);
        String numero =Obj.getSelectedItem().toString();
        Integer limite= numero.indexOf("-");
        String code=numero.substring(0,limite);
        Integer base =  Integer.parseInt(code);
        at.execute(base);
        ArrayList<String> DetallePedido = new ArrayList<>();
        ListView pv = (ListView)findViewById(R.id.listView);
        String st = at.get();
        JSONArray ja = new JSONArray(st);
        for(int i=0; i<ja.length(); i++){
            JSONObject jo=ja.getJSONObject(i);
            String sd =jo.getString("idDetalle")+ "-    " + jo.getString("cantidad");
            DetallePedido.add(sd);
        }
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this, R.layout.abc_simple_dropdown_hint, DetallePedido);
        pv.setAdapter(aa);
    }
}

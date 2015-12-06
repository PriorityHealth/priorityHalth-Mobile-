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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
                HttpGet httpGet = new HttpGet("http://priorityhealth2.herokuapp.com/rest/despachos/"+urls[0]);
                //"http://priorityhealth2.herokuapp.com/rest/pedidos/paciente/"+urls[0]
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
        System.out.print(st);
        JSONArray ja = new JSONArray(st);
        for(int i=0; i<ja.length(); i++){
            JSONObject jo=ja.getJSONObject(i);
            String sd =jo.getString("idDespacho")+"-   "+"-   "+jo.getString("estado")+"-   "+jo.getString("numeroPagoCoutaModeradora");
            pedidos.add(sd);
        }
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this, R.layout.abc_simple_dropdown_hint, pedidos);
        tv.setAdapter(aa);
    }


       public void registro(View v) throws JSONException {


        EditText a1=(EditText)findViewById(R.id.editText2);
           Integer a =  new Integer(a1.getText().toString());

        AsyncTask<Integer, Integer, Integer> at = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected Integer doInBackground(Integer... urls) {
                Integer jso=urls[0];

                DefaultHttpClient dhhtpc=new DefaultHttpClient();
                HttpPost postreq;
                postreq = new HttpPost("http://priorityhealth2.herokuapp.com/rest/despachos/despacho");
                //agregar la versiÃ³n textual del documento jSON a la peticiÃ³n
                StringEntity se= null;
                try {
                    se = new StringEntity(jso.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new
                        BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                postreq.setEntity(se);
                //ejecutar la peticiÃ³n
                HttpResponse httpr= null;
                try {
                    httpr = dhhtpc.execute(postreq);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Para obtener la respuesta:
                try {
                    String reqResponse= EntityUtils.toString(httpr.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {

            }

            @Override
            protected void onPostExecute(Integer result) {

            }
        };

        at.execute(a);
    }

}

/*
Dhawala Bhagawat
Niveditha Gopalkrishna
*/

package com.example.newsapp;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> sourceNames = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<Source> result = new ArrayList<>();
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListView= findViewById(R.id.mylist);

        new GetDataAsync().execute("https://newsapi.org/v2/sources?apiKey=5958922d49234bb68678858a24a0c7d4");
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,sourceNames);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Source selectedSource = result.get(i);
                Log.d("Clicked source", result.get(i).toString());
                Intent newsIntent = new Intent(MainActivity.this, NewsActivity.class);
                newsIntent.putExtra("sourceSelected",selectedSource);
                startActivity(newsIntent);
            }
        });





                }



     private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Source>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);

        }


        @Override
        protected ArrayList<Source> doInBackground(String... params) {
            HttpURLConnection connection = null;

            if(isConnected()){
             //   Toast.makeText(MainActivity.this, "Internet Present", Toast.LENGTH_SHORT).show();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray sources = root.getJSONArray("sources");
                    for (int i=0;i<sources.length();i++) {
                        JSONObject personJson = sources.getJSONObject(i);
                        Source source = new Source();
                        source.name = personJson.getString("name");
                        source.id = personJson.getString("id");
                        sourceNames.add(source.name);
                        result.add(source);

                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }}
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Source> result) {
            if (result.size() > 0) {
                Log.d("demo", result.toString());
                findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
                myListView.setAdapter(adapter);
            } else {
                Log.d("demo", "empty result");
            }
        }
    }


}

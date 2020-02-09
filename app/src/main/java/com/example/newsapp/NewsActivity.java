package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

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

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    ArrayList<String> sourceNames = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<News> result = new ArrayList<>();
    ListView myListView;
    ArrayList <list_row> rowItems = new ArrayList<list_row>();;
    public static String[] imageurl;
    public static String[] newsTitle;
    public static String[] newsAuthor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        myListView= findViewById(R.id.newsListView);

        Intent news = getIntent();
        Source source = (Source)news.getExtras().getSerializable("sourceSelected");
        Log.d("id",source.toString());
        setTitle(source.name);
        //https://newsapi.org/v2/top-headlines? abc-news
        //sources=<Source_id>&apiKey=<Your_Key>
        new GetDataAsync().execute("https://newsapi.org/v2/top-headlines?sources="+source.id+"&apiKey=5958922d49234bb68678858a24a0c7d4");
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,sourceNames);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News selectedNews = result.get(i);
                String url = selectedNews.url;
                Log.d("Clicked News url", url);
                Intent webIntent = new Intent(NewsActivity.this, WebviewActivity.class);
                webIntent.putExtra("selectednews", url);

                startActivity(webIntent);

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

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);

        }


        @Override
        protected ArrayList<News> doInBackground(String... params) {
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
                        JSONArray sources = root.getJSONArray("articles");
                        for (int i=0;i<sources.length();i++) {
                            JSONObject personJson = sources.getJSONObject(i);
                            News news = new News();
                            news.author = personJson.getString("author");

                            news.title = personJson.getString("title");

                            news.url = personJson.getString("url");
                            news.urlToImage = personJson.getString("urlToImage");
                           // imageurl[i] = news.urlToImage;
                            news.publishedAt = personJson.getString("publishedAt");
                            sourceNames.add(news.title);

                            result.add(news);

                        }
                        Log.d("News",result.toString());
                       /* for(int i =0;i<result.size();i++) {

                            newsAuthor[i] = result.get(i).author;
                            newsTitle[i] = result.get(i).title;
                            imageurl[i] = result.get(i).urlToImage;

                        }*/
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
        protected void onPostExecute(ArrayList<News> result) {
           /* for(int i =0;i<result.size();i++) {

                newsAuthor[i] = result.get(i).getAuthor();
                newsTitle[i] = result.get(i).getTitle();
                imageurl[i] = result.get(i).getUrlToImage();

            }*/

            if (result.size() > 0) {

                Log.d("demo", result.toString());
                for (int i = 0; i < result.size(); i++) {
                    list_row item = new list_row(result.get(i).getUrlToImage(), result.get(i).getTitle(), result.get(i).getAuthor(), result.get(i).getPublishedAt());
                    rowItems.add(item);
                }
                CustomListViewAdapter adapter = new CustomListViewAdapter(NewsActivity.this,
                        R.layout.list_item, rowItems);
                myListView.setAdapter(adapter);



            // findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
               // myListView.setAdapter(adapter);


            } else {
                Log.d("demo", "empty result");
            }
        }
    }

}

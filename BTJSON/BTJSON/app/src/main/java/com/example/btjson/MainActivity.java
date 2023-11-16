package com.example.btjson;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Product> arrayList = new ArrayList<>();
    Adapter adapter;
    ListView listView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_item);
        new ReadJson().execute("https://dummyjson.com/products");
    }
    private class ReadJson extends AsyncTask<String, Void, String> {
        StringBuilder content = new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line ="";
                while ((line = bufferedReader.readLine())!=null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                JSONArray arrayObject = object.getJSONArray("products");
                for (int i=0; i<arrayObject.length();i++)
                {
                    JSONObject object1 = arrayObject.getJSONObject(i);
                    String title = object1.getString("title");
                    String description = object1.getString("description");
                    arrayList.add(new Product(title, description));
                }
                adapter = new Adapter(getApplicationContext(), arrayList);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            super.onPostExecute(s);
        }
    }
}
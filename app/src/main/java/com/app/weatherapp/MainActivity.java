package com.app.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;
    String city;
    ImageView image ;
    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String key = "f4acb164b5acc4b7b77a48ad4ceee13c";
        listView = (ListView) findViewById(R.id.listview);
        adapter = new WeatherArrayAdapter(this, weatherList);
        listView.setAdapter(adapter);

        image = (ImageView)findViewById(R.id.buttonweather);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.editweather);
                URL url = createUrl(editText.getText().toString());
                if (url != null) {
                    dimisskeyboard(editText);
                    GetWeatherTask getweather = new GetWeatherTask();
                    getweather.execute(url);

                } else {
                    Snackbar.make(findViewById(R.id.action_container), R.string.invalid_url, Snackbar.LENGTH_LONG).show();
                }
            }
        });
      //  button = (Button) findViewById(R.id.buttonweather);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editText = (EditText) findViewById(R.id.editweather);
//                URL url = createUrl(editText.getText().toString());
//                if (url != null) {
//                    dimisskeyboard(editText);
//                    GetWeatherTask getweather = new GetWeatherTask();
//                    getweather.execute(url);
//
//                } else {
//                    Snackbar.make(findViewById(R.id.action_container), R.string.invalid_url, Snackbar.LENGTH_LONG).show();
//                }
//
//            }
//        });




    }

    private void dimisskeyboard(View view) {
        InputMethodManager IMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        IMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private URL createUrl(String City) {
        String apikey = getString(R.string.api_key);
        String baseurl = getString(R.string.web_service_url);
        try {

            String urlString = baseurl + URLEncoder.encode(City, "UTF-8") + "&units=imperial&cnt=16&APPID=" + apikey;
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                int res = connection.getResponseCode();
                if (res == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                    } catch (IOException ex) {
                        Snackbar.make(findViewById(R.id.action_container), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                } else {
                    Snackbar.make(findViewById(R.id.action_container), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.action_container), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();

            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            convertJSONToArrayList(weather);
            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(0);
        }
    }

    private void convertJSONToArrayList(JSONObject json) {
        weatherList.clear();
        try {
            JSONArray list = json.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);

                JSONObject temp = day.getJSONObject("temp");

                JSONObject weather = day.getJSONArray("weather").getJSONObject(0);

                weatherList.add(new Weather(
                        day.getLong("dt"),
                        temp.getDouble("min"),
                        temp.getDouble("mix"),
                        day.getDouble("humidity"),
                        weather.getString("description"),
                        weather.getString("icon")));

            }

        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }
}

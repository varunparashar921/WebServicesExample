package com.example.sriva.webservicesexample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;

class AccessWebServiceActivity extends Activity {
    String city="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button submitButton = (Button)this.findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                String country="";
                EditText cityName = (EditText) findViewById(R.id.city_name);
                city=cityName.getText().toString();
                EditText countryName = (EditText) findViewById(R.id.country_name);
                country=countryName.getText().toString();
                String url="http://api.openweathermap.org/data/2.5/weather?q="+city+","+country;
                new ReadJSONFeed().execute(url);
            }
        });
    }
    private class ReadJSONFeed extends AsyncTask<String, String, String> {
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            HttpPost httppost = new HttpPost(urls[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
        protected void onPostExecute(String result) {
            String weatherInfo="Weather Report of "+city +" is: \n";
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jscoordObject = new JSONObject(jsonObject.getString("coord"));
                weatherInfo+="Longitude: "+jscoordObject.getString("lon")+"\n";
                weatherInfo+="Latitude: "+jscoordObject.getString("lat")+"\n";
                JSONArray jsweatherObject = new JSONArray(jsonObject.getString("weather"));
                JSONObject jweatherObject = jsweatherObject.getJSONObject(0);
                weatherInfo+="Clouds: "+jweatherObject.getString("description")+"\n";
                JSONObject jsmainObject = new JSONObject(jsonObject.getString("main"));
                weatherInfo+="Humidity: "+jsmainObject.getString("humidity")+"% \n";
                weatherInfo+="Atmospheric Pressure: "+jsmainObject.getString("pressure")+"hPa \n";
                float temp=Float.parseFloat(jsmainObject.getString("temp"));
                temp = temp - (float) 273.15;
                NumberFormat df = NumberFormat.getNumberInstance();
                df.setMaximumFractionDigits(2);
                weatherInfo+="Temperature: "+ String.valueOf(df.format(temp)) +" C\n";
                JSONObject jswindObject = new JSONObject(jsonObject.getString("wind"));
                weatherInfo+="Wind Speed: "+jswindObject.getString("speed")+"mps \n";
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            TextView resp = (TextView) findViewById(R.id.response);
            if(weatherInfo.trim().length() >0 )
                resp.setText(weatherInfo);
            else
                resp.setText("Sorry no match found");
        }
    }
}

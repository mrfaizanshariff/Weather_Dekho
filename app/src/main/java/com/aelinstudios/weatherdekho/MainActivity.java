package com.aelinstudios.weatherdekho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;
    private TextView textView,temp,tempmin,tempmax,pressure,humidity,reports;
    String baseURL="https://api.openweathermap.org/data/2.5/weather?q=";
    String API="&appid=API_KEY";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp=findViewById(R.id.temp);
        tempmin=findViewById(R.id.temp_min);
        tempmax=findViewById(R.id.temp_max);
        pressure=findViewById(R.id.pressure);
        humidity=findViewById(R.id.humidity);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.result);
        reports=findViewById(R.id.report);
        button=findViewById(R.id.button);
        requestQueue=VolleySingleton.getInstance(this).getRequestQueue();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendapirequest();
            }
        });




    }
    private void sendapirequest(){
        String url= baseURL+editText.getText().toString()+API;
//        Log.i("TEST",url);
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject= null;
                try {
                    jsonObject = response.getJSONObject("main");
                   int temperatur= (int) (jsonObject.getInt("temp") - 273.15);
                    int feels_like= (int) (jsonObject.getInt("feels_like")-273.15);
                    int temp_mini= (int) (jsonObject.getInt("temp_min")-273.15);
                    int temp_maxi = (int) (jsonObject.getInt("temp_max")-273.15);
                    int humidityy =jsonObject.getInt("humidity");
                    temp.setText("Temperature :"+String.valueOf(temperatur));
                    tempmin.setText("Temp Min :"+String.valueOf(temp_mini));
                    tempmax.setText("Temp Max :"+String.valueOf(temp_mini));
                    pressure.setText("Feels Like :"+String.valueOf(feels_like));
                    humidity.setText("Humidity :"+String.valueOf(humidityy));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray jsonArray=response.getJSONArray("weather");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject report = jsonArray.getJSONObject(i);
                        String brief_rep =report.getString("main");
                        String desc= report.getString("description");
                        textView.setText("Condition : "+brief_rep);
                        reports.setText("Details : "+desc);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No such city",Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(request);
    }
}

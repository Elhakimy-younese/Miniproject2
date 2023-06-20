package com.example.miniproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvstartactQuots, tvstartactAuther;
    Button Btnstartactpass;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvstartactQuots = findViewById(R.id.tvstartactQuots);
        tvstartactAuther = findViewById(R.id.tvstartactAuther);
        Btnstartactpass = findViewById(R.id.btnstartactpass);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvstartactQuots.setText(jsonObject.getString("quote"));
                            tvstartactAuther.setText(jsonObject.getString("author"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                tvstartactQuots.setText("That didn't work!");
            }
        });
        // Set the tag on the request.
        stringRequest.setTag("TAG");
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        Btnstartactpass.setOnClickListener(v -> {
            finish();
        });

    }
}
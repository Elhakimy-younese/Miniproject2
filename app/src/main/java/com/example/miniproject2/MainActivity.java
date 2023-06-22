package com.example.miniproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    TextView tvstartactQuots, tvstartactAuthor;
    Button Btnstartactpass;
    ToggleButton tbstartactpinunpin;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvstartactQuots = findViewById(R.id.tvstartactQuots);
        tvstartactAuthor = findViewById(R.id.tvstartactAuther);
        Btnstartactpass = findViewById(R.id.btnstartactpass);
        tbstartactpinunpin = findViewById(R.id.tbstartactpinunpin);




        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);

        String quote = sharedPreferences.getString("quote", null);
        if (quote == null){
            getRandomquote();
        }else {
            String author = sharedPreferences.getString("author", null);
            tvstartactQuots.setText(quote);
            tvstartactAuthor.setText(author);

            tbstartactpinunpin.setChecked(true);
        }

        Btnstartactpass.setOnClickListener(v -> {
            finish();
        });

        tbstartactpinunpin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String Quote = null;
            String Author = null;
            if (isChecked){
                Quote = tvstartactQuots.getText().toString();
                Author = tvstartactAuthor.getText().toString();

            }else {
                getRandomquote();
            }

            editor.putString("quote", Quote);
            editor.putString("author", Author);
            editor.commit();
        });



    }

    private void getRandomquote() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                response -> {
                    try {
                        tvstartactQuots.setText(response.getString("quote"));
                        tvstartactAuthor.setText(response.getString("author"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);


    }
}
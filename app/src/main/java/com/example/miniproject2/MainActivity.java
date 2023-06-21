package com.example.miniproject2;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

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
        String url = "https://dummyjson.com/quotes/3";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                response -> {


                    try {

                            tvstartactQuots.setText(response.getString("quote"));
                            tvstartactAuther.setText(response.getString("author"));


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });






        queue.add(jsonObjectRequest);

        Btnstartactpass.setOnClickListener(v -> {
            finish();
        });

    }
//    final int MIN = 1, MAX = 100;
//
//
//
//    private void display_random_quote(Context context, int min, int max) {
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        int random_number = new Random().nextInt(max - min + 1) + min;
//        String url = "https://dummyjson.com/quotes/" + random_number;
//
//
//        @SuppressLint("SetTextI18n")
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
//                response -> {
//                    try {
//                        id_tv.setText("#" + response.getInt("id"));
//                        quote_tv.setText("" + response.getString("quote"));
//                        author_tv.setText("" + response.getString("author"));
//
//                    } catch (Exception e) {
//                        Log.e("TAG", "display_random_quote: " + e);
//                    }
//                },
//                error -> quote_tv.setText("That didn't work!"));
//
//        queue.add(jsonObjectRequest);
//    }
//
//
//    Yla bit t5dem biha :
//    display_random_quote(this.context, MIN, MAX);
}
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
    Spinner spinner;
    SharedPreferences sharedPreferencescolor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvstartactQuots = findViewById(R.id.tvstartactQuots);
        tvstartactAuthor = findViewById(R.id.tvstartactAuther);
        Btnstartactpass = findViewById(R.id.btnstartactpass);
        tbstartactpinunpin = findViewById(R.id.tbstartactpinunpin);

        spinner = findViewById(R.id.colorSpinner);

        // work with spinner
        String[] spinnerItems = {"Default ", "LightSalmon", "Plum", "PaleGreen", "CornflowerBlue"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        sharedPreferencescolor = getSharedPreferences("color", MODE_PRIVATE);

        // Set the initial background color based on the stored color in SharedPreferences
        String storedColor = sharedPreferencescolor.getString("color", "Default");
        int savedColorIndex = spinnerAdapter.getPosition(storedColor);
        spinner.setSelection(savedColorIndex);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = spinner.getSelectedItem().toString();
                int backgroundColor;

                switch (selectedColor) {
                    case "LightSalmon":
                        backgroundColor = getResources().getColor(R.color.LightSalmon);
                        break;
                    case "Plum":
                        backgroundColor = getResources().getColor(R.color.plum);
                        break;
                    case "PaleGreen":
                        backgroundColor = getResources().getColor(R.color.PaleGreen);
                        break;
                    case "CornflowerBlue":
                        backgroundColor = getResources().getColor(R.color.CornflowerBlue);
                        break;
                    default:
                        backgroundColor = getResources().getColor(R.color.white);
                        break;
                }

                getWindow().getDecorView().setBackgroundColor(backgroundColor);

                // Store the selected color in SharedPreferences
                SharedPreferences.Editor coloreditor = sharedPreferencescolor.edit();
                coloreditor.putString("color", selectedColor);
                coloreditor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






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
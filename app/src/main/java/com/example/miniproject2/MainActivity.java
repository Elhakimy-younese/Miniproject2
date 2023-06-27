package com.example.miniproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miniproject2.colorsdb.ColorsDbHelper;
import com.example.miniproject2.db.quotesDbHelper;
import com.example.miniproject2.modols.Colors;
import com.example.miniproject2.modols.Quote;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private final static int INVALIDE_ID = -1;
    TextView tvstartactQuots, tvstartactAuthor;
    Button Btnstartactpass;
    ToggleButton tbstartactpinunpin;
    SharedPreferences sharedPreferences;

    ImageView Ivstartactfavorite;
    quotesDbHelper db;
    ColorsDbHelper colordb;
    TextView tvstartactid;
    Spinner spinner ;
    ArrayList<Colors> spinnerItems;
    @SuppressLint({"MissingInflatedId", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvstartactQuots = findViewById(R.id.tvstartactQuots);
        tvstartactAuthor = findViewById(R.id.tvstartactAuther);
        Btnstartactpass = findViewById(R.id.btnstartactpass);
        tbstartactpinunpin = findViewById(R.id.tbstartactpinunpin);
        Ivstartactfavorite = findViewById(R.id.ivstartactfavorite);
        tvstartactid = findViewById(R.id.tvstartactid);
        spinner = findViewById(R.id.colorSpinner);



        colordb =new ColorsDbHelper(this);
        colordb.add(new Colors( "Default       ", "#FF000000"));
        colordb.add(new Colors( "LightSalmon   ", "#FFA07A"));
        colordb.add(new Colors( "Plum          ", "#DDA0DD"));
        colordb.add(new Colors("PaleGreen     ", "#98FB98"));
        colordb.add(new Colors("CornflowerBlue", "#6495ED"));


        spinnerItems = colordb.getAllcolors();
        ArrayAdapter<Colors> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = spinner.getSelectedItem().toString();
                int backgroundColor;
                String code = null;

                switch (selectedColor) {
                    case "LightSalmon":
                        backgroundColor = getResources().getColor(R.color.LightSalmon);
                        code = "#FFA07A";
                        break;
                    case "Plum":
                        backgroundColor = getResources().getColor(R.color.plum);
                        code = "#DDA0DD";
                        break;
                    case "PaleGreen":
                        backgroundColor = getResources().getColor(R.color.PaleGreen);
                        code = "#98FB98";
                        break;
                    case "CornflowerBlue":
                        backgroundColor = getResources().getColor(R.color.CornflowerBlue);
                        code = "#6495ED";
                        break;
                    default:
                        backgroundColor = getResources().getColor(R.color.white);
                        code = "#FF000000";
                        break;
                }

                getWindow().getDecorView().setBackgroundColor(backgroundColor);
                colordb.addbgcolor(selectedColor, code);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        db = new quotesDbHelper(this);


        //region like / dislike
        Ivstartactfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(tvstartactid.getText().toString().substring(1));
                boolean isfavorite = db.isFavorite(id);
                if (isfavorite){
                    tbstartactpinunpin.setChecked(false);
                    Ivstartactfavorite.setImageResource(R.drawable.dislike);

                    db.delete(id );



                }else {
                    Ivstartactfavorite.setImageResource(R.drawable.like);

                    String quote = tvstartactQuots.getText().toString();
                    String author = tvstartactAuthor.getText().toString();
                    db.add(new Quote(id, quote, author ));



                }
//                isfavorite = !isfavorite;
            }
        });
        //endregion





        //region pin / unpin / sharedpreferences
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);

        int pinnedquoteid = sharedPreferences.getInt("id", INVALIDE_ID);

        if (pinnedquoteid == INVALIDE_ID){
            getRandomquote();
        }else {
            String pinnedquote = sharedPreferences.getString("quote", null);
            String author = sharedPreferences.getString("author", null);

            tvstartactid.setText(String.format("#%d", pinnedquoteid));
            tvstartactQuots.setText(pinnedquote);
            tvstartactAuthor.setText(author);

            Ivstartactfavorite.setImageResource(db.isFavorite(pinnedquoteid)? R.drawable.like : R.drawable.dislike);
}

        Btnstartactpass.setOnClickListener(v -> {
            getRandomquote();
        });

        tbstartactpinunpin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int pinnedid = INVALIDE_ID;
            String Quote = null;
            String Author = null;
            if (isChecked){
                pinnedid = Integer.parseInt(tvstartactid.getText().toString().substring(1)) ;
                Quote = tvstartactQuots.getText().toString();
                Author = tvstartactAuthor.getText().toString();

                if (!db.isFavorite(pinnedid)) {
                    Ivstartactfavorite.setImageResource(R.drawable.like);

                    db.add(new Quote(pinnedid, Quote, Author));



                    //endregion
                }


            }else {
                getRandomquote();
            }
            editor.putInt("id", pinnedid);
            editor.putString("quote", Quote);
            editor.putString("author", Author);
            editor.commit();
        });
        //endregion







    }


    private void getRandomquote() {

        RequestQueue queue = Volley.newRequestQueue(this);
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        String url = String.format("https://dummyjson.com/quotes/%d", randomNumber);

//        String url = "https://dummyjson.com/quotes/random";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                response -> {
                    try {

                        int id = response.getInt("id");
                        String quote = response.getString("quote");
                        String author = response.getString("author");

                        if (db.isFavorite(id))
                            Ivstartactfavorite.setImageResource(R.drawable.like);
                        else
                            Ivstartactfavorite.setImageResource(R.drawable.dislike);

                        tvstartactid.setText(String.format("#%d", id));
                        tvstartactQuots.setText(quote);
                        tvstartactAuthor.setText(author);
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
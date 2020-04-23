package com.example.stt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.os.Bundle;
import android.speech.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private EditText speechtext;
    private ImageButton talk;
    private String resulto = "";
    private ListView lView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechtext = findViewById(R.id.speechtext);
        talk = findViewById(R.id.talkbut);
        lView = findViewById(R.id.list);
        //resulto = "";
        final String[] colorsArray = new String[]{
                "red", "blue", "green", "black", "white",
                "gray", "cyan", "magenta", "yellow", "lightgray",
                "darkgray", "grey", "lightgrey", "darkgrey", "aqua",
                "fuchsia", "lime", "maroon", "navy", "olive",
                "purple", "silver", "teal"
        };

        //Get widgets reference from XML layout


        //Initialize an ArrayAdapter and data bind with a String Array
        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,colorsArray);

        //Data bind ListView with ArrayAdapter
        lView.setAdapter(adapter);
        talk.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 10);

            }


        });
        speechtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (MainActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 10:
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resulto = result.get(0);
                    speechtext.setText(resulto);



            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }
}

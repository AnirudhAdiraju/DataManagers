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
    ArrayList<Item> items = new ArrayList<Item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        speechtext = findViewById(R.id.speechtext);
        talk = findViewById(R.id.talkbut);
        lView = findViewById(R.id.list);
        //resulto = "";
        items.add(new Item("Gala Apples",5));
        items.add(new Item("Yellow Bananas",3));
        items.add(new Item("Oranges",5));
        items.add(new Item("Red Delicious Apples",1));
        items.add(new Item("Green Bananas",1));
        items.add(new Item("Red Grapes",10));
        items.add(new Item("Green Grapes",2));

        //Get widgets reference from XML layout


        //Initialize an ArrayAdapter and data bind with a String Array
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(items.get(position).getName());
                text2.setText("Stock:"+items.get(position).getStock());
                return view;
            }
        };

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
                int textlength = s.length();
                final ArrayList<Item> tempArrayList = new ArrayList<Item>();
                for(Item c: items){
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, tempArrayList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        text1.setText(tempArrayList.get(position).getName());
                        text2.setText("Stock:"+tempArrayList.get(position).getStock());
                        return view;
                    }
                };
                lView.setAdapter(adapter);
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
                    int textlength = resulto.length();
                    final ArrayList<Item> tempArrayList = new ArrayList<Item>();
                    for(Item c: items){
                        if (textlength <= c.getName().length()) {
                            if (c.getName().toLowerCase().contains(resulto.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }
                        }
                    }

                    adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, tempArrayList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                            text1.setText(tempArrayList.get(position).getName());
                            text2.setText("Stock:"+tempArrayList.get(position).getStock());
                            return view;
                        }
                    };
                    lView.setAdapter(adapter);


            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }
}

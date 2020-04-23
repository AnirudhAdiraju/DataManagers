package com.example.stt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.speech.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
        addItemToSheet();
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
    private void addItemToSheet() {
        final ProgressDialog loading = ProgressDialog.show(this,"Getting Items","Please wait");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxLHCBJ3n_UQ7Ndi89abcmXf4Erz6OoEczZKLIgEkl53xhA5ohn/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        String[] itms = response.split(",");
                        if (itms[0].equals("true")) {
                            Toast.makeText(MainActivity.this, "Items Received From Database", Toast.LENGTH_LONG).show();
                            for (int x=1; x<itms.length;x+=2){
                                items.add(new Item(itms[x],itms[x+1]));
                            }

                            //Get widgets reference from XML layout


                            //Initialize an ArrayAdapter and data bind with a String Array
                            adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, items) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                    text1.setText(items.get(position).getName());
                                    text2.setText("Stock: "+items.get(position).getStock());
                                    return view;
                                }
                            };

                            //Data bind ListView with ArrayAdapter
                            lView.setAdapter(adapter);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR",error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //here we pass params
                params.put("action","getItems");

                return params;
            }
        };

        int socketTimeOut = 5000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


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

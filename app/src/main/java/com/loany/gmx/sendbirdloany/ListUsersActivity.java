package com.loany.gmx.sendbirdloany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListUsersActivity extends AppCompatActivity {

    TextView textView;
    //ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        textView = (TextView) findViewById(R.id.textView3);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.sendbird.com/v3/users";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ListView listView = (ListView) findViewById(R.id.list1);
                        ArrayList<String> list = new ArrayList<>();
                        // Display the first 500 characters of the response string.
                        String json = new String(response);
                        Log.d("Response is: ", json);

                        Gson gson = new Gson();
                        UserList userObject = gson.fromJson(json, UserList.class);

                        for(int i=0;i<userObject.users.size();i++) {
                            list.add(i,userObject.users.get(i).getUser_id());
                            Log.d("user", list.get(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListUsersActivity.this,android.R.layout.simple_list_item_1,list);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                //ListView user = (ListView) parent.getItemAtPosition(position);
                                Intent intent = new Intent(ListUsersActivity.this, GroupChannelActivity.class);
                                Log.d("user:",parent.getItemAtPosition(position).toString());
                                intent.putExtra("UserName", parent.getItemAtPosition(position).toString());
                                startActivity(intent);
                            }
                        });



                        //textView.setText(list);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response","That didn't work!");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Api-Token", "38f9076c203d585f007be392218fec5457f396ac");
                return headers;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void GetUsers() {


    }

}

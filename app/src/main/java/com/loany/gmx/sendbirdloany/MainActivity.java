package com.loany.gmx.sendbirdloany;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText Username,Password;
    TextView set;
    Context context;
    CharSequence success;
    CharSequence failure;
    int duration;
    Toast toastSuccess;
    Toast toastFailure;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set = (TextView) findViewById(R.id.textView);
        Username = (EditText) findViewById(R.id.userid);
        Password = (EditText) findViewById(R.id.password);

        context = getApplicationContext();
        success = "user registered";
        failure = "call not successfull";
        duration = Toast.LENGTH_SHORT;
        toastSuccess = Toast.makeText(context,success,duration);
        toastFailure = Toast.makeText(context,failure,duration);

        SendBird.init(getString(R.string.app_id), MainActivity.this);

    }


    public void Login(View view) {
        SendBirdLogin();
        Intent intent = new Intent(MainActivity.this, ListUsersActivity.class);
        //intent.putExtra("User Name",Username.getText().toString());
        startActivity(intent);

    }

    public void SendBirdLogin() {
        SendBird.connect(Username.getText().toString(), new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
            }
        });
    }

    public void Register(View view) {
        SendBirdRegister();
    }


    public void SendBirdRegister() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.sendbird.com/v3/users";

        String jsonString = "{\n" +
                "    \"user_id\": \"User\",               \n" +
                "    \"nickname\": \"Akhil\",\n" +
                "    \"profile_url\": \"https://sendbird.com/main/img/profiles/profile_03_512px.png\",\n" +
                "    \"issue_access_token\": true\n" +
                "}";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                toastSuccess.show();
                                token = response.getString("access_token");
                                Log.d("Token", token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    toastFailure.show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Api-Token", "38f9076c203d585f007be392218fec5457f396ac");
                    return headers;
                }
            };


// Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

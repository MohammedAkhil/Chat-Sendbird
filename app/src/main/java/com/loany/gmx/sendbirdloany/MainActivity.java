package com.loany.gmx.sendbirdloany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    EditText Username,Password;
    TextView set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set = (TextView) findViewById(R.id.textView);
        Username = (EditText) findViewById(R.id.userid);
        Password = (EditText) findViewById(R.id.password);

        SendBird.init(getString(R.string.app_id), MainActivity.this);

    }


    public void Login(View view) {
        SendBirdLogin();
    }




    public void Register(View view) {
        SendBirdLogin();
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
        Intent intent = new Intent(MainActivity.this, ListOpenChannel.class);
        intent.putExtra("User Name",Username.getText().toString());
        startActivity(intent);

    }

}

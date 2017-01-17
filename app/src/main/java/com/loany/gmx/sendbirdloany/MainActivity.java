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
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);

        SendBird.init(getString(R.string.app_id), MainActivity.this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("PMyoLlBam7kD9xhDOhdcCohw1CcGdXycxKY1OpLq")
                .clientKey("JPfgT7zQ77tDZN56Ecx8b9vI8s2Y3w2YlhLcCE0i")
                .server("https://parseapi.back4app.com/").build()
        );


        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "364171950094");
        installation.saveInBackground();



    }


    public void Login(View view) {
        ParseUser.logInInBackground(Username.getText().toString(), Password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    //set.setText("Hello parse");
                    //ParseObject data = new ParseObject("data");
                    //data.put("loany1", 1234567);
                    //data.saveInBackground();
                    SendBirdLogin();

                } else {
                    //Login Fail
                    //get error by calling e.getMessage()
                    set.setText("failed");
                }
            }
        });
    }




    public void Register(View view) {
        ParseUser user = new ParseUser();
        user.setUsername(Username.getText().toString());
        user.setPassword(Password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Register Successful
                    SendBirdLogin();
                    set.setText("registered!!");
                } else {
                    //Register Fail
                    //get error by calling e.getMessage()
                }
            }
        });
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

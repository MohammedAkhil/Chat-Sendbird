package com.loany.gmx.sendbirdloany;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

public class OpenChatActivity extends AppCompatActivity {

    private String mChannelUrl;
    private EditText textmessage;
    //private String message;
    private OpenChannel channel;
    private TextView text;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_chat);


        textmessage = (EditText) findViewById(R.id.textmessage);
        text = (TextView) findViewById(R.id.textView2);

        mChannelUrl = getIntent().getStringExtra("channel_url");
        userid = getIntent().getStringExtra("User Name");


        if (mChannelUrl == null || mChannelUrl.length() <= 0) {
            finish();
            return;
        }


        OpenChannel();


    }

    public void OpenChannel() {
        OpenChannel.getChannel(mChannelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;
                        }
                        channel = openChannel;
                    }
                });
            }
        });
    }

    public void send(View view) {

        final ParseObject messages = new ParseObject("Messages");

        channel.sendUserMessage(textmessage.getText().toString(), new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                messages.put("UserName",userid);
                messages.put("Messages", textmessage.getText().toString());
                messages.saveInBackground();
                text.setText(textmessage.getText().toString());
            }
        });
    }

}

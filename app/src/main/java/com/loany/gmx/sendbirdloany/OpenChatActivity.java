package com.loany.gmx.sendbirdloany;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.util.List;

public class OpenChatActivity extends AppCompatActivity {

    private String mChannelUrl;
    private EditText textmessage;
    //private String message;
    private OpenChannel mOpenChannel;
    private TextView text;
    private String userid;
    private static final String identifier = "SendBirdOpenChat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_chat);


        textmessage = (EditText) findViewById(R.id.textmessage);
        text = (TextView) findViewById(R.id.textView2);

        mChannelUrl = getIntent().getStringExtra("channel_url");
        userid = getIntent().getStringExtra("User Name");

        OpenChannel(mChannelUrl);
        RecieveMessages();



    }

    private void OpenChannel(String ChannelUrl) {
        OpenChannel.getChannel(ChannelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    //Toast.makeText(getParent(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        mOpenChannel = openChannel;
                    }
                });
            }
        });
    }


    private void exitChannel() {
        if (mOpenChannel != null) {
            mOpenChannel.exit(null);
        }
    }

    private void refreshChannel() {
        if (mOpenChannel != null) {
            mOpenChannel.refresh(null);
        }
    }


    private void sendmsg() {

        final DatabaseHandler db = new DatabaseHandler(this);


        mOpenChannel.sendUserMessage(textmessage.getText().toString(), new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                db.addMessage(new Message(textmessage.getText().toString(),userid));


                List<Message> messages = db.getAllMessages();

                for(Message msg: messages) {
                    String log = "userid - " + msg.getID() + ", message - " + msg.getMessage();
                    Log.d("message :", log);
                }

                text.setText(textmessage.getText().toString());
            }
        });
    }

    private void RecieveMessages() {
        SendBird.addChannelHandler(identifier, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseMessage instanceof UserMessage) {
                    text.setText(((UserMessage) baseMessage).getMessage().toString());
                } else if (baseMessage instanceof FileMessage) {
                    // item is a FileMessage
                } else if (baseMessage instanceof AdminMessage) {
                    // item is an AdminMessage
                }
            }
        });

    }


    public void send(View view) {

        //final ParseObject messages = new ParseObject("Messages");

        sendmsg();
    }

    public void exit(View view) {
        exitChannel();
        finish();
    }

    public void refresh(View view) {
        refreshChannel();
    }

}

package com.loany.gmx.sendbirdloany;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class GroupChannelActivity extends AppCompatActivity {

    String receiver;
    String sender;
    TextView sentText;
    EditText sendText;
    private GroupChannel channel;
    String channelName;
    private static final String identifier = "SendBirdGroupChannelList";
    private PreviousMessageListQuery mPrevMessageListQuery;
    List<String> prevMessageList = new ArrayList<>();
    String prevmsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);
        sentText = (TextView) findViewById(R.id.sentMessage);
        sendText = (EditText) findViewById(R.id.sendMessage);
        receiver = getIntent().getStringExtra("UserName");
        sender = SendBird.getCurrentUser().getUserId();
        List<String> users = new ArrayList<>();
        users.add(sender);
        users.add(receiver);
        //channel = null;
        channelName = sender + "-and-" + receiver;


        GroupChannel.createChannelWithUserIds(users,true,channelName,null,null, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error.
                    Log.d("Error","channel not opened");
                    return;
                }

                try {
                    channel = groupChannel;
                } catch (NullPointerException e1) {
                    Log.d("exception", String.valueOf(e1));
                }


                loadPrevMessages(true,groupChannel);
            }
        });





    }


    public void send(View view) {
        channel.sendUserMessage(sendText.getText().toString(), new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error.
                    Log.d("Error","MSG not sent");
                    return;
                }
                Messages messages = new Messages(sender, receiver, userMessage.getMessage());
                messages.save();
                sentText.setText(sendText.getText().toString());
            }
        });
    }



    @Override
    public void onPause() {
        super.onPause();
        SendBird.removeChannelHandler(identifier);
    }

    @Override
    public void onResume() {
        super.onResume();

        SendBird.addChannelHandler(identifier, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel instanceof GroupChannel) {
                    //GroupChannel groupChannel = (GroupChannel) baseChannel;
                    Messages messages = new Messages(((UserMessage) baseMessage).getSender().getUserId(),SendBird.getCurrentUser().getUserId(),((UserMessage) baseMessage).getMessage());
                    messages.save();
                }
            }

        });

    }

    private void loadPrevMessages(final boolean refresh, GroupChannel mchannel){

        if (mchannel == null) {
            Log.d("Channel","NULL!!!!!!!!!!!!!!!!!!!!!!!1");
            return;
        }

        if (refresh || mPrevMessageListQuery == null) {
            mPrevMessageListQuery = channel.createPreviousMessageListQuery();
        }

        if (mPrevMessageListQuery.isLoading()) {
            return;
        }

        if (!mPrevMessageListQuery.hasMore()) {
            return;
        }


        mPrevMessageListQuery.load(30, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> messages, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                for(BaseMessage message: messages) {
                    prevmsg = "";
                    prevMessageList.add(((UserMessage) message).getMessage());
                    //prevmsg = prevmsg + ((UserMessage) message).getMessage() + "-user\n";
                }

                BaseMessage msg = messages.get(0);
                UserMessage userMessage = ((UserMessage) msg);
                userMessage.getSender();


                if(!prevMessageList.isEmpty()) {
                    //Log.d("prev",prevMessageList.get(0));
                    sentText.setText(prevmsg);
                }

            }
        });


    }


}

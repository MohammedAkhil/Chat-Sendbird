package com.loany.gmx.sendbirdloany;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.OpenChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import static android.R.id.list;

public class ListOpenChannel extends AppCompatActivity {

    private ListView mListView;
    private OpenChannelListQuery mChannelListQuery;
    private SendBirdChannelAdapter mAdapter;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_open_channel);

        userid = getIntent().getStringExtra("User Name");


        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new SendBirdChannelAdapter(this);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenChannel channel = mAdapter.getItem(position);
                Intent intent = new Intent(ListOpenChannel.this, OpenChatActivity.class);
                intent.putExtra("channel_url", channel.getUrl());
                intent.putExtra("User Name", userid);
                startActivity(intent);
            }
        });


        OpenChannelListQuery channelListQuery = OpenChannel.createOpenChannelListQuery();
        channelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
            @Override
            public void onResult(final List<OpenChannel> channels, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                mAdapter.addAll(channels);
                mAdapter.notifyDataSetChanged();
            }
        });


    }


    public static class SendBirdChannelAdapter extends BaseAdapter {


        private final LayoutInflater mInflater;
        private final ArrayList<OpenChannel> mItemList;

        public SendBirdChannelAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItemList = new ArrayList<>();
        }

        public int getCount() {
            return mItemList.size();
        }

        @Override
        public OpenChannel getItem(int position) {
            return mItemList.get(position);
        }

        public void clear() {
            mItemList.clear();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void add(OpenChannel channel) {
            mItemList.add(channel);
            notifyDataSetChanged();
        }

        public void addAll(Collection<OpenChannel> channels) {
            mItemList.addAll(channels);
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.sendbird_view_open_channel, parent, false);
                viewHolder.setView("img_thumbnail", convertView.findViewById(R.id.img_thumbnail));
                viewHolder.setView("txt_topic", convertView.findViewById(R.id.txt_topic));
                viewHolder.setView("txt_desc", convertView.findViewById(R.id.txt_desc));

                convertView.setTag(viewHolder);
            }

            OpenChannel item = getItem(position);
            viewHolder = (ViewHolder) convertView.getTag();
            //Helper.displayUrlImage(viewHolder.getView("img_thumbnail", ImageView.class), item.getCoverUrl());
            viewHolder.getView("txt_topic", TextView.class).setText("#" + item.getName());
            viewHolder.getView("txt_desc", TextView.class).setText("" + item.getParticipantCount() + ((item.getParticipantCount() <= 1) ? " Member" : " Members"));

            return convertView;
        }

        private static class ViewHolder {
            private Hashtable<String, View> holder = new Hashtable<>();

            public void setView(String k, View v) {
                holder.put(k, v);
            }

            public View getView(String k) {
                return holder.get(k);
            }

            public <T> T getView(String k, Class<T> type) {
                return type.cast(getView(k));
            }
        }
    }

    public void ListUsers(){
    }


    }



package com.cmu.android.juicychatcomp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.cmu.android.juicychatcomp.DB.DatabaseConnector;
import com.cmu.android.juicychatcomp.DB.Message;

import java.util.List;

public class ChatroomActivity extends AppCompatActivity {
    public final static String CHATROOM_GROUP = "groupCode";
    private ListView msgListView;
    private MessageListAdapter mMessageListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        // get corresponding group
        Bundle extras = getIntent().getExtras();
        int groupCode = extras.getInt(CHATROOM_GROUP);

        // using corresponding group to query messages
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        List<Message> messages = connector.getAllMessagesByGroupOrderByTime(groupCode);

        // to debug
        Toast.makeText(this, "get " + messages.size() + " messages", Toast.LENGTH_SHORT).show();

        // set up the adapter
        mMessageListAdapter = new MessageListAdapter(this, messages);

        // set up the list view
        msgListView = (ListView) findViewById(R.id.list_view_messages);
        msgListView.setAdapter(mMessageListAdapter);
    }
}

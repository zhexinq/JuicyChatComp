package com.cmu.android.juicychatcomp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.cmu.android.juicychatcomp.DB.DatabaseConnector;
import com.cmu.android.juicychatcomp.DB.Group;
import com.cmu.android.juicychatcomp.DB.Message;
import com.cmu.android.juicychatcomp.helper.SimpleItemTouchHelperCallback;

import java.util.List;

public class GroupChatsActivity extends AppCompatActivity {
    private final String TAG = GroupChatsActivity.class.getSimpleName();
    private RecyclerListAdapter mRecyclerListAdapter;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;

    private static final String[] STRINGS = new String[]{
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_chat);

        // TEST BEGIN
        // sample data for testing db
        Group group1 = new Group();
        group1.groupCode = 1;
        group1.createTime = "1991-05-27 01:00:21";
        Group group2 = new Group();
        group2.groupCode = 2;
        group2.createTime = "1991-08-27 01:00:21";

        Message message1 = new Message();
        message1.createTime = "1991-05-27 02:00:21";
        message1.owner = "zhexinq";
        message1.message = "Juicy Meeting is awesome";
        message1.group = group1;
        Message message2 = new Message();
        message2.createTime = "1991-05-27 11:00:21";
        message2.owner = "zhexinq";
        message2.message = "Agreed";
        message2.group = group1;

        // Get singleton instance of database
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        // Add a group
        connector.addGroup(group1);
        connector.addGroup(group2);
        // Add a message
        connector.addMessage(message1);
        connector.addMessage(message2);
        // query groups
        List<Group> groups = connector.getAllGroupsOrderByCreateTime();
        for (Group g : groups) {
            Log.d(TAG, "group: " + g.groupCode + " " + g.createTime);
        }
        // query message by group
        List<Message> messages = connector.getAllMessagesByGroupOrderByTime(group1.groupCode);
        for (Message m : messages) {
            Log.d(TAG, "message: " + m.message + " " + m.createTime);
        }
        // delete group
        connector.deleteChatGroup(group2.groupCode);
        groups = connector.getAllGroupsOrderByCreateTime();
        for (Group g : groups) {
            Log.d(TAG, "group: " + g.groupCode + " " + g.createTime);
        }
        // TEST END

        mRecyclerListAdapter = new RecyclerListAdapter(STRINGS);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mRecyclerListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecyclerListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

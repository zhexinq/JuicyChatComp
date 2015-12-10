package com.cmu.android.juicychatcomp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class JoinGroupActivity extends AppCompatActivity {
    private static final String TAG = JoinGroupActivity.class.getSimpleName();
    private String chatAction;
    private EditText groupCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        // find the group code view
        groupCodeEditText = (EditText) findViewById(R.id.join_group_password);
        // get whether user wants to create/join an group chat
        chatAction = getIntent().getExtras().getString(ChatroomActivity.CHAT_ACTION);
        Log.e(TAG, "get chat action: " + chatAction);
    }

    // invoked when cancel button clicked
    public void cancel(View view) {
        finish();
    }

    // invoke when ok button clicked
    public void confirm(View view) {
        // pass group code and chat action to the chat room session
        Intent i = new Intent(this, ChatroomActivity.class);
        String groupCode = groupCodeEditText.getText().toString();
        i.putExtra(ChatroomActivity.CHATROOM_GROUP, groupCode);
        i.putExtra(ChatroomActivity.CHAT_ACTION, chatAction);
        startActivity(i);
    }


}
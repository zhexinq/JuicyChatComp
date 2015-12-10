package com.cmu.android.juicychatcomp;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.cmu.android.juicychatcomp.DB.DatabaseConnector;
import com.cmu.android.juicychatcomp.DB.Message;
import com.cmu.android.juicychatcomp.util.WsConfig;
import com.codebutler.android_websockets.WebSocketClient;

import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class ChatroomActivity extends AppCompatActivity {
    public final static String TAG = ChatroomActivity.class.getSimpleName();
    public final static String CHATROOM_GROUP = "groupCode";
    public final static String CHAT_ACTION = "chatAction";
    public final static String IS_OLD_USER = "isOldUser";
    private ListView msgListView;
    private List<Message> mMessages;
    private MessageListAdapter mMessageListAdapter;
    private WebSocketClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        // get corresponding group
        Bundle extras = getIntent().getExtras();
        int groupCode = extras.getInt(CHATROOM_GROUP);

        // using corresponding group to query messages
        DatabaseConnector connector = DatabaseConnector.getInstance(this);
        mMessages = connector.getAllMessagesByGroupOrderByTime(groupCode);

        // to debug
        Toast.makeText(this, "get " + mMessages.size() + " messages", Toast.LENGTH_SHORT).show();

        // set up the adapter
        mMessageListAdapter = new MessageListAdapter(this, mMessages);

        // set up the list view
        msgListView = (ListView) findViewById(R.id.list_view_messages);
        msgListView.setAdapter(mMessageListAdapter);

        String queryParams = String.format("name=%s&groupCode=%s&action=%s&isOldUser=%s", "juicer", 1, "create", "false");
        client = new WebSocketClient(URI.create(WsConfig.URL_WEBSOCKET
                + URLEncoder.encode(queryParams)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {

            }

            /**
             * On receiving the message from web socket server
             * */
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));

                parseMessage(message);

            }

            @Override
            public void onMessage(byte[] data) {}

            /**
             * Called when the connection is terminated
             * */
            @Override
            public void onDisconnect(int code, String reason) {

                String message = String.format(Locale.US,
                        "Disconnected! Code: %d Reason: %s", code, reason);

                showToast(message);

                // clear the session id from shared preferences
//                utils.storeSessionId(null);
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error! : " + error);

                showToast("Error! : " + error);
            }

        }, null);

        client.connect();
    }

    private void parseMessage(String message) {

    }

    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mMessages.add(m);

                mMessageListAdapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(client != null & client.isConnected()){
            client.disconnect();
        }
    }

    /**
     * Plays device's default notification sound
     * */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

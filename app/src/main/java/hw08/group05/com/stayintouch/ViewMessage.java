package hw08.group05.com.stayintouch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewMessage extends AppCompatActivity {

    Firebase ref;
    ListView listView;
    EditText editText;
    ImageButton imageButton;
    static User convouser, curruser;
    ArrayList<Message> messageArrayList = new ArrayList<>();
    ViewMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        if (getIntent().getExtras() != null) {
            convouser = (User) getIntent().getExtras().getSerializable(Conversation.CLICK_OBJ);
            curruser = (User) getIntent().getExtras().getSerializable(Conversation.CURRUSER);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.logo7);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(convouser.getUserFullname());

        ref = Application.ref;

        imageButton = (ImageButton) findViewById(R.id.imageButton_SendMessage);
        editText = (EditText) findViewById(R.id.editText_SendMessage);
        listView = (ListView) findViewById(R.id.listView_ViewMessage);

        final Firebase messageRef = ref.child("messages");

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Message message = postSnapshot.getValue(Message.class);
                    if (message.getSenderName().equalsIgnoreCase(curruser.getUserFullname())
                            && message.getReceiverName().equalsIgnoreCase(convouser.getUserFullname())
                            || message.getReceiverName().equalsIgnoreCase(curruser.getUserFullname())
                            && message.getSenderName().equalsIgnoreCase(convouser.getUserFullname())) {
                        messageArrayList.add(message);
                    }
                }
                Log.d("messageArrayList", messageArrayList.size() + "");
                adapter = new ViewMessageAdapter(ViewMessage.this, R.layout.messageitemlayout, messageArrayList);
                listView.setAdapter(adapter);
                adapter.setNotifyOnChange(true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Query queryRef = messageRef.orderByChild("timeStamp");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                Log.d("messageadded", message.getMessageText());
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                Log.d("messageremoved", message.getMessageText());
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final Firebase userRef = ref.child("users/username/"+convouser.getPushId());
                Log.d("userRef",userRef.toString());
                Map<String,Object> taskMap = new HashMap<String,Object>();
                taskMap.put("read", "true");
                userRef.updateChildren(taskMap);*/

                Message enteredMessage = new Message();

                Firebase newmessage = messageRef.push();
                enteredMessage.setMessageText(editText.getText().toString());
                Log.d("sentmessage", editText.getText().toString());
                enteredMessage.setReceiverName(convouser.getUserFullname());
                enteredMessage.setSenderName(curruser.getUserFullname());

                Date date = new Date();
                enteredMessage.setTimeStamp(String.valueOf(date.getTime()));
                enteredMessage.setMessageRead(false);
                enteredMessage.setPushId(newmessage.getKey());


                newmessage.setValue(enteredMessage, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        Toast.makeText(ViewMessage.this, "Message Sent", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
                startActivity(getIntent());
                editText.setText("");
                /*final Firebase userRef1 = ref.child("users/username/"+curruser.getPushId());
                Map<String,Object> taskMap1 = new HashMap<String,Object>();
                taskMap1.put("read", "false");
                userRef1.updateChildren(taskMap1);*/
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewmessageoptions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_viewcontact:
                Intent inte = new Intent(ViewMessage.this, ViewContact.class);
                startActivity(inte);
                break;
            // action with ID action_settings was selected
            case R.id.action_callcontact:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + convouser.getPhoneNo()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return true;
                }
                startActivity(callIntent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        /*final Firebase userRef = ref.child("users/username/"+convouser.getPushId());
        Log.d("userRef",userRef.toString());
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("read", "true");
        userRef.updateChildren(taskMap);*/
        Intent intent = new Intent(ViewMessage.this,Conversation.class);
        startActivity(intent);
        finish();
    }
}

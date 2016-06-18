package hw08.group05.com.stayintouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.view.ViewProcessor;

import java.util.ArrayList;

public class Conversation extends AppCompatActivity {

    Firebase ref;
    ListView listView;
    static String userEmail = "";
    final ArrayList<User> usersArrayList = new ArrayList<>();
    final static String CURRUSER = "CURRUSER";
    final static String CLICK_OBJ = "CLICK_OBJ";
    ProgressDialog progressDialog;
    static User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Firebase.setAndroidContext(this);
        ref = Application.ref;

        if (getIntent().getExtras() != null) {
            userEmail = getIntent().getExtras().getString("USER");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ref.child("users/username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " users");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User iteratoruser = postSnapshot.getValue(User.class);
                    if (iteratoruser.getUserEmail().equalsIgnoreCase(userEmail)){
                        currUser = iteratoruser;
                    }else{
                        usersArrayList.add(iteratoruser);
                    }
                }



                listView = (ListView) findViewById(R.id.listView);
                Log.d("pawan", usersArrayList.size()+"");

                ListViewAdapter adapter = new ListViewAdapter(Conversation.this,R.layout.rowitemlayout, usersArrayList);

                listView.setAdapter(adapter);

                adapter.setNotifyOnChange(true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent inte = new Intent(Conversation.this, ViewMessage.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(CLICK_OBJ, usersArrayList.get(position));
                        bundle.putSerializable(CURRUSER, currUser);
                        inte.putExtras(bundle);
                        startActivity(inte);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed " + firebaseError.getMessage());
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_editprofile:
                Intent inte = new Intent(Conversation.this, EditProfile.class);
                inte.putExtra("USER", userEmail);
                startActivity(inte);
                break;
            // action with ID action_settings was selected
            case R.id.action_logout:
                ref.unauth();
                Intent intent = new Intent(Conversation.this,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Conversation.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

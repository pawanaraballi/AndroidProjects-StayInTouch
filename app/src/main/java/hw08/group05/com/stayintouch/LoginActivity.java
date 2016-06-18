package hw08.group05.com.stayintouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button createuser,login;
    EditText editTextemail,editTextpassword;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.logo7);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(R.string.stayintouchlogin);

        Firebase.setAndroidContext(this);
        ref = Application.ref;

        login = (Button) findViewById(R.id.button_login);
        createuser = (Button) findViewById(R.id.button_createnewuser);
        editTextemail = (EditText) findViewById(R.id.editText_loginemail);
        editTextpassword = (EditText) findViewById(R.id.editText_loginpassword);

        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CreateUser.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    public void checkLogin() {

        final String userEmail = editTextemail.getText().toString();
        String userPassword = editTextpassword.getText().toString();



        ref.authWithPassword(userEmail, userPassword,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("provider", authData.getProvider());
                        if (authData.getProviderData().containsKey("displayName")) {
                            map.put("displayName", authData.getProviderData().get("displayName").toString());
                        }
                        ref.child("users").child(authData.getUid()).setValue(map);
                        System.out.println("Success");

                        Toast.makeText(LoginActivity.this, "Logged In.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, Conversation.class);
                        intent.putExtra("USER", userEmail);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        System.out.println("Error");
                        switch (error.getCode()) {
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                // handle a non existing user
                                Toast.makeText(LoginActivity.this, "USER DOES NOT EXIST", Toast.LENGTH_SHORT).show();
                                System.out.println("USER_DOES_NOT_EXIST");
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                // handle an invalid password
                                Toast.makeText(LoginActivity.this, "INVALID PASSWORD", Toast.LENGTH_SHORT).show();
                                System.out.println("INVALID_PASSWORD");
                                break;
                            default:
                                // handle other errors
                                System.out.println("Unknown error");
                                break;
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

package hw08.group05.com.stayintouch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateUser extends AppCompatActivity {

    Firebase ref;
    Button buttonsignup,buttoncancel;
    EditText editTextName,editTextEmail,editTextPassword,editTextConfirmPassword,editTextPhoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Firebase.setAndroidContext(this);
        ref = Application.ref;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.logo7);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(R.string.stayintouchsignup);

        editTextName = (EditText) findViewById(R.id.editText_firstlastname);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPhoneno = (EditText) findViewById(R.id.editText_phone);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.editText_confirmpassword);

        buttonsignup = (Button) findViewById(R.id.button_signup);
        buttoncancel = (Button) findViewById(R.id.button_cancel);

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = editTextName.getText().toString();
                final String userEmail = editTextEmail.getText().toString();
                final String userPassword = editTextPassword.getText().toString();
                final String userPhoneNo = editTextPhoneno.getText().toString();
                final String userConfirmPassword = editTextConfirmPassword.getText().toString();

                if (userConfirmPassword.equals(userPassword)){
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.icon_user_default);
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    final byte[] image=stream.toByteArray();
                    System.out.println("byte array:"+image);
                    final String img_str = Base64.encodeToString(image, 0);

                    ref.createUser(userEmail, userPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            ref = ref.child("users/username");
                            Firebase userpush = ref.push();

                            //create object and push it into the database
                            User userObj = new User(userName, userEmail, userPassword,userPhoneNo,img_str,userpush.getKey(),true);

                            userpush.setValue(userObj);

                            Toast.makeText(CreateUser.this, "Account was created.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CreateUser.this, LoginActivity.class);
                            startActivity(intent);

                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        }
                        @Override
                        public void onError(FirebaseError firebaseError) {
                            // there was an error
                            Log.d("test", firebaseError.getMessage());

                            switch (firebaseError.getCode()) {
                                case FirebaseError.EMAIL_TAKEN:
                                    //
                                    editTextName.setError("Account was not created. Please use other email address.");
                                    //Toast.makeText(CreateUser.this, "Account was not created. Please use other email address.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // handle other errors
                                    System.out.println("Can not create user");
                                    break;
                            }
                        }
                    });
                }else{
                    editTextConfirmPassword.setError("Password Does not match");
                }
            }
        });

        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateUser.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

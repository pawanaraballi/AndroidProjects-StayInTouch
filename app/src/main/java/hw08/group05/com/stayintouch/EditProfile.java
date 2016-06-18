package hw08.group05.com.stayintouch;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class EditProfile extends AppCompatActivity {

    Firebase ref;
    String userEmail;
    static User curruser;
    ImageView imageViewdp;
    EditText editTextname,editTextemail,editTextphoneno,editTextpassword;
    Button buttonUpdate,buttonCancel;
    private static int RESULT_LOAD_IMAGE = 1;
    static String strBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ref = Application.ref;

        if(getIntent().getExtras()!=null){
            userEmail = getIntent().getExtras().getString("USER");
        }


        editTextname = (EditText) findViewById(R.id.editText_DetailflName);
        editTextemail = (EditText) findViewById(R.id.editText_DetailEmail);
        editTextphoneno = (EditText) findViewById(R.id.editText_DetailPhoneNo);
        editTextpassword = (EditText) findViewById(R.id.editText_DetailPassword);

        imageViewdp = (ImageView) findViewById(R.id.imageView_Detaildp);

        buttonUpdate = (Button) findViewById(R.id.button_DetailUpdate);
        buttonCancel = (Button) findViewById(R.id.button_DetailCancel);

        ref = ref.child("users/username");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " details");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User iteratoruser = postSnapshot.getValue(User.class);
                    Log.d("useremail", iteratoruser.getUserFullname());
                    if (iteratoruser.getUserEmail().equalsIgnoreCase(userEmail)){
                        Log.d("User",iteratoruser.getUserFullname());
                        curruser = iteratoruser;
                        editTextname.setText(iteratoruser.getUserFullname());
                        editTextemail.setText(iteratoruser.getUserEmail());
                        editTextphoneno.setText(iteratoruser.getPhoneNo());
                        editTextpassword.setText(iteratoruser.getUserPassword());

                        strBase64= iteratoruser.getProfilepic();
                    }
                }

                byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageViewdp.setImageBitmap(decodedByte);

                imageViewdp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed " + firebaseError.getMessage());
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase updateUser = ref.child("users/username");
                User user = new User();
                user.setPhoneNo(editTextphoneno.getText().toString());
                user.setUserEmail(editTextemail.getText().toString());
                user.setUserFullname(editTextname.getText().toString());
                user.setUserPassword(editTextpassword.getText().toString());

                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.id.imageView_Detaildp);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                final byte[] image=stream.toByteArray();
                System.out.println("byte array:"+image);
                final String img_str = Base64.encodeToString(image, 0);

                user.setProfilepic(img_str);

                Toast.makeText(EditProfile.this,"Profile Updated",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditProfile.this, Conversation.class);
                startActivity(intent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, Conversation.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView_Detaildp);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfile.this, Conversation.class);
        startActivity(intent);
        finish();
    }
}

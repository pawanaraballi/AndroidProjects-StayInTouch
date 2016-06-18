package hw08.group05.com.stayintouch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {
    TextView textViewFullname,textViewname,textViewphoneno,textViewemail;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        textViewFullname = (TextView) findViewById(R.id.textView_ViewContactName);
        textViewemail = (TextView) findViewById(R.id.textView_ViewContactemail);
        textViewname = (TextView) findViewById(R.id.textView_ViewContactflname);
        textViewphoneno = (TextView) findViewById(R.id.textView_ViewContactPhoneno);

        imageView = (ImageView) findViewById(R.id.imageView_ViewContactdp);

        textViewFullname.setText(ViewMessage.convouser.getUserFullname());
        textViewphoneno.setText(ViewMessage.convouser.getPhoneNo());
        textViewname.setText(ViewMessage.convouser.getUserFullname());
        textViewemail.setText(ViewMessage.convouser.getUserEmail());

        byte[] decodedString = Base64.decode(ViewMessage.convouser.getProfilepic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewContact.this,ViewMessage.class);
        startActivity(intent);
        finish();
    }
}

package hw08.group05.com.stayintouch;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Pawan on 4/15/2016.
 */
public class ViewMessageAdapter extends ArrayAdapter<Message> {
    List<Message> mData;
    Context mContext;
    int mResource;
    ViewMessage viewMessage;
    final Firebase messageRef = hw08.group05.com.stayintouch.Application.ref.child("messages");

    public ViewMessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        final Message message = mData.get(position);

        TextView textViewName = (TextView) convertView.findViewById(R.id.textView_ViewMessageName);
        textViewName.setText(message.getSenderName());
        TextView textViewMessage = (TextView) convertView.findViewById(R.id.textView_ViewMessageMessage);
        textViewMessage.setText(message.getMessageText());
        TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewViewMessageTime);
        Log.d("getTimeStamp",message.getTimeStamp());
        Long l = Long.valueOf(message.getTimeStamp());
        textViewTime.setText(new Timestamp(l).toString());
        ImageButton imageButtonDelete = (ImageButton) convertView.findViewById(R.id.imageButton_ViewMessageDelete);

        if (Conversation.currUser.getUserFullname().equalsIgnoreCase(message.getReceiverName())){
            imageButtonDelete.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(Color.LTGRAY);
        }
        if (Conversation.currUser.getUserFullname().equalsIgnoreCase(message.getSenderName())){
            convertView.setBackgroundColor(Color.GRAY);
        }

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete functionality
                Firebase deletemessage = messageRef.child(message.getPushId());
                deletemessage.removeValue(null);
            }
        });
        return convertView;
    }
}

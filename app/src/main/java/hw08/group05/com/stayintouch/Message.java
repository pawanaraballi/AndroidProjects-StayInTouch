package hw08.group05.com.stayintouch;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Pawan on 4/15/2016.
 */
public class Message implements Serializable,Parcelable{
    String senderName;
    String receiverName;
    String messageText;
    String timeStamp;
    String pushId;
    boolean messageRead;

    protected Message(Parcel in) {
        senderName = in.readString();
        receiverName = in.readString();
        messageText = in.readString();
        timeStamp = in.readString();
        pushId = in.readString();
        messageRead = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderName);
        dest.writeString(receiverName);
        dest.writeString(messageText);
        dest.writeString(timeStamp);
        dest.writeString(pushId);
        dest.writeByte((byte) (messageRead ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public String toString() {
        return "Message{" +
                "senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", messageText='" + messageText + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", pushId='" + pushId + '\'' +
                ", messageRead=" + messageRead +
                '}';
    }

    public Message(String senderName, String receiverName, String messageText, String timeStamp, String pushId, boolean messageRead) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.messageText = messageText;
        this.timeStamp = timeStamp;
        this.pushId = pushId;
        this.messageRead = messageRead;
    }

    public Message() {

    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public void setMessageRead(boolean messageRead) {
        this.messageRead = messageRead;
    }
}

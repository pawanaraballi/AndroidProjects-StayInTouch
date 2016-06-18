package hw08.group05.com.stayintouch;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.Firebase;

import java.io.Serializable;

/**
 * Created by Pawan on 4/15/2016.
 */
public class User implements Parcelable,Serializable {
    private String userFullname;
    private String userEmail;
    private String userPassword;
    private String phoneNo;
    private String profilepic;
    private String pushId;
    private boolean read = true;

    protected User(Parcel in) {
        userFullname = in.readString();
        userEmail = in.readString();
        userPassword = in.readString();
        phoneNo = in.readString();
        profilepic = in.readString();
        pushId = in.readString();
        read = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userFullname);
        dest.writeString(userEmail);
        dest.writeString(userPassword);
        dest.writeString(phoneNo);
        dest.writeString(profilepic);
        dest.writeString(pushId);
        dest.writeByte((byte) (read ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "userFullname='" + userFullname + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", profilepic='" + profilepic + '\'' +
                ", pushId='" + pushId + '\'' +
                ", read=" + read +
                '}';
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public User(String userFullname, String userEmail, String userPassword, String phoneNo, String profilepic, String pushId, boolean read) {

        this.userFullname = userFullname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.phoneNo = phoneNo;
        this.profilepic = profilepic;
        this.pushId = pushId;
        this.read = read;
    }

    public User() {

    }
}

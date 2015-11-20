package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prakhar1001 on 11/18/2015.
 */
public class ParceableContactInfo implements Parcelable {

    String First_Name;
    String Last_Name;
    String Phone_Number;
    byte[] ContactImage;
    int id;
    int is_favourite;


    protected ParceableContactInfo(Parcel in) {
        id = in.readInt();
        First_Name = in.readString();
        Last_Name = in.readString();
        Phone_Number = in.readString();
       // ContactImage = in.readByteArray();
        is_favourite = in.readInt();
    }

    public static final Creator<ParceableContactInfo> CREATOR = new Creator<ParceableContactInfo>() {
        @Override
        public ParceableContactInfo createFromParcel(Parcel in) {
            return new ParceableContactInfo(in);
        }

        @Override
        public ParceableContactInfo[] newArray(int size) {
            return new ParceableContactInfo[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public byte[] getContactImage() {
        return ContactImage;
    }

    public void setContactImage(byte[] contactImage) {
        ContactImage = contactImage;
    }

    // constructor
    public ParceableContactInfo(int id, String first_name, String last_name, String phone_number, int is_favourite) {
        this.id = id;
        First_Name = first_name;
        Last_Name = last_name;
        Phone_Number = phone_number;
        this.is_favourite = is_favourite;
    }


    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(First_Name);
        parcel.writeString(Last_Name);
        parcel.writeString(Phone_Number);
        parcel.writeByteArray(ContactImage);
        parcel.writeInt(is_favourite);
    }
}

package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prakhar1001 on 11/18/2015.
 */
public class ParceableGroupInfo implements Parcelable {
    public byte[] getContactImage() {
        return ContactImage;
    }

    public void setContactImage(byte[] contactImage) {
        ContactImage = contactImage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String Group_Name;
    String Description;
    byte[] ContactImage;
    int id;

    protected ParceableGroupInfo(Parcel in) {
        id = in.readInt();
        Group_Name = in.readString();
        Description = in.readString();
    }

    public static final Creator<ParceableGroupInfo> CREATOR = new Creator<ParceableGroupInfo>() {
        @Override
        public ParceableGroupInfo createFromParcel(Parcel in) {
            return new ParceableGroupInfo(in);
        }

        @Override
        public ParceableGroupInfo[] newArray(int size) {
            return new ParceableGroupInfo[size];
        }
    };



    // constructor
    public ParceableGroupInfo(int id, String group_name, String description) {
        this.id = id;
        Group_Name = group_name;
        Description = description;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(Group_Name);
        parcel.writeString(Description);
        parcel.writeByteArray(ContactImage);

    }
}

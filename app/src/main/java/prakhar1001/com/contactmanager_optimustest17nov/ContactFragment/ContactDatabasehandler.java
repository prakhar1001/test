package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakhar1001 on 11/17/2015.
 */
public class ContactDatabasehandler extends SQLiteOpenHelper {

    public static final String DataBase_Name = "ContactManager.db";

    public static final String Table_Name = "Contact_Info";
    public static final String First_Name = "First_Name";
    public static final String Last_Name = "Last_Name";
    public static final String Phone_Number = "Phone_Number";
    public static final String Image_Contact = "Image_Contact";
    public static final String Is_Favourite = "Is_Favourite";

    List<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();

    public static final int DataBase_Version = 1;

    private final Context context;
    SQLiteDatabase database_ob;

    private String Contact_Id = "_id";
    String[] cols = {Contact_Id, First_Name, Last_Name, Phone_Number, Image_Contact, Is_Favourite};

    public ContactDatabasehandler(Context context) {
        super(context, DataBase_Name, null, DataBase_Version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + Table_Name +
                "(_id integer primary key AUTOINCREMENT,First_Name text not null,Last_Name text not null,Phone_Number text not null," +
                "Image_Contact blob,Is_Favourite integer DEFAULT 0)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        // Create tables again
        onCreate(db);
    }


    public void Close() {
        database_ob.close();
    }

    public long InsertData(String First_Name, String Last_Name, String Phone_Number, byte[] contactImageByte) {

        database_ob = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(cols[1], First_Name);
        contentValues.put(cols[2], Last_Name);
        contentValues.put(cols[3], Phone_Number);
        contentValues.put(cols[4], contactImageByte);


        long id = database_ob.insert(Table_Name, null, contentValues);
        Close();
        return id;
    }

    public int updateData(int position, int status) {
        database_ob = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cols[5], status);
        int result = database_ob.update(Table_Name, contentValues, Contact_Id + "=" + position + 1, null);
        Close();
        return result;
    }

    public List<ParceableContactInfo> SortByName() throws SQLException {

        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;

        mCursor = database_ob.query(Table_Name, cols,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        contactInfoArrayList = populateArrayList(mCursor);

        Close();
        return contactInfoArrayList;
    }

    private List<ParceableContactInfo> populateArrayList(Cursor mCursor) {
        byte[] byteImage2 = null;
        if (mCursor.moveToFirst()) {
            do {
                ParceableContactInfo parceableContactInfo = new ParceableContactInfo(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getInt(5));
                parceableContactInfo.setId(mCursor.getInt(0));
                parceableContactInfo.setFirst_Name(mCursor.getString(1));
                parceableContactInfo.setLast_Name(mCursor.getString(2));
                parceableContactInfo.setPhone_Number(mCursor.getString(3));
                byteImage2 = mCursor.getBlob(mCursor.getColumnIndex(cols[4]));
                parceableContactInfo.setContactImage(byteImage2);
                parceableContactInfo.setIs_favourite(mCursor.getInt(5));

                // Adding contact to list
                contactInfoArrayList.add(parceableContactInfo);
            } while (mCursor.moveToNext());
        }
        return contactInfoArrayList;
    }


    public List<ParceableContactInfo> QueryByIsFavourite() throws SQLException {
        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;

        mCursor = database_ob.query(Table_Name, cols,
                Is_Favourite + "=" + 1, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        contactInfoArrayList = populateArrayList(mCursor);

        Close();
        return contactInfoArrayList;
    }


    public List<ParceableContactInfo> query(String inputText) throws SQLException {

        database_ob = this.getWritableDatabase();
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {

            //Toast.makeText(context, "No Query Typed,please Type Something..", Toast.LENGTH_LONG).show();

        } else {

            mCursor = database_ob.query(Table_Name, cols, First_Name
                            + " like '%" + inputText + "%'" + "OR " + Last_Name + " like '%" + inputText +
                            "%'" + "OR " + Phone_Number + " like '%" + inputText + "%'", null, null,
                    null, null, null);
        }


            if (mCursor != null) {
                mCursor.moveToFirst();
                contactInfoArrayList = populateArrayList(mCursor);
            }

            Close();
            return contactInfoArrayList;

        }

    public int UpdateContactData(int contact_id, String first_name, String last_name, String phone_number, byte[] contactImageByte) {
        database_ob = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cols[1], first_name);
        contentValues.put(cols[2], last_name);
        contentValues.put(cols[3], phone_number);
        contentValues.put(cols[4], contactImageByte);
        int result = database_ob.update(Table_Name, contentValues, Contact_Id + "=" + contact_id, null);
        Close();
        return result;
    }

    public List<ParceableContactInfo> FindById(int contact_id) throws SQLException {
        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;

        mCursor = database_ob.query(Table_Name, cols,
                Contact_Id + "=" + contact_id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        contactInfoArrayList = populateArrayList(mCursor);

        Close();
        return contactInfoArrayList;
    }
}

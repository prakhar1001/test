package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class GroupDatabasehandler extends SQLiteOpenHelper {

    public static final String DataBase_Name = "ContactManager.db";

    public static final String Table_Name = "Group_Info";
    public static final String Group_Name = "Group_Name";
    public static final String Description = "Description";
    public static final String Image_Contact = "Image_Contact";


    List<ParceableGroupInfo> groupInfoArrayList = new ArrayList<ParceableGroupInfo>();

    public static final int DataBase_Version = 1;

    private final Context context;
    SQLiteDatabase database_ob;

    private String Contact_Id = "_id";
    String[] cols = {Contact_Id, Group_Name, Description, Image_Contact};

    public GroupDatabasehandler(Context context) {
        super(context, DataBase_Name, null, DataBase_Version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + Table_Name +
                "(_id integer primary key AUTOINCREMENT,Group_Name text not null,Description text not null,Image_Contact blob)";
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

    boolean isTableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public List<ParceableGroupInfo> SortByName() throws SQLException {

        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;
        if (isTableExists(database_ob, Table_Name)) {
            mCursor = database_ob.query(Table_Name, cols,
                    null, null, null, null, null);
        }else
        {
            onCreate(database_ob);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor != null) {
            groupInfoArrayList = populateArrayList(mCursor);
        }
        Close();
        return groupInfoArrayList;
    }

    private List<ParceableGroupInfo> populateArrayList(Cursor mCursor) {
        byte[] byteImage2 = null;
        if (mCursor.moveToFirst()) {
            do {
                ParceableGroupInfo parceableGroupInfo = new ParceableGroupInfo(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2));

                parceableGroupInfo.setId(mCursor.getInt(0));
                parceableGroupInfo.setGroup_Name(mCursor.getString(1));
                parceableGroupInfo.setDescription(mCursor.getString(2));
                byteImage2 = mCursor.getBlob(mCursor.getColumnIndex(cols[3]));
                parceableGroupInfo.setContactImage(byteImage2);


                // Adding contact to list
                groupInfoArrayList.add(parceableGroupInfo);
            } while (mCursor.moveToNext());
        }
        return groupInfoArrayList;
    }

    public long InsertGroupData(String group_name, String description, byte[] contactImageByte) {
        database_ob = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(cols[1], group_name);
        contentValues.put(cols[2], description);
        contentValues.put(cols[3], contactImageByte);


        long id = database_ob.insert(Table_Name, null, contentValues);
        Close();
        return id;
    }


    public List<ParceableGroupInfo> query(String inputText) throws SQLException {

        database_ob = this.getWritableDatabase();
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {

            //  Toast.makeText(context, "No Query Typed,please Type Something..", Toast.LENGTH_LONG).show();

        } else {

            mCursor = database_ob.query(Table_Name, cols, Group_Name
                    + " like '%" + inputText + "%'" + "OR " + Description + " like '%" + inputText +
                    "%'", null, null, null, null, null);
        }


        if (mCursor != null) {
            mCursor.moveToFirst();
            groupInfoArrayList = populateArrayList(mCursor);
        }

        Close();
        return groupInfoArrayList;

    }

    public int UpdateGroupData(int contact_id, String group_name, String description, byte[] contactImageByte) {
        database_ob = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cols[1], group_name);
        contentValues.put(cols[2], description);
        contentValues.put(cols[3], contactImageByte);
        int result = database_ob.update(Table_Name, contentValues, Contact_Id + "=" + contact_id, null);
        Close();
        return result;
    }


    public List<ParceableGroupInfo> FindById(int contact_id) throws SQLException {
        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;

        mCursor = database_ob.query(Table_Name, cols,
                Contact_Id + "=" + contact_id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        groupInfoArrayList = populateArrayList(mCursor);

        Close();
        return groupInfoArrayList;
    }
}


package prakhar1001.com.contactmanager_optimustest17nov.Group_Contact_Connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ParceableContactInfo;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.ParceableGroupInfo;

/**
 * Created by Prakhar1001 on 11/21/2015.
 */
public class Group_Contact_ConnectionHandler extends SQLiteOpenHelper {

    public static final String DataBase_Name = "ContactManager.db";

    public static final String Table_Name = "Group_Contact_Link";
    public static final String Table_Id = "Group_Contact_Link_id";
    public static final String Contact_Id = "Contact_id";
    public static final String Group_Id = "Group_id";

    List<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();
    List<ParceableGroupInfo> groupInfoArrayList = new ArrayList<ParceableGroupInfo>();

    public static final int DataBase_Version = 1;

    private final Context context;
    SQLiteDatabase database_ob;

    String[] cols = {Table_Id, Contact_Id, Group_Id};
    private List<ParceableContactInfo> relatedContacts;
    private ArrayList<ParceableGroupInfo> relatedGroups;

    public Group_Contact_ConnectionHandler(Context context) {
        super(context, DataBase_Name, null, DataBase_Version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + Table_Name +
                "(Group_Contact_Link_id integer primary key AUTOINCREMENT,Contact_id INTEGER," +
                "  Group_id INTEGER," +
                "  FOREIGN KEY(Contact_id) REFERENCES Contact_Info(_id)," +
                "  FOREIGN KEY(Group_id) REFERENCES Group_Info(_id))";
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

    public long InsertLinkData(int Contact_id, int Group_id) {

        database_ob = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(cols[1], Contact_id);
        contentValues.put(cols[2], Group_id);
        long id = 0;
        if (isTableExists(database_ob, Table_Name)) {
            id = database_ob.insert(Table_Name, null, contentValues);
        } else {
            onCreate(database_ob);
        }
        Close();
        return id;
    }



    public List<ParceableContactInfo> getRelatedContacts(int contact_id) throws SQLException {
        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;

        mCursor = database_ob.query(Table_Name, cols, Contact_Id + "=" + contact_id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            for (int i = 0; i < mCursor.getCount(); i++) {
                ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(context);
                ArrayList<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();
                contactInfoArrayList = (ArrayList<ParceableContactInfo>) contactDatabasehandler.FindById(mCursor.getInt(2));
                if (contactInfoArrayList != null) {
                    for (int j = 0; j < contactInfoArrayList.size(); j++) {
                        if (contactInfoArrayList.get(j) != null) {
                            relatedContacts = new ArrayList<ParceableContactInfo>();
                            relatedContacts.add(j, contactInfoArrayList.get(j));
                        }
                    }
                }
                mCursor.moveToNext();
            }
        }

        Close();
        return relatedContacts;
    }

    public ArrayList<ParceableGroupInfo> getRelatedGroups(int group_id) throws SQLException {
        database_ob = this.getReadableDatabase();
        Cursor mCursor = null;

        mCursor = database_ob.query(Table_Name, cols, Group_Id + "=" + group_id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            for (int i = 0; i < mCursor.getCount(); i++) {
                GroupDatabasehandler groupDatabasehandler = new GroupDatabasehandler(context);
                ArrayList<ParceableGroupInfo> groupInfoArrayList = new ArrayList<ParceableGroupInfo>();
                groupInfoArrayList = (ArrayList<ParceableGroupInfo>) groupDatabasehandler.FindById(mCursor.getInt(1));
                if (groupInfoArrayList != null) {
                    for (int j = 0; j < groupInfoArrayList.size(); j++) {
                        if (groupInfoArrayList.get(j) != null) {
                            relatedGroups = new ArrayList<ParceableGroupInfo>();
                            relatedGroups.add(j, groupInfoArrayList.get(j));
                        }
                    }
                }
                mCursor.moveToNext();
            }
        }

        Close();
        return relatedGroups;
    }
}

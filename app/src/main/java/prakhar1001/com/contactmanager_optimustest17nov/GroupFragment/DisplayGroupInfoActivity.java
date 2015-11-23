package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ParceableContactInfo;
import prakhar1001.com.contactmanager_optimustest17nov.Group_Contact_Connection.Group_Contact_ConnectionHandler;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class DisplayGroupInfoActivity extends AppCompatActivity {

    ArrayList<ParceableContactInfo> contactInfoArrayList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int contact_id;
        final String Group_Name, Description;
        final byte[] Group_Image;
        ImageView Group_Photo;
        setContentView(R.layout.displaygroupinfo);
        TextView contact_id_text = (TextView) findViewById(R.id.contact_id);
        TextView group_name_text = (TextView) findViewById(R.id.group_name);
        TextView description_text = (TextView) findViewById(R.id.description);
        Group_Photo = (ImageView) findViewById(R.id.Contact_Image);
        Button GroupInfoEdit = (Button) findViewById(R.id.Edit_Group_Info);
        ListView Added_Contacts = (ListView) findViewById(R.id.added_contact_listView);
        TextView NoEntryFound = (TextView) findViewById(R.id.NotextFound);

        Intent intent = getIntent();
        contact_id = intent.getIntExtra("Contact_id", 0);
        Group_Name = intent.getStringExtra("Group_Name");
        Description = intent.getStringExtra("Description");
        Group_Image = intent.getByteArrayExtra("Group_Image");

        contact_id_text.setText(Integer.toString(contact_id));
        group_name_text.setText(Group_Name);
        description_text.setText(Description);

        if (Group_Image != null) {
            Group_Photo.setImageBitmap(BitmapFactory.decodeByteArray(Group_Image, 0,
                    Group_Image.length));
        } else {
            Group_Photo.setImageResource(R.drawable.contect_image);
        }
        GroupInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGroupInfoActivity(contact_id,Group_Name,Description,Group_Image);
            }
        });

        Group_Contact_ConnectionHandler group_contact_connectionHandler= new Group_Contact_ConnectionHandler(this);
         contactInfoArrayList = new ArrayList<ParceableContactInfo>();
        contactInfoArrayList = (ArrayList<ParceableContactInfo>) group_contact_connectionHandler.getRelatedContacts(contact_id);
        if (contactInfoArrayList.size() > 0) {
            NoEntryFound.setVisibility(View.GONE);
            ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(this,
                    R.layout.contactdialoglistlayout, contactInfoArrayList);
            Added_Contacts.setAdapter(contactDatabaseAdapter);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contactInfoArrayList = savedInstanceState.getParcelableArrayList("Linked Contact to Groups List");
    }

    //Edit Button Implementation
    void editGroupInfoActivity(int contact_id, String group_Name, String description, byte[] group_Image) {
        Intent i = new Intent(DisplayGroupInfoActivity.this,EditGroupInfoActivity.class);
        i.putExtra("Contact_id",contact_id);
        i.putExtra("Group_Name",group_Name );
        i.putExtra("Description",description );
        i.putExtra("Group_Image",group_Image );
        startActivity(i);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Linked Contact to Groups List",contactInfoArrayList);
    }
}

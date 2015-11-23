package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

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

import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.ParceableGroupInfo;
import prakhar1001.com.contactmanager_optimustest17nov.Group_Contact_Connection.Group_Contact_ConnectionHandler;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class DisplayContactInfoActivity extends AppCompatActivity {

    ArrayList<ParceableGroupInfo> groupInfoArrayList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int contact_id;
        final String First_Name, Last_Name, Phone_Number;
        final byte[] Contact_Image;
        ImageView Contact_Photo;
        setContentView(R.layout.displaycontactinfo);

        TextView contact_id_text = (TextView) findViewById(R.id.contact_id);
        TextView first_name_text = (TextView) findViewById(R.id.first_name);
        TextView last_name_text = (TextView) findViewById(R.id.last_name);
        TextView phone_number_text = (TextView) findViewById(R.id.phone_number);
        Contact_Photo = (ImageView) findViewById(R.id.Contact_Image);
        ListView Added_Groups = (ListView) findViewById(R.id.added_group_listView);
        TextView NoEntryFound = (TextView) findViewById(R.id.NotextFound);

        Intent intent = getIntent();
        contact_id = intent.getIntExtra("Contact_id", 0);
        First_Name = intent.getStringExtra("First_Name");
        Last_Name = intent.getStringExtra("Last_Name");
        Phone_Number = intent.getStringExtra("Phone_Number");
        Contact_Image = intent.getByteArrayExtra("Group_Image");

        contact_id_text.setText(Integer.toString(contact_id));
        first_name_text.setText(First_Name);
        last_name_text.setText(Last_Name);
        phone_number_text.setText(Phone_Number);

        if (Contact_Image != null) {
            Contact_Photo.setImageBitmap(BitmapFactory.decodeByteArray(Contact_Image, 0,
                    Contact_Image.length));
        } else
            Contact_Photo.setImageResource(R.drawable.contect_image);


        Group_Contact_ConnectionHandler group_contact_connectionHandler = new Group_Contact_ConnectionHandler(this);
        groupInfoArrayList = new ArrayList<ParceableGroupInfo>();
        groupInfoArrayList = group_contact_connectionHandler.getRelatedGroups(contact_id);
        if (groupInfoArrayList.size() > 0 ) {
            NoEntryFound.setVisibility(View.GONE);
            GroupDatabaseAdapter groupDatabaseAdapter = new GroupDatabaseAdapter(this,
                    R.layout.groupdialoglistlayout, groupInfoArrayList);
            Added_Groups.setAdapter(groupDatabaseAdapter);
        }

        Button ContactInfoEdit = (Button) findViewById(R.id.Edit_Contact_Info);
        ContactInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DisplayContactInfoActivity.this, EditContactInfoActivity.class);
                i.putExtra("Contact_id", contact_id);
                i.putExtra("First_Name", First_Name);
                i.putExtra("Last_Name", Last_Name);
                i.putExtra("Phone_Number", Phone_Number);
                i.putExtra("Group_Image", Contact_Image);
                startActivity(i);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Linked Groups to Contact List",groupInfoArrayList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        groupInfoArrayList = savedInstanceState.getParcelableArrayList("Linked Groups to Contact List");
    }
}

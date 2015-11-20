package prakhar1001.com.contactmanager_optimustest17nov.SearchActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.AddContactActivity;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ParceableContactInfo;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.AddGroupActivity;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.ParceableGroupInfo;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class SearchActivity extends AppCompatActivity {

    ArrayList<ParceableContactInfo> contactInfoArrayList;
    ArrayList<ParceableGroupInfo> groupInfoArrayList;
    EditText searchText;
    ListView contactList, groupList;

    int ContactStatus ;
    int GroupStatus ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);

        searchText = (EditText) findViewById(R.id.Search_text_activity);
        searchText.setMaxWidth(searchText.getWidth());

        contactList = (ListView) findViewById(R.id.search_Contact_List);
        groupList = (ListView) findViewById(R.id.search_Group_List);

        ImageButton search_ImageButton = (ImageButton) findViewById(R.id.Search_ImageButton);
        search_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setFocusableInTouchMode(true);
                Toast.makeText(getApplicationContext(), "search activity", Toast.LENGTH_SHORT).show();
                String SearchInputQuery = searchText.getText().toString();
                buildResult(SearchInputQuery);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        buildResult(searchText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GroupStatus = savedInstanceState.getInt("GroupStatus");
        ContactStatus = savedInstanceState.getInt("ContactStatus");
        buildResult(searchText.getText().toString());

    }

    private void buildResult(String Search_Text) {


        ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(getApplicationContext());
        GroupDatabasehandler groupDatabasehandler = new GroupDatabasehandler(getApplicationContext());


        contactInfoArrayList = new ArrayList<ParceableContactInfo>();
        groupInfoArrayList = new ArrayList<ParceableGroupInfo>();
        contactInfoArrayList = (ArrayList<ParceableContactInfo>) contactDatabasehandler.query(Search_Text);
        groupInfoArrayList = (ArrayList<ParceableGroupInfo>) groupDatabasehandler.query(Search_Text);

        Button AddtoContact = (Button) findViewById(R.id.AddtoContact);
        Button ContactQueryResult = (Button) findViewById(R.id.Search_Contact);
        Button AddtoGroup = (Button) findViewById(R.id.AddtoGroup);
        Button GroupQueryResult = (Button) findViewById(R.id.Search_Group);

        if (contactInfoArrayList.size() == 0) {
            //Null ContactList,No result Found,Ad as a Contact
            ContactQueryResult.setVisibility(View.GONE);
            AddtoContact.setVisibility(View.VISIBLE);
            AddtoContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SearchActivity.this, AddContactActivity.class);
                    startActivity(i);
                }
            });
        } else {
            AddtoContact.setVisibility(View.GONE);
            ContactQueryResult.setVisibility(View.VISIBLE);
            if (ContactStatus == 1)
            {
                ContactDatabaseAdapter contactDatabaseAdapter;
                contactDatabaseAdapter = new ContactDatabaseAdapter(SearchActivity.this,
                        R.layout.contactlistlayout, contactInfoArrayList);
                contactList.setAdapter(contactDatabaseAdapter);

            }
            ContactQueryResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContactDatabaseAdapter contactDatabaseAdapter;
                    if (ContactStatus == 0) {
                        ContactStatus = 1;
                        contactDatabaseAdapter = new ContactDatabaseAdapter(SearchActivity.this,
                                R.layout.contactlistlayout, contactInfoArrayList);
                        contactList.setAdapter(contactDatabaseAdapter);
                    } else {
                        ContactStatus = 0;
                        contactList.setAdapter(null);
                    }
                }
            });
        }

        if (groupInfoArrayList.size() == 0) {
            //Null GroupList,No result Found,Ad as a Group
            GroupQueryResult.setVisibility(View.GONE);
            AddtoGroup.setVisibility(View.VISIBLE);
            AddtoGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SearchActivity.this, AddGroupActivity.class);
                    startActivity(i);
                }
            });
        } else {
            AddtoGroup.setVisibility(View.GONE);
            GroupQueryResult.setVisibility(View.VISIBLE);
            if (GroupStatus == 1){
                GroupDatabaseAdapter groupDatabaseAdapter;
                groupDatabaseAdapter = new GroupDatabaseAdapter(SearchActivity.this,
                        R.layout.grouplistlayout, groupInfoArrayList);
                groupList.setAdapter(groupDatabaseAdapter);
            }
            GroupQueryResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GroupDatabaseAdapter groupDatabaseAdapter;
                    if (GroupStatus == 0) {
                        GroupStatus = 1;
                        groupDatabaseAdapter = new GroupDatabaseAdapter(SearchActivity.this,
                                R.layout.grouplistlayout, groupInfoArrayList);
                        groupList.setAdapter(groupDatabaseAdapter);
                    } else {
                        GroupStatus = 0;
                        groupList.setAdapter(null);
                    }
                }
            });

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("GroupStatus", GroupStatus);
        outState.putInt("ContactStatus", ContactStatus);
    }
}

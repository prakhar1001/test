package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/17/2015.
 */
public class ContactFragment extends Fragment {

    ArrayList<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();
    ListView contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactfragment, container, false);
        contactList = (ListView) view.findViewById(R.id.contactlist);


        if (savedInstanceState != null) {
            contactInfoArrayList = savedInstanceState.getParcelableArrayList("contactInfoArrayList");

        } else {
            ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(getActivity());
            contactInfoArrayList = (ArrayList<ParceableContactInfo>) contactDatabasehandler.SortByName();
        }

        ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(getActivity(),
                R.layout.contactlistlayout, contactInfoArrayList);
        contactList.setAdapter(contactDatabaseAdapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view,
                                                                       int position, long id) {

                                                   Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                                                   Intent i = new Intent(getActivity(), DisplayContactInfoActivity.class);
                                                   i.putExtra("Contact_id", contactInfoArrayList.get(position).getId());
                                                   i.putExtra("First_Name", contactInfoArrayList.get(position).getFirst_Name());
                                                   i.putExtra("Last_Name", contactInfoArrayList.get(position).getLast_Name());
                                                   i.putExtra("Phone_Number", contactInfoArrayList.get(position).getPhone_Number());
                                                   i.putExtra("Contact_Image", contactInfoArrayList.get(position).getContactImage());
                                                   getActivity().startActivity(i);
                                               }
                                           }
        );
        

        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddContactActivity.class);
                getActivity().startActivity(i);

            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("ContactFragment", 4);
        outState.putParcelableArrayList("contactInfoArrayList", contactInfoArrayList);
        super.onSaveInstanceState(outState);
    }
}

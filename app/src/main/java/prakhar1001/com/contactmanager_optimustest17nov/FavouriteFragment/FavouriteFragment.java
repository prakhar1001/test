package prakhar1001.com.contactmanager_optimustest17nov.FavouriteFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ParceableContactInfo;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/17/2015.
 */
public class FavouriteFragment extends Fragment {

    ArrayList<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();
    ListView contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favouritefragment, container, false);
        contactList = (ListView) view.findViewById(R.id.contactlist);


        if (savedInstanceState != null) {
            contactInfoArrayList = savedInstanceState.getParcelableArrayList("contactInfoArrayList");

        } else {
            ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(getActivity());
            contactInfoArrayList = (ArrayList<ParceableContactInfo>) contactDatabasehandler.QueryByIsFavourite();
        }

        ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(getActivity(),
                R.layout.contactlistlayout, contactInfoArrayList);
        contactList.setAdapter(contactDatabaseAdapter);



        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("ContactFragment", 4);
        outState.putParcelableArrayList("contactInfoArrayList", contactInfoArrayList);
        super.onSaveInstanceState(outState);
    }
}




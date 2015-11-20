package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/17/2015.
 */
public class GroupFragment extends Fragment {

    ArrayList<ParceableGroupInfo> groupInfoArrayList = new ArrayList<ParceableGroupInfo>();
    ListView groupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groupfragment, container, false);
        groupList = (ListView) view.findViewById(R.id.grouplist);


        if (savedInstanceState != null) {
            groupInfoArrayList = savedInstanceState.getParcelableArrayList("groupInfoArrayList");

        } else {
            GroupDatabasehandler groupDatabasehandler = new GroupDatabasehandler(getActivity());
            groupInfoArrayList = (ArrayList<ParceableGroupInfo>) groupDatabasehandler.SortByName();
        }

        GroupDatabaseAdapter groupDatabaseAdapter = new GroupDatabaseAdapter(getActivity(),
                R.layout.grouplistlayout, groupInfoArrayList);
        groupList.setAdapter(groupDatabaseAdapter);



        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddGroupActivity.class);
                getActivity().startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view,
                                                                     int position, long id) {

                                                 Intent i = new Intent(getActivity(),DisplayGroupInfoActivity.class);
                                                 i.putExtra("Contact_id",groupInfoArrayList.get(position).getId());
                                                 i.putExtra("Group_Name",groupInfoArrayList.get(position).getGroup_Name());
                                                 i.putExtra("Description",groupInfoArrayList.get(position).getDescription());
                                                 i.putExtra("Group_Image",groupInfoArrayList.get(position).getContactImage());
                                                 getActivity().startActivity(i);

                                             }
                                         }
        );
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("GroupFragment", 4);
        outState.putParcelableArrayList("groupInfoArrayList", groupInfoArrayList);
        super.onSaveInstanceState(outState);
    }
}


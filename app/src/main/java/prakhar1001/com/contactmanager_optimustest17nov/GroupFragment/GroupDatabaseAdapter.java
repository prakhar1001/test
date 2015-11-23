package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDialogDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ParceableContactInfo;
import prakhar1001.com.contactmanager_optimustest17nov.Group_Contact_Connection.Group_Contact_ConnectionHandler;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class GroupDatabaseAdapter extends ArrayAdapter<ParceableGroupInfo> {

    Context context;
    ArrayList<ParceableGroupInfo> data = null;
    int layoutResourceId;
    ViewHolder holder;
    View normal;


    public GroupDatabaseAdapter(Context context, int layoutResourceId, ArrayList<ParceableGroupInfo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.Contact_Id = (TextView) row.findViewById(R.id.contact_id);
            holder.Group_Name = (TextView) row.findViewById(R.id.group_name);
            holder.Description = (TextView) row.findViewById(R.id.description);
            holder.Contact_Photo = (ImageView) row.findViewById(R.id.Contact_Image);
            holder.Edit_Group_Info = (ImageButton) row.findViewById(R.id.edit_group_info_imagebutton);
            holder.Add_Contacts = (ImageButton) row.findViewById(R.id.add_contact_imagebutton);
            normal = (View) inflater.inflate(R.layout.dialoglistlayout, parent, false);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ParceableGroupInfo parceableGroupInfo = data.get(position);

        if (holder.Contact_Id != null && holder.Group_Name != null && holder.Description != null && holder.Add_Contacts != null
                && holder.Edit_Group_Info != null) {
            holder.Contact_Id.setText(Integer.toString(parceableGroupInfo.getId()));

            holder.Group_Name.setText(parceableGroupInfo.getGroup_Name());
            holder.Description.setText(parceableGroupInfo.getDescription());

            if (parceableGroupInfo.getContactImage() != null) {
                holder.Contact_Photo.setImageBitmap(BitmapFactory.decodeByteArray(parceableGroupInfo.getContactImage(), 0,
                        parceableGroupInfo.getContactImage().length));
            } else
                holder.Contact_Photo.setImageResource(R.drawable.contect_image);

            holder.Add_Contacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    normal = (View) inflater.inflate(R.layout.dialoglistlayout, null);
                    alertDialog.setCancelable(false);
                    alertDialog.setView(normal);
                    alertDialog.setTitle("Select Contacts");

                    ListView lv = (ListView) normal.findViewById(R.id.ListView);

                    //major change
                    ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(context);
                    ArrayList<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();
                    contactInfoArrayList = (ArrayList<ParceableContactInfo>) contactDatabasehandler.SortByName();
                    final ContactDialogDatabaseAdapter contactDialogDatabaseAdapter = new ContactDialogDatabaseAdapter(context,
                            R.layout.contactdialoglistlayout, contactInfoArrayList);
                    lv.setAdapter(contactDialogDatabaseAdapter);

                    alertDialog.setPositiveButton("Save", new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "You clicked on Save", Toast.LENGTH_SHORT).show();
                                    ArrayList<Integer> Add_to_Group = contactDialogDatabaseAdapter.getAdd_to_group_list();
                                    if (Add_to_Group.size() > 0) {
                                        for (int i = 0; i < Add_to_Group.size(); i++) {
                                            Group_Contact_ConnectionHandler group_contact_connectionHandler = new
                                                    Group_Contact_ConnectionHandler(context);
                                            long id = group_contact_connectionHandler.InsertLinkData(parceableGroupInfo.getId(), Add_to_Group.get(i));

                                            if (id < 0) {
                                                Toast.makeText(context, "Unsuccessful Attempt For " + parceableGroupInfo.getGroup_Name(), Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        }
                                        Toast.makeText(context, "successful Attempt For " + parceableGroupInfo.getGroup_Name(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "You clicked on Save but no Group " +
                                                "has been added yet for contact" + parceableGroupInfo.getGroup_Name(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    alertDialog.setNegativeButton("Cancel", new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "You clicked on Cancel", Toast.LENGTH_SHORT).show();

                                }
                            });


                    alertDialog.show();

                }
            });

            holder.Edit_Group_Info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, EditGroupInfoActivity.class);
                    i.putExtra("Contact_id", parceableGroupInfo.getId());
                    i.putExtra("Group_Name", parceableGroupInfo.getGroup_Name());
                    i.putExtra("Description", parceableGroupInfo.getDescription());
                    i.putExtra("Group_Image", parceableGroupInfo.getContactImage());
                    context.startActivity(i);
                }
            });
        }
        return row;

    }


    class ViewHolder {
        TextView Contact_Id;
        TextView Group_Name;
        TextView Description;
        ImageView Contact_Photo;
        ImageButton Edit_Group_Info;
        ImageButton Add_Contacts;

    }


}

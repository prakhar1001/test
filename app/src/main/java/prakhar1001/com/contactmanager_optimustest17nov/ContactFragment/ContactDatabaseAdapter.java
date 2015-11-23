package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

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

import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDialogDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.ParceableGroupInfo;
import prakhar1001.com.contactmanager_optimustest17nov.Group_Contact_Connection.Group_Contact_ConnectionHandler;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/18/2015.
 */
public class ContactDatabaseAdapter extends ArrayAdapter<ParceableContactInfo> {

    Context context;
    ArrayList<ParceableContactInfo> data = null;
    int layoutResourceId;
    ViewHolder holder;
    View normal;


    public ContactDatabaseAdapter(Context context, int layoutResourceId, ArrayList<ParceableContactInfo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.Contact_Id = (TextView) row.findViewById(R.id.contact_id);
            holder.First_Name = (TextView) row.findViewById(R.id.first_name);
            holder.Last_Name = (TextView) row.findViewById(R.id.last_name);
            holder.Phone_Number = (TextView) row.findViewById(R.id.phone_number);
            holder.Contact_Photo = (ImageView) row.findViewById(R.id.Contact_Image);
            holder.Is_Favourite = (ImageButton) row.findViewById(R.id.is_favourite_imagebutton);
            holder.Add_to_Group = (ImageButton) row.findViewById(R.id.add_to_group_imagebutton);
            holder.Edit_Contact_Info = (ImageButton) row.findViewById(R.id.edit_contact_info_imagebutton);
            normal = (View) inflater.inflate(R.layout.dialoglistlayout, parent, false);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ParceableContactInfo parceableContactInfo = data.get(position);

        holder.Contact_Id.setText(Integer.toString(parceableContactInfo.getId()));
        holder.First_Name.setText(parceableContactInfo.getFirst_Name());
        holder.Last_Name.setText(parceableContactInfo.getLast_Name());
        holder.Phone_Number.setText(parceableContactInfo.getPhone_Number());

        if (parceableContactInfo.getContactImage() != null) {
            holder.Contact_Photo.setImageBitmap(BitmapFactory.decodeByteArray(parceableContactInfo.getContactImage(), 0,
                    parceableContactInfo.getContactImage().length));
        } else
            holder.Contact_Photo.setImageResource(R.drawable.contect_image);

        if (holder.Is_Favourite != null) {
            if (parceableContactInfo.getIs_favourite() == 1) {
                holder.Is_Favourite.setImageResource(R.drawable.ic_filled_star);
            }
            if (parceableContactInfo.getIs_favourite() == 0) {
                holder.Is_Favourite.setImageResource(R.drawable.ic_star);
            }


            holder.Is_Favourite.setOnClickListener(new View.OnClickListener() {
                                                       public void onClick(View v) {
                                                           ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(context);
                                                           int status;
                                                           switch (parceableContactInfo.getIs_favourite()) {
                                                               case 0: {
                                                                   status = 1;
                                                                   if (contactDatabasehandler.updateData(position, status) > 0) {
                                                                       parceableContactInfo.setIs_favourite(1);
                                                                       holder.Is_Favourite.setImageResource(R.drawable.ic_filled_star);
                                                                       Toast.makeText(context, "Checked For Favourite:" + position, Toast.LENGTH_SHORT).show();
                                                                   }
                                                                   break;
                                                               }
                                                               case 1: {
                                                                   status = 0;
                                                                   if (contactDatabasehandler.updateData(position, status) > 0) {
                                                                       parceableContactInfo.setIs_favourite(0);
                                                                       holder.Is_Favourite.setImageResource(R.drawable.ic_star);
                                                                       Toast.makeText(context, "UnChecked For Favourite:" + position, Toast.LENGTH_SHORT).show();
                                                                   }
                                                                   break;
                                                               }
                                                           }
                                                       }
                                                   }
            );
        }

        holder.Add_to_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                normal = (View) inflater.inflate(R.layout.dialoglistlayout, null);
                alertDialog.setCancelable(false);
                alertDialog.setView(normal);
                alertDialog.setTitle("Select Groups");

                ListView lv = (ListView) normal.findViewById(R.id.ListView);

                GroupDatabasehandler groupDatabasehandler = new GroupDatabasehandler(context);
                ArrayList<ParceableGroupInfo> groupInfoArrayList = new ArrayList<ParceableGroupInfo>();
                groupInfoArrayList = (ArrayList<ParceableGroupInfo>) groupDatabasehandler.SortByName();
                final GroupDialogDatabaseAdapter groupDialogDatabaseAdapter = new GroupDialogDatabaseAdapter(context,
                        R.layout.groupdialoglistlayout, groupInfoArrayList);
                lv.setAdapter(groupDialogDatabaseAdapter);

                alertDialog.setPositiveButton("Save", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<Integer> Add_to_Contact = groupDialogDatabaseAdapter.getAdd_to_contact_list();
                                //for every item in the group list update the table with corresponding contact
                                if (Add_to_Contact.size() > 0) {
                                    for (int i = 0; i < Add_to_Contact.size(); i++) {
                                        Group_Contact_ConnectionHandler group_contact_connectionHandler = new
                                                Group_Contact_ConnectionHandler(context);
                                        long id = group_contact_connectionHandler.InsertLinkData(parceableContactInfo.getId(), Add_to_Contact.get(i));

                                        if (id < 0) {
                                            Toast.makeText(context, "Unsuccessful Attempt For " + parceableContactInfo.getFirst_Name(), Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    Toast.makeText(context, "successful Attempt For " + parceableContactInfo.getFirst_Name(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "You clicked on Save but no Group " +
                                            "has been added yet for contact" + parceableContactInfo.getFirst_Name(), Toast.LENGTH_SHORT).show();
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

        if (holder.Edit_Contact_Info != null) {
            holder.Edit_Contact_Info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, EditContactInfoActivity.class);
                    i.putExtra("Contact_id", parceableContactInfo.getId());
                    i.putExtra("First_Name", parceableContactInfo.getFirst_Name());
                    i.putExtra("Last_Name", parceableContactInfo.getLast_Name());
                    i.putExtra("Phone_Number", parceableContactInfo.getPhone_Number());
                    i.putExtra("Group_Image", parceableContactInfo.getContactImage());
                    context.startActivity(i);
                }
            });
        }

        return row;
    }


    class ViewHolder {
        TextView Contact_Id;
        TextView First_Name;
        TextView Last_Name;
        TextView Phone_Number;
        ImageView Contact_Photo;
        ImageButton Is_Favourite;
        ImageButton Add_to_Group;
        ImageButton Edit_Contact_Info;

    }


}

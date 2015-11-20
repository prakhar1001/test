package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/18/2015.
 */
public class ContactDatabaseAdapter extends ArrayAdapter<ParceableContactInfo> {

    Context context;
    ArrayList<ParceableContactInfo> data = null;
    int layoutResourceId;
    ViewHolder holder;


    public ContactDatabaseAdapter(Context context, int layoutResourceId, ArrayList<ParceableContactInfo> data) {
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
            holder.First_Name = (TextView) row.findViewById(R.id.first_name);
            holder.Last_Name = (TextView) row.findViewById(R.id.last_name);
            holder.Phone_Number = (TextView) row.findViewById(R.id.phone_number);
            holder.Contact_Photo = (ImageView) row.findViewById(R.id.Contact_Image);
            /*holder.Is_Favourite = (ImageButton) row.findViewById(R.id.is_favourite_imagebutton);
            holder.Add_to_Group = (ImageButton) row.findViewById(R.id.add_to_group_imagebutton);
*/
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

       /* if (parceableContactInfo.getIs_favourite() == 1) {
            holder.Is_Favourite.setImageResource(R.drawable.ic_filled_star);
        }
        if (parceableContactInfo.getIs_favourite() == 0) {
            holder.Is_Favourite.setImageResource(R.drawable.ic_star);
        }*/


        /*holder.Is_Favourite.setOnClickListener(new View.OnClickListener() {
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
*/

        return row;
    }


    class ViewHolder {
        TextView Contact_Id;
        TextView First_Name;
        TextView Last_Name;
        TextView Phone_Number;
        ImageView Contact_Photo;
       /* ImageButton Is_Favourite;
        ImageButton Add_to_Group;*/

    }


}

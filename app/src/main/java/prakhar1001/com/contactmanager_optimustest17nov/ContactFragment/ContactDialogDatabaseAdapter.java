package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/18/2015.
 */
public class ContactDialogDatabaseAdapter extends ArrayAdapter<ParceableContactInfo> {

    Context context;
    ArrayList<ParceableContactInfo> data = null;
    int layoutResourceId;
    ViewHolder holder;
    View normal;
    ArrayList<Integer> add_to_group_list = null;


    public ContactDialogDatabaseAdapter(Context context, int layoutResourceId, ArrayList<ParceableContactInfo> data) {
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
            holder.Group_Thumb_up = (ImageButton) row.findViewById(R.id.add_to_group_imagebutton);
            holder.Group_Thumb_down = (ImageButton) row.findViewById(R.id.remove_from_group_imagebutton);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ParceableContactInfo parceableContactInfo = data.get(position);
        add_to_group_list = new ArrayList<Integer>();

        holder.Contact_Id.setText(Integer.toString(parceableContactInfo.getId()));
        holder.First_Name.setText(parceableContactInfo.getFirst_Name());
        holder.Last_Name.setText(parceableContactInfo.getLast_Name());
        holder.Phone_Number.setText(parceableContactInfo.getPhone_Number());

        if (parceableContactInfo.getContactImage() != null) {
            holder.Contact_Photo.setImageBitmap(BitmapFactory.decodeByteArray(parceableContactInfo.getContactImage(), 0,
                    parceableContactInfo.getContactImage().length));
        } else
            holder.Contact_Photo.setImageResource(R.drawable.contect_image);


        holder.Group_Thumb_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Contact Thumb up" , Toast.LENGTH_SHORT).show();
                add_to_group_list.add(parceableContactInfo.getId());
            }
        });


        holder.Group_Thumb_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Contact Thumb down", Toast.LENGTH_SHORT).show();
                add_to_group_list.remove(parceableContactInfo.getId());

            }
        });

        return row;
    }

    public ArrayList<Integer> getAdd_to_group_list() {
        return add_to_group_list;
    }

    class ViewHolder {
        TextView Contact_Id;
        TextView First_Name;
        TextView Last_Name;
        TextView Phone_Number;
        ImageView Contact_Photo;
        ImageButton Group_Thumb_up;
        ImageButton Group_Thumb_down;


    }


}

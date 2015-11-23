package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

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
 * Created by Prakhar1001 on 11/19/2015.
 */
public class GroupDialogDatabaseAdapter extends ArrayAdapter<ParceableGroupInfo> {

    Context context;
    ArrayList<ParceableGroupInfo> data = null;
    int layoutResourceId;
    ViewHolder holder;
    View normal;
    ArrayList<Integer> add_to_contact_list = null;

    public GroupDialogDatabaseAdapter(Context context, int layoutResourceId, ArrayList<ParceableGroupInfo> data) {
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
            holder.Group_Thumb_up = (ImageButton) row.findViewById(R.id.add_to_group_imagebutton);
            holder.Group_Thumb_down = (ImageButton) row.findViewById(R.id.remove_from_group_imagebutton);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ParceableGroupInfo parceableGroupInfo = data.get(position);
        add_to_contact_list = new ArrayList<Integer>();

        holder.Contact_Id.setText(Integer.toString(parceableGroupInfo.getId()));
        holder.Group_Name.setText(parceableGroupInfo.getGroup_Name());
        holder.Description.setText(parceableGroupInfo.getDescription());

        if (parceableGroupInfo.getContactImage() != null) {
            holder.Contact_Photo.setImageBitmap(BitmapFactory.decodeByteArray(parceableGroupInfo.getContactImage(), 0,
                    parceableGroupInfo.getContactImage().length));
        } else
            holder.Contact_Photo.setImageResource(R.drawable.contect_image);

        holder.Group_Thumb_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Group Thumb up" + position, Toast.LENGTH_SHORT).show();
                add_to_contact_list.add(parceableGroupInfo.getId());
            }
        });


        holder.Group_Thumb_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Group Thumb down" , Toast.LENGTH_SHORT).show();
                add_to_contact_list.remove((Integer) parceableGroupInfo.getId());
            }
        });

        return row;

    }

    public ArrayList<Integer> getAdd_to_contact_list() {
        return add_to_contact_list;
    }

    class ViewHolder {
        TextView Contact_Id;
        TextView Group_Name;
        TextView Description;
        ImageView Contact_Photo;
        ImageButton Group_Thumb_up;
        ImageButton Group_Thumb_down;

    }


}

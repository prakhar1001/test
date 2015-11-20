package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

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

import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.ParceableGroupInfo;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class GroupDatabaseAdapter extends ArrayAdapter<ParceableGroupInfo> {

    Context context;
    ArrayList<ParceableGroupInfo> data = null;
    int layoutResourceId;
    ViewHolder holder;


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

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ParceableGroupInfo parceableGroupInfo = data.get(position);

        holder.Contact_Id.setText(Integer.toString(parceableGroupInfo.getId()));
        holder.Group_Name.setText(parceableGroupInfo.getGroup_Name());
        holder.Description.setText(parceableGroupInfo.getDescription());

        if (parceableGroupInfo.getContactImage() != null) {
            holder.Contact_Photo.setImageBitmap(BitmapFactory.decodeByteArray(parceableGroupInfo.getContactImage(), 0,
                    parceableGroupInfo.getContactImage().length));
        } else
            holder.Contact_Photo.setImageResource(R.drawable.contect_image);





        return row;
    }


    class ViewHolder {
        TextView Contact_Id;
        TextView Group_Name;
        TextView Description;
        ImageView Contact_Photo;

    }


}

package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ContactDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.ContactFragment.ParceableContactInfo;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class EditGroupInfoActivity extends AppCompatActivity{

    EditText description_text,group_name_text;
    GroupDatabasehandler groupDatabasehandler;
    TextInputLayout inputLayoutGrouptName, inputLayoutDescription;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    ImageView Group_Photo;
    int contact_id;
    byte contactImageByte[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupDatabasehandler = new GroupDatabasehandler(this);

        setContentView(R.layout.addgroupactivity);


        inputLayoutGrouptName = (TextInputLayout) findViewById(R.id.input_layout_groupname);
        group_name_text = (EditText) findViewById(R.id.input_groupname);

        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_description);
        description_text = (EditText) findViewById(R.id.input_description);

        group_name_text.addTextChangedListener((TextWatcher) new MyTextWatcher(group_name_text));
        description_text.addTextChangedListener(new MyTextWatcher(description_text));

        FloatingActionButton contactphoto = (FloatingActionButton) findViewById(R.id.add_photo);
        contactphoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        SELECT_PICTURE);
            }
        });


        Group_Photo = (ImageView) findViewById(R.id.ImageGroup);
        ListView ContactListView = (ListView) findViewById(R.id.contactlistView);

        Intent intent = getIntent();
        contact_id = intent.getIntExtra("Contact_id", 0);
        String Group_Name = intent.getStringExtra("Group_Name");
        String Description = intent.getStringExtra("Description");
        byte[] Group_Image = intent.getByteArrayExtra("Group_Image");


        group_name_text.setText(Group_Name);
        description_text.setText(Description);

        if (Group_Image != null) {
            Group_Photo.setImageBitmap(BitmapFactory.decodeByteArray(Group_Image, 0,
                    Group_Image.length));
        } else {
            Group_Photo.setImageResource(R.drawable.contect_image);
        }

        Button editgroupbutton = (Button) findViewById(R.id.add_button);

        editgroupbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                updateForm(groupDatabasehandler);
            }
        });

        ContactDatabasehandler contactDatabasehandler = new ContactDatabasehandler(this);
        ArrayList<ParceableContactInfo> contactInfoArrayList = new ArrayList<ParceableContactInfo>();
        contactInfoArrayList = (ArrayList<ParceableContactInfo>) contactDatabasehandler.SortByName();
        ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(this,
                R.layout.contactlistlayout, contactInfoArrayList);
        ContactListView.setAdapter(contactDatabaseAdapter);




    }

    private void updateForm(GroupDatabasehandler groupDatabasehandler) {
        String Group_Name, Description ;

        if (!validateGroupName()) {
            return;
        }
        if (!validateDescription()) {
            return;
        }

        Group_Name = group_name_text.getText().toString();
        Description = description_text.getText().toString();


        if (selectedImagePath != null) {
            try {
                FileInputStream instream = new FileInputStream(selectedImagePath);
                BufferedInputStream bif = new BufferedInputStream(instream);
                contactImageByte = new byte[bif.available()];
                bif.read(contactImageByte);
            } catch (IOException e) {
                Toast.makeText(this, "IO Exception", Toast.LENGTH_LONG).show();
            }
        }

        //Redundant data is still unchecked
        if (((Group_Name) != null) && ((Description) != null)) {

            int id = groupDatabasehandler.UpdateGroupData(contact_id,Group_Name, Description, contactImageByte);
            if (id < 0) {
                Toast.makeText(this, "Unsuccessful attempt!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successful attempt!"+id+"Row Updated", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "Null Elemnets are not allowed", Toast.LENGTH_SHORT).show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                Group_Photo.setVisibility(View.VISIBLE);
                Group_Photo.setImageURI(selectedImageUri);

            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private boolean validateGroupName() {
        if (group_name_text.getText().toString().trim().isEmpty()) {
            inputLayoutGrouptName.setError(getString(R.string.err_msg_group_name));
            requestFocus(group_name_text);
            return false;
        } else {
            inputLayoutGrouptName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        if (description_text.getText().toString().trim().isEmpty()) {
            inputLayoutDescription.setError(getString(R.string.err_msg_description));
            requestFocus(description_text);
            return false;
        } else {
            inputLayoutDescription.setErrorEnabled(false);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_firstname:
                    validateGroupName();
                    break;
                case R.id.input_lastname:
                    validateDescription();
                    break;


            }
        }
    }
}

package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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

import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabaseAdapter;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.GroupDatabasehandler;
import prakhar1001.com.contactmanager_optimustest17nov.GroupFragment.ParceableGroupInfo;
import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/19/2015.
 */
public class EditContactInfoActivity extends Activity {


    EditText inputFirstName, inputLastName, inputPhoneNumber;
    TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutPhoneNumber;

    ContactDatabasehandler contactDatabasehandler;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    ImageView Group_Photo;
    int contact_id;
    byte contactImageByte[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactDatabasehandler = new ContactDatabasehandler(this);

        setContentView(R.layout.addcontactactivity);

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);
        inputFirstName = (EditText) findViewById(R.id.input_firstname);

        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_lastname);
        inputLastName = (EditText) findViewById(R.id.input_lastname);

        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phonenumber);
        inputPhoneNumber = (EditText) findViewById(R.id.input_phonenumber);

        inputFirstName.addTextChangedListener((TextWatcher) new MyTextWatcher(inputFirstName));
        inputLastName.addTextChangedListener(new MyTextWatcher(inputLastName));
        inputPhoneNumber.addTextChangedListener(new MyTextWatcher(inputPhoneNumber));

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


        Group_Photo = (ImageView) findViewById(R.id.ImageContact);

        ListView ContactListView = (ListView) findViewById(R.id.grouplistview);


        Intent intent = getIntent();
        contact_id = intent.getIntExtra("Contact_id", 0);
        String First_Name = intent.getStringExtra("First_Name");
        String Last_Name = intent.getStringExtra("Last_Name");
        String Phone_Number = intent.getStringExtra("Phone_Number");
        byte[] Group_Image = intent.getByteArrayExtra("Contact_Image");


        inputFirstName.setText(First_Name);
        inputLastName.setText(Last_Name);
        inputPhoneNumber.setText(Phone_Number);

        if (Group_Image != null) {
            Group_Photo.setImageBitmap(BitmapFactory.decodeByteArray(Group_Image, 0,
                    Group_Image.length));
        } else {
            Group_Photo.setImageResource(R.drawable.contect_image);
        }

        Button editcontactbutton = (Button) findViewById(R.id.add_button);

        editcontactbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                updateForm(contactDatabasehandler);
            }
        });

        GroupDatabasehandler groupDatabasehandler = new GroupDatabasehandler(this);
        ArrayList<ParceableGroupInfo> groupInfoArrayList = new ArrayList<ParceableGroupInfo>();
        groupInfoArrayList = (ArrayList<ParceableGroupInfo>) groupDatabasehandler.SortByName();
        GroupDatabaseAdapter groupDatabaseAdapter = new GroupDatabaseAdapter(this,
                R.layout.grouplistlayout, groupInfoArrayList);
        ContactListView.setAdapter(groupDatabaseAdapter);




    }

    private void updateForm(ContactDatabasehandler contactDatabasehandler) {
        String First_Name, Last_Name, Phone_Number;

        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        if (!validatePhoneNumber()) {
            return;
        }
        First_Name = inputFirstName.getText().toString();
        Last_Name = inputLastName.getText().toString();
        Phone_Number = inputPhoneNumber.getText().toString();

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
        if (((First_Name) != null) && ((Last_Name) != null) && ((Phone_Number) != null) ) {
            int id = contactDatabasehandler.UpdateContactData(contact_id,First_Name, Last_Name, Phone_Number, contactImageByte);
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


    private boolean validateFirstName() {
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            inputLayoutFirstName.setError(getString(R.string.err_msg_first_name));
            requestFocus(inputFirstName);
            return false;
        } else {
            inputLayoutFirstName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName() {
        if (inputLastName.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(getString(R.string.err_msg_last_name));
            requestFocus(inputLastName);
            return false;
        } else {
            inputLayoutLastName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhoneNumber() {
        if (inputPhoneNumber.getText().toString().trim().isEmpty()) {
            inputLayoutPhoneNumber.setError(getString(R.string.err_msg_phone_number));
            requestFocus(inputPhoneNumber);
            return false;
        } else {
            inputLayoutPhoneNumber.setErrorEnabled(false);
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
                    validateFirstName();
                    break;
                case R.id.input_lastname:
                    validateLastName();
                    break;
                case R.id.input_phonenumber:
                    validatePhoneNumber();
                    break;


            }
        }
    }

}



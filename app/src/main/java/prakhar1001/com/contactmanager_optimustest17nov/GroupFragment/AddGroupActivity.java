package prakhar1001.com.contactmanager_optimustest17nov.GroupFragment;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import prakhar1001.com.contactmanager_optimustest17nov.R;

/**
 * Created by Prakhar1001 on 11/18/2015.
 */
public class AddGroupActivity extends AppCompatActivity{

    EditText inputGroupName, inputDescription;
    TextInputLayout inputLayoutGrouptName, inputLayoutDescription;
    GroupDatabasehandler groupDatabasehandler;
    private String selectedImagePath;
    private static final int SELECT_PICTURE = 1;
    ImageView image1;
    byte contactImageByte[];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addgroupactivity);

        groupDatabasehandler = new GroupDatabasehandler(this);
        image1 = (ImageView) findViewById(R.id.ImageGroup);

        inputLayoutGrouptName = (TextInputLayout) findViewById(R.id.input_layout_groupname);
        inputGroupName = (EditText) findViewById(R.id.input_groupname);

        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_description);
        inputDescription = (EditText) findViewById(R.id.input_description);


        inputGroupName.addTextChangedListener((TextWatcher) new MyTextWatcher(inputGroupName));
        inputDescription.addTextChangedListener(new MyTextWatcher(inputDescription));

        Button addgroupbutton = (Button) findViewById(R.id.add_button);

        addgroupbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                submitForm(groupDatabasehandler);
            }
        });

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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                image1.setVisibility(View.VISIBLE);
                image1.setImageURI(selectedImageUri);

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

    private void submitForm(GroupDatabasehandler groupDatabasehandler) {
        String Group_Name, Description ;

        if (!validateGroupName()) {
            return;
        }
        if (!validateDescription()) {
            return;
        }

        Group_Name = inputGroupName.getText().toString();
        Description = inputDescription.getText().toString();


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

            long id = groupDatabasehandler.InsertGroupData(Group_Name, Description, contactImageByte);
            if (id < 0) {
                Toast.makeText(this, "Unsuccessful attempt!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successful attempt!", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "Null Elemnets are not allowed", Toast.LENGTH_SHORT).show();
    }

    private boolean validateGroupName() {
        if (inputGroupName.getText().toString().trim().isEmpty()) {
            inputLayoutGrouptName.setError(getString(R.string.err_msg_group_name));
            requestFocus(inputGroupName);
            return false;
        } else {
            inputLayoutGrouptName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        if (inputDescription.getText().toString().trim().isEmpty()) {
            inputLayoutDescription.setError(getString(R.string.err_msg_description));
            requestFocus(inputDescription);
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

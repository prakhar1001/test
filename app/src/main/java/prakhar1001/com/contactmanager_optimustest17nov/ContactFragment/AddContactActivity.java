package prakhar1001.com.contactmanager_optimustest17nov.ContactFragment;

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
 * Created by Prakhar1001 on 11/17/2015.
 */
public class AddContactActivity extends AppCompatActivity {

    EditText inputFirstName, inputLastName, inputPhoneNumber;
    TextInputLayout inputLayoutFirstName, inputLayoutLastName, inputLayoutPhoneNumber;
    ContactDatabasehandler contactDatabasehandler;
    private String selectedImagePath;
    private static final int SELECT_PICTURE = 1;
    ImageView image1;
    byte contactImageByte[];
    Uri selectedImageUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontactactivity);

        contactDatabasehandler = new ContactDatabasehandler(this);
        image1 = (ImageView) findViewById(R.id.ImageContact);

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_firstname);
        inputFirstName = (EditText) findViewById(R.id.input_firstname);

        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_lastname);
        inputLastName = (EditText) findViewById(R.id.input_lastname);

        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_phonenumber);
        inputPhoneNumber = (EditText) findViewById(R.id.input_phonenumber);


        inputFirstName.addTextChangedListener((TextWatcher) new MyTextWatcher(inputFirstName));
        inputLastName.addTextChangedListener(new MyTextWatcher(inputLastName));
        inputPhoneNumber.addTextChangedListener(new MyTextWatcher(inputPhoneNumber));

        Button addcontactbutton = (Button) findViewById(R.id.add_button);

        addcontactbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                submitForm(contactDatabasehandler);
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
                selectedImageUri = data.getData();
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
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void submitForm(ContactDatabasehandler contactDatabasehandler) {
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
        if (((First_Name) != null) && ((Last_Name) != null) && ((Phone_Number) != null)) {
            long id = contactDatabasehandler.InsertData(First_Name, Last_Name, Phone_Number, contactImageByte);
            if (id < 0) {
                Toast.makeText(this, "Unsuccessful attempt!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successful attempt!", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "Null Elemnets are not allowed", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putByteArray("contactImageByte", contactImageByte);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contactImageByte = savedInstanceState.getByteArray("contactImageByte");


    }


}


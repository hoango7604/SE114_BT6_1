package com.se114.hoango7604.se114_bt6_1;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private Button btnChangeImage;
    private ImageView imgProfile;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private RadioGroup rdgGender;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private Button btnSave;
    private Button btnCancel;

    private File output;
    private String name;
    private String email;
    private String phone;
    private String gender;

    private static final int CAMERA_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        clickHandle();
    }

    private void clickHandle() {
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");

                output = new File(dir, "CameraDemo.jpeg");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();

                try {
                    FileOutputStream out = openFileOutput("dulieu.txt", Context.MODE_PRIVATE);
                    OutputStreamWriter writer = new OutputStreamWriter(out);
                    writer.write("Name\n" + name + "\nEmail\n" + email + "\nPhone\n" + phone + "\nGender\n" + gender);
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        btnChangeImage = findViewById(R.id.btnChangeImage);
        imgProfile = findViewById(R.id.imgProfile);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        rdgGender = findViewById(R.id.rdgGender);
        rdbMale = findViewById(R.id.rdbMale);
        rdbFemale = findViewById(R.id.rdbFemale);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        name = "";
        email = "";
        phone = "";
        gender = "";

        if (!getBaseContext().getFileStreamPath("dulieu.txt").exists()){
            File file = new File(getBaseContext().getFilesDir(), "dulieu.txt");
        }
        else {
            try {
                FileInputStream in = openFileInput("dulieu.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null){
                    switch (line){
                        case "Name":
                            name = reader.readLine();
                            edtName.setText(name);
                            break;
                        case "Email":
                            email = reader.readLine();
                            edtEmail.setText(email);
                            break;
                        case "Phone":
                            phone = reader.readLine();
                            edtPhone.setText(phone);
                            break;
                        case "Gender":
                            gender = reader.readLine();
                            if (gender.equals("Male")){
                                rdgGender.check(rdgGender.getChildAt(1).getId());
                            }
                            else {
                                rdgGender.check(rdgGender.getChildAt(0).getId());
                            }
                            break;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRadioButtonCheck(View view){
        switch (view.getId()){
            case R.id.rdbFemale:
                if (((RadioButton) view).isChecked()){
                    gender = "Female";
                }
                break;
            case R.id.rdbMale:
                if (((RadioButton) view).isChecked()){
                    gender = "Male";
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imgProfile.setImageBitmap(bitmap);
    }
}
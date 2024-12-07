package com.example.passwordmanager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Update_Activity extends AppCompatActivity {


    EditText title_input, username_input, password_input, url_input;
    Button update_button, delete_button, reveal_button, copy_button, copy_button_username;

    String id, title, username, password, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        title_input = findViewById(R.id.update_title_input);
        username_input = findViewById(R.id.update_username_input);
        password_input = findViewById(R.id.update_password_input);
        url_input = findViewById(R.id.update_url_input);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        reveal_button = findViewById(R.id.reveal_button);
        copy_button = findViewById(R.id.copy_button);
        copy_button_username = findViewById(R.id.copy_button_username);

        reveal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_input.getInputType()==InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    password_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }

        });

        copy_button_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", username_input.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(Update_Activity.this, "Username copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", password_input.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(Update_Activity.this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });


        getAndSetIntentData();
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(Update_Activity.this);
                title = title_input.getText().toString().trim();
                username = username_input.getText().toString().trim();
                password = password_input.getText().toString().trim();
                url = url_input.getText().toString().trim();
                myDB.updateData(id, title, username, password, url);
                finish();

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();

            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("_id") && getIntent().hasExtra("title") && getIntent().hasExtra("username")){
            //get data from intent
            id = getIntent().getStringExtra("_id");
            title = getIntent().getStringExtra("title");
            username = getIntent().getStringExtra("username");
            password = getIntent().getStringExtra("password");
            url = getIntent().getStringExtra("url");

            //set intent data
            title_input.setText(title);
            username_input.setText(username);
            password_input.setText(password);
            url_input.setText(url);

        }else {
             Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }


    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Update_Activity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();


    }


}
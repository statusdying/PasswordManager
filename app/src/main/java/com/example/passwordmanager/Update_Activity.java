package com.example.passwordmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Update_Activity extends AppCompatActivity {


    EditText title_input, username_input, password_input, url_input;
    Button update_button;

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
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getAndSetIntentData();
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
}
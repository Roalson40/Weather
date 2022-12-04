package com.example.weather.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Data.Data;
import com.example.weather.Data.FireBaseData;
import com.example.weather.Model.User;
import com.example.weather.R;

public class Register extends AppCompatActivity {

    FireBaseData data = new Data();
    TextView username;
    TextView password;
    TextView passwordAgain;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        passwordAgain = findViewById(R.id.editTextTextPasswordAgain);
        register = findViewById(R.id.register2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().toString().equals("") && !password.getText().toString().equals("") && !passwordAgain.getText().toString().equals("")){
                    if (password.getText().toString().equals(passwordAgain.getText().toString()) && !password.getText().toString().equals("")){
                        data.UploadUser(new User(username.getText().toString(),password.getText().toString()));
                        Toast.makeText(getApplicationContext(),"registration success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this,Login.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"The two entered passwords do not match", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Text is Empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
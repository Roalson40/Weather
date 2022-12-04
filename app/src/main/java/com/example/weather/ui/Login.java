package com.example.weather.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Data.Data;
import com.example.weather.Data.FireBaseData;
import com.example.weather.MainActivity;
import com.example.weather.Model.User;
import com.example.weather.R;
import com.example.weather.ui.home.WeatherFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class Login extends AppCompatActivity {

    TextView username;
    TextView password;
    Button logIn;
    Button register;
    FireBaseData fireBaseData = new Data();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.login);
        register = findViewById(R.id.register1);




        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                fireBaseData.getDatabase().child("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        try {
                            for (int i = 0; i < Objects.requireNonNull(task.getResult().getValue()).toString().length(); i++) {
                                if (task.getResult().getValue().toString().contains(username.getText().toString())){
                                    if (Objects.requireNonNull(task.getResult().child(username.getText().toString()).getValue(User.class)).getPassword().contains(password.getText())){
                                        Intent intent = new Intent(Login.this, WeatherFragment.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Wrong Username or Password", Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Wrong Username or Password", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        }
                        catch (Exception e){
                            e.getMessage();
                        }

                    }

                });

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }
}
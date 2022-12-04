package com.example.weather.Data;

import com.example.weather.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Data implements FireBaseData{
    FirebaseDatabase database;
    DatabaseReference mDatabase;


    public Data(){
        database = FirebaseDatabase.getInstance("https://weather-72c51-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = database.getReference();
    }

    @Override
    public void UploadUser(User user) {
        if (!user.getUsername().equals("") && !user.getPassword().equals("")){
            mDatabase.child("Users").child(user.getUsername()).setValue(user);
        }
    }

    @Override
    public ArrayList<User> getUsers() {
        return null;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }
}

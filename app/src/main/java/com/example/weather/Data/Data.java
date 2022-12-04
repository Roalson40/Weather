package com.example.weather.Data;

import com.example.weather.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Data implements FireBaseData{
    FirebaseDatabase database;
    DatabaseReference Database;


    public Data(){
        database = FirebaseDatabase.getInstance("https://weather-72c51-default-rtdb.europe-west1.firebasedatabase.app/");
        Database = database.getReference();
    }

    @Override
    public void UploadUser(User user) {
        if (!user.getUsername().equals("") && !user.getPassword().equals("")){
            Database.child("Users").child(user.getUsername()).setValue(user);
        }
    }

    @Override

    public DatabaseReference getDatabase() {
        return Database;
    }
}

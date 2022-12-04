package com.example.weather.Data;

import com.example.weather.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public interface FireBaseData {

    DatabaseReference getmDatabase();
    FirebaseDatabase getDatabase();
    void UploadUser(User user);
    ArrayList<User> getUsers();
}

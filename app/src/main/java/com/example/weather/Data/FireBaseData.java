package com.example.weather.Data;

import com.example.weather.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public interface FireBaseData {

    DatabaseReference getDatabase();
    void UploadUser(User user);

}

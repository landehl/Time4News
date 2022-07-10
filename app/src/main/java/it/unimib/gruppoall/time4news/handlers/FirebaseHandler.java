package it.unimib.gruppoall.time4news.handlers;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import it.unimib.gruppoall.time4news.database.FbDatabase;

public class FirebaseHandler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FbDatabase.FbDatabase();
    }
}

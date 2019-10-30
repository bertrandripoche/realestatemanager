package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.database.dao.FlatDao;
import com.openclassrooms.realestatemanager.database.dao.AgentDao;

@Database(entities = {Flat.class, Agent.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {
    private static volatile RealEstateManagerDatabase INSTANCE;

    // --- DAO ---
    public abstract FlatDao mFlatDao();
    public abstract AgentDao mAgentDao();

    // --- INSTANCE ---
    public static RealEstateManagerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", "0");
                contentValues.put("firstname", "Marc");
                contentValues.put("lastname", "Antoine");
                contentValues.put("mail", "mon.mail@gmail.com");
                contentValues.put("id", "1");
                contentValues.put("firstname", "Agnès");
                contentValues.put("lastname", "Isard");
                contentValues.put("mail", "mon.mail@gmail.com");
                contentValues.put("id", "2");
                contentValues.put("firstname", "Naïma");
                contentValues.put("lastname", "Lassim");
                contentValues.put("mail", "mon.mail@gmail.com");
                contentValues.put("id", "3");
                contentValues.put("firstname", "Pierre");
                contentValues.put("lastname", "Martin");
                contentValues.put("mail", "mon.mail@gmail.com");
                contentValues.put("id", "4");
                contentValues.put("firstname", "Roger");
                contentValues.put("lastname", "Dumont");
                contentValues.put("mail", "mon.mail@gmail.com");
                contentValues.put("id", "5");
                contentValues.put("firstname", "Abdel");
                contentValues.put("lastname", "Malat");
                contentValues.put("mail", "mon.mail@gmail.com");

                db.insert("Agent", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }
}

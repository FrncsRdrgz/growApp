package com.example.growapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {SeedGrower.class}, version = 1, exportSchema = false)
public abstract class SeedGrowerDatabase extends RoomDatabase {
    public static final String DB_NAME ="seedgrower";
    private static SeedGrowerDatabase instance;

    public static synchronized SeedGrowerDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SeedGrowerDatabase.class,DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract SeedGrowerDao seedGrowerDao();

 /*   private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private SeedGrowerDao seedGrowerDao;

        private PopulateDbAsyncTask(SeedGrowerDatabase db) {
            seedGrowerDao = db.seedGrowerDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }*/
}

package govph.rsis.growapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SeedGrower.class,Seeds.class}, version = 2, exportSchema = false)
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
    public abstract SeedsDao seedsDao();

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

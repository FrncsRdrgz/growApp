package govph.rsis.growapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtDao;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserDao;

@Database(entities = {SeedGrower.class,Seeds.class, User.class, SeedBought.class}, version = 8, exportSchema = true)
public abstract class SeedGrowerDatabase extends RoomDatabase {
    public static final String DB_NAME ="seedgrower";
    private static SeedGrowerDatabase instance;

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `User` (`userId` INTEGER PRIMARY KEY NOT NULL, "
                    + "`serialNum` TEXT,"
                    +"`fullname` TEXT)");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER  TABLE SeedGrower ADD COLUMN previousCrop TEXT");

        }
    };
    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER  TABLE SeedGrower ADD COLUMN previousVariety TEXT");

        }
    };
    static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER  TABLE User ADD COLUMN accredArea TEXT");
        }
    };
    static final Migration MIGRATION_6_7 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER  TABLE User ADD COLUMN isLoggedIn TEXT");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7,8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `SeedBought` (`id` INTEGER PRIMARY KEY NOT NULL, "
                    + "`serialNum` TEXT,"
                    +"`palletCode` TEXT,"
                    +"`variety` TEXT,"
                    + "`seedClass` TEXT,"
                    + "`riceProgram` TEXT,"
                    + "`quantity` INTEGER NOT NULL,"
                    + "`usedQuantity` INTEGER NOT NULL,"
                    + "`area` DOUBLE NOT NULL,"
                    + "`tableName` TEXT)");
        }
    };
    public static synchronized SeedGrowerDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SeedGrowerDatabase.class,DB_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6,MIGRATION_6_7,MIGRATION_7_8)
                    .build();
        }
        return instance;
    }

    public abstract SeedGrowerDao seedGrowerDao();
    public abstract SeedsDao seedsDao();
    public abstract UserDao userDao();
    public abstract SeedBoughtDao seedBoughtDao();

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

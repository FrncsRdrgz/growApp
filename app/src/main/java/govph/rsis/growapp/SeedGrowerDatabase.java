package govph.rsis.growapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import govph.rsis.growapp.Municipality.Municipality;
import govph.rsis.growapp.Municipality.MunicipalityDao;
import govph.rsis.growapp.Province.Province;
import govph.rsis.growapp.Province.ProvinceDao;
import govph.rsis.growapp.Region.Region;
import govph.rsis.growapp.Region.RegionDao;
import govph.rsis.growapp.SeedBought.SeedBought;
import govph.rsis.growapp.SeedBought.SeedBoughtDao;
import govph.rsis.growapp.User.User;
import govph.rsis.growapp.User.UserDao;

@Database(entities = {SeedGrower.class,Seeds.class, User.class, SeedBought.class, Province.class, Region.class, Municipality.class}, version = 14, exportSchema = true)
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

    static final Migration MIGRATION_8_9 = new Migration(8,9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER  TABLE SeedGrower ADD COLUMN bought_id TEXT");
        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9,10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE SeedBought ADD COLUMN station_name TEXT");
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10,11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE SeedGrower ADD COLUMN transplanting_method TEXT");
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11,12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `regions` (`id` INTEGER PRIMARY KEY NOT NULL," +
                    "`region_id` INTEGER NOT NULL," +
                    "`region` TEXT)");
        }
    };

    static final Migration MIGRATION_12_13 = new Migration(12,13) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `provinces` (`id` INTEGER PRIMARY KEY NOT NULL," +
                    "`province_id` INTEGER NOT NULL," +
                    "`region_id` INTEGER NOT NULL," +
                    "`province` TEXT)");
        }
    };

    static final Migration MIGRATION_13_14 = new Migration(13,14) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `municipalities` (`id` INTEGER PRIMARY KEY NOT NULL," +
                    "`municipality_id` INTEGER NOT NULL," +
                    "`province_id` INTEGER NOT NULL," +
                    "`municipality` TEXT)");
        }
    };
    public static synchronized SeedGrowerDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SeedGrowerDatabase.class,DB_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6,MIGRATION_6_7,MIGRATION_7_8,MIGRATION_8_9,MIGRATION_9_10,MIGRATION_10_11,
                            MIGRATION_11_12,MIGRATION_12_13,MIGRATION_13_14)
                    .build();
        }
        return instance;
    }

    public abstract SeedGrowerDao seedGrowerDao();
    public abstract SeedsDao seedsDao();
    public abstract UserDao userDao();
    public abstract SeedBoughtDao seedBoughtDao();
    public abstract MunicipalityDao municipalityDao();
    public abstract ProvinceDao provinceDao();
    public abstract RegionDao regionDao();

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

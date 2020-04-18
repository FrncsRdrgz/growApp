package govph.rsis.growapp;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SeedGrowerDatabase_Impl extends SeedGrowerDatabase {
  private volatile SeedGrowerDao _seedGrowerDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SeedGrower` (`sgId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `macaddress` TEXT, `accredno` TEXT, `latitude` TEXT, `longitude` TEXT, `variety` TEXT, `seedsource` TEXT, `otherseedsource` TEXT, `seedclass` TEXT, `dateplanted` TEXT, `areaplanted` TEXT, `quantity` TEXT, `seedbedarea` TEXT, `seedlingage` TEXT, `seedlot` TEXT, `controlno` TEXT, `barangay` TEXT, `datecollected` TEXT, `isSent` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'aadc6e4ae16b20ddf32baf2e5225394e')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `SeedGrower`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSeedGrower = new HashMap<String, TableInfo.Column>(19);
        _columnsSeedGrower.put("sgId", new TableInfo.Column("sgId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("macaddress", new TableInfo.Column("macaddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("accredno", new TableInfo.Column("accredno", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("latitude", new TableInfo.Column("latitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("longitude", new TableInfo.Column("longitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("variety", new TableInfo.Column("variety", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("seedsource", new TableInfo.Column("seedsource", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("otherseedsource", new TableInfo.Column("otherseedsource", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("seedclass", new TableInfo.Column("seedclass", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("dateplanted", new TableInfo.Column("dateplanted", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("areaplanted", new TableInfo.Column("areaplanted", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("quantity", new TableInfo.Column("quantity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("seedbedarea", new TableInfo.Column("seedbedarea", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("seedlingage", new TableInfo.Column("seedlingage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("seedlot", new TableInfo.Column("seedlot", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("controlno", new TableInfo.Column("controlno", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("barangay", new TableInfo.Column("barangay", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("datecollected", new TableInfo.Column("datecollected", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("isSent", new TableInfo.Column("isSent", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSeedGrower = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSeedGrower = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSeedGrower = new TableInfo("SeedGrower", _columnsSeedGrower, _foreignKeysSeedGrower, _indicesSeedGrower);
        final TableInfo _existingSeedGrower = TableInfo.read(_db, "SeedGrower");
        if (! _infoSeedGrower.equals(_existingSeedGrower)) {
          return new RoomOpenHelper.ValidationResult(false, "SeedGrower(govph.rsis.growapp.SeedGrower).\n"
                  + " Expected:\n" + _infoSeedGrower + "\n"
                  + " Found:\n" + _existingSeedGrower);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "aadc6e4ae16b20ddf32baf2e5225394e", "0c0153e0ecd9c779d3d7b765f057a6f5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "SeedGrower");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `SeedGrower`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public SeedGrowerDao seedGrowerDao() {
    if (_seedGrowerDao != null) {
      return _seedGrowerDao;
    } else {
      synchronized(this) {
        if(_seedGrowerDao == null) {
          _seedGrowerDao = new SeedGrowerDao_Impl(this);
        }
        return _seedGrowerDao;
      }
    }
  }
}

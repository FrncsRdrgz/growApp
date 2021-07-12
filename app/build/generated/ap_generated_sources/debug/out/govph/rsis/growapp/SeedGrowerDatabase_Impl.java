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
import govph.rsis.growapp.Municipality.MunicipalityDao;
import govph.rsis.growapp.Municipality.MunicipalityDao_Impl;
import govph.rsis.growapp.Province.ProvinceDao;
import govph.rsis.growapp.Province.ProvinceDao_Impl;
import govph.rsis.growapp.Region.RegionDao;
import govph.rsis.growapp.Region.RegionDao_Impl;
import govph.rsis.growapp.SeedBought.SeedBoughtDao;
import govph.rsis.growapp.SeedBought.SeedBoughtDao_Impl;
import govph.rsis.growapp.User.UserDao;
import govph.rsis.growapp.User.UserDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SeedGrowerDatabase_Impl extends SeedGrowerDatabase {
  private volatile SeedGrowerDao _seedGrowerDao;

  private volatile SeedsDao _seedsDao;

  private volatile UserDao _userDao;

  private volatile SeedBoughtDao _seedBoughtDao;

  private volatile MunicipalityDao _municipalityDao;

  private volatile ProvinceDao _provinceDao;

  private volatile RegionDao _regionDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(17) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SeedGrower` (`sgId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `macaddress` TEXT, `accredno` TEXT, `latitude` TEXT, `longitude` TEXT, `variety` TEXT, `seedsource` TEXT, `otherseedsource` TEXT, `seedclass` TEXT, `dateplanted` TEXT, `areaplanted` TEXT, `quantity` TEXT, `seedbedarea` TEXT, `seedlingage` TEXT, `seedlot` TEXT, `controlno` TEXT, `barangay` TEXT, `datecollected` TEXT, `isSent` INTEGER, `riceProgram` TEXT, `coop` TEXT, `previousCrop` TEXT, `previousVariety` TEXT, `bought_id` TEXT, `transplanting_method` TEXT, `region` TEXT, `province` TEXT, `municipality` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Seeds` (`seed_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `variety` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `User` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serialNum` TEXT, `fullname` TEXT, `accredArea` TEXT, `isLoggedIn` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SeedBought` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serialNum` TEXT, `palletCode` TEXT, `variety` TEXT, `seedClass` TEXT, `riceProgram` TEXT, `quantity` INTEGER NOT NULL, `area` REAL NOT NULL, `usedQuantity` INTEGER NOT NULL, `tableName` TEXT, `station_name` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Province` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `province_id` INTEGER NOT NULL, `region_id` INTEGER NOT NULL, `province` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Region` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `region_id` INTEGER NOT NULL, `region` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Municipality` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `municipality_id` INTEGER NOT NULL, `province_id` INTEGER NOT NULL, `municipality` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f5f52b0b511caac18f7b92a4a425b4d2')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `SeedGrower`");
        _db.execSQL("DROP TABLE IF EXISTS `Seeds`");
        _db.execSQL("DROP TABLE IF EXISTS `User`");
        _db.execSQL("DROP TABLE IF EXISTS `SeedBought`");
        _db.execSQL("DROP TABLE IF EXISTS `Province`");
        _db.execSQL("DROP TABLE IF EXISTS `Region`");
        _db.execSQL("DROP TABLE IF EXISTS `Municipality`");
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
        final HashMap<String, TableInfo.Column> _columnsSeedGrower = new HashMap<String, TableInfo.Column>(28);
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
        _columnsSeedGrower.put("riceProgram", new TableInfo.Column("riceProgram", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("coop", new TableInfo.Column("coop", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("previousCrop", new TableInfo.Column("previousCrop", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("previousVariety", new TableInfo.Column("previousVariety", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("bought_id", new TableInfo.Column("bought_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("transplanting_method", new TableInfo.Column("transplanting_method", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("region", new TableInfo.Column("region", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("province", new TableInfo.Column("province", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedGrower.put("municipality", new TableInfo.Column("municipality", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSeedGrower = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSeedGrower = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSeedGrower = new TableInfo("SeedGrower", _columnsSeedGrower, _foreignKeysSeedGrower, _indicesSeedGrower);
        final TableInfo _existingSeedGrower = TableInfo.read(_db, "SeedGrower");
        if (! _infoSeedGrower.equals(_existingSeedGrower)) {
          return new RoomOpenHelper.ValidationResult(false, "SeedGrower(govph.rsis.growapp.SeedGrower).\n"
                  + " Expected:\n" + _infoSeedGrower + "\n"
                  + " Found:\n" + _existingSeedGrower);
        }
        final HashMap<String, TableInfo.Column> _columnsSeeds = new HashMap<String, TableInfo.Column>(2);
        _columnsSeeds.put("seed_id", new TableInfo.Column("seed_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeeds.put("variety", new TableInfo.Column("variety", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSeeds = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSeeds = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSeeds = new TableInfo("Seeds", _columnsSeeds, _foreignKeysSeeds, _indicesSeeds);
        final TableInfo _existingSeeds = TableInfo.read(_db, "Seeds");
        if (! _infoSeeds.equals(_existingSeeds)) {
          return new RoomOpenHelper.ValidationResult(false, "Seeds(govph.rsis.growapp.Seeds).\n"
                  + " Expected:\n" + _infoSeeds + "\n"
                  + " Found:\n" + _existingSeeds);
        }
        final HashMap<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(5);
        _columnsUser.put("userId", new TableInfo.Column("userId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("serialNum", new TableInfo.Column("serialNum", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("fullname", new TableInfo.Column("fullname", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("accredArea", new TableInfo.Column("accredArea", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("isLoggedIn", new TableInfo.Column("isLoggedIn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("User", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(_db, "User");
        if (! _infoUser.equals(_existingUser)) {
          return new RoomOpenHelper.ValidationResult(false, "User(govph.rsis.growapp.User.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
        final HashMap<String, TableInfo.Column> _columnsSeedBought = new HashMap<String, TableInfo.Column>(11);
        _columnsSeedBought.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("serialNum", new TableInfo.Column("serialNum", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("palletCode", new TableInfo.Column("palletCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("variety", new TableInfo.Column("variety", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("seedClass", new TableInfo.Column("seedClass", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("riceProgram", new TableInfo.Column("riceProgram", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("area", new TableInfo.Column("area", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("usedQuantity", new TableInfo.Column("usedQuantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("tableName", new TableInfo.Column("tableName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSeedBought.put("station_name", new TableInfo.Column("station_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSeedBought = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSeedBought = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSeedBought = new TableInfo("SeedBought", _columnsSeedBought, _foreignKeysSeedBought, _indicesSeedBought);
        final TableInfo _existingSeedBought = TableInfo.read(_db, "SeedBought");
        if (! _infoSeedBought.equals(_existingSeedBought)) {
          return new RoomOpenHelper.ValidationResult(false, "SeedBought(govph.rsis.growapp.SeedBought.SeedBought).\n"
                  + " Expected:\n" + _infoSeedBought + "\n"
                  + " Found:\n" + _existingSeedBought);
        }
        final HashMap<String, TableInfo.Column> _columnsProvince = new HashMap<String, TableInfo.Column>(4);
        _columnsProvince.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProvince.put("province_id", new TableInfo.Column("province_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProvince.put("region_id", new TableInfo.Column("region_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProvince.put("province", new TableInfo.Column("province", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProvince = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProvince = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProvince = new TableInfo("Province", _columnsProvince, _foreignKeysProvince, _indicesProvince);
        final TableInfo _existingProvince = TableInfo.read(_db, "Province");
        if (! _infoProvince.equals(_existingProvince)) {
          return new RoomOpenHelper.ValidationResult(false, "Province(govph.rsis.growapp.Province.Province).\n"
                  + " Expected:\n" + _infoProvince + "\n"
                  + " Found:\n" + _existingProvince);
        }
        final HashMap<String, TableInfo.Column> _columnsRegion = new HashMap<String, TableInfo.Column>(3);
        _columnsRegion.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegion.put("region_id", new TableInfo.Column("region_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegion.put("region", new TableInfo.Column("region", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRegion = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRegion = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRegion = new TableInfo("Region", _columnsRegion, _foreignKeysRegion, _indicesRegion);
        final TableInfo _existingRegion = TableInfo.read(_db, "Region");
        if (! _infoRegion.equals(_existingRegion)) {
          return new RoomOpenHelper.ValidationResult(false, "Region(govph.rsis.growapp.Region.Region).\n"
                  + " Expected:\n" + _infoRegion + "\n"
                  + " Found:\n" + _existingRegion);
        }
        final HashMap<String, TableInfo.Column> _columnsMunicipality = new HashMap<String, TableInfo.Column>(4);
        _columnsMunicipality.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMunicipality.put("municipality_id", new TableInfo.Column("municipality_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMunicipality.put("province_id", new TableInfo.Column("province_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMunicipality.put("municipality", new TableInfo.Column("municipality", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMunicipality = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMunicipality = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMunicipality = new TableInfo("Municipality", _columnsMunicipality, _foreignKeysMunicipality, _indicesMunicipality);
        final TableInfo _existingMunicipality = TableInfo.read(_db, "Municipality");
        if (! _infoMunicipality.equals(_existingMunicipality)) {
          return new RoomOpenHelper.ValidationResult(false, "Municipality(govph.rsis.growapp.Municipality.Municipality).\n"
                  + " Expected:\n" + _infoMunicipality + "\n"
                  + " Found:\n" + _existingMunicipality);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f5f52b0b511caac18f7b92a4a425b4d2", "d60f44f3f25fbe3bce9a68685ad3f590");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "SeedGrower","Seeds","User","SeedBought","Province","Region","Municipality");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `SeedGrower`");
      _db.execSQL("DELETE FROM `Seeds`");
      _db.execSQL("DELETE FROM `User`");
      _db.execSQL("DELETE FROM `SeedBought`");
      _db.execSQL("DELETE FROM `Province`");
      _db.execSQL("DELETE FROM `Region`");
      _db.execSQL("DELETE FROM `Municipality`");
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

  @Override
  public SeedsDao seedsDao() {
    if (_seedsDao != null) {
      return _seedsDao;
    } else {
      synchronized(this) {
        if(_seedsDao == null) {
          _seedsDao = new SeedsDao_Impl(this);
        }
        return _seedsDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public SeedBoughtDao seedBoughtDao() {
    if (_seedBoughtDao != null) {
      return _seedBoughtDao;
    } else {
      synchronized(this) {
        if(_seedBoughtDao == null) {
          _seedBoughtDao = new SeedBoughtDao_Impl(this);
        }
        return _seedBoughtDao;
      }
    }
  }

  @Override
  public MunicipalityDao municipalityDao() {
    if (_municipalityDao != null) {
      return _municipalityDao;
    } else {
      synchronized(this) {
        if(_municipalityDao == null) {
          _municipalityDao = new MunicipalityDao_Impl(this);
        }
        return _municipalityDao;
      }
    }
  }

  @Override
  public ProvinceDao provinceDao() {
    if (_provinceDao != null) {
      return _provinceDao;
    } else {
      synchronized(this) {
        if(_provinceDao == null) {
          _provinceDao = new ProvinceDao_Impl(this);
        }
        return _provinceDao;
      }
    }
  }

  @Override
  public RegionDao regionDao() {
    if (_regionDao != null) {
      return _regionDao;
    } else {
      synchronized(this) {
        if(_regionDao == null) {
          _regionDao = new RegionDao_Impl(this);
        }
        return _regionDao;
      }
    }
  }
}

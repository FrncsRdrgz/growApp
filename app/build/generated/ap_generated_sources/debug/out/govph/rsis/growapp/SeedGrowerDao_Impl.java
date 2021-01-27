package govph.rsis.growapp;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SeedGrowerDao_Impl implements SeedGrowerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SeedGrower> __insertionAdapterOfSeedGrower;

  private final EntityDeletionOrUpdateAdapter<SeedGrower> __deletionAdapterOfSeedGrower;

  private final EntityDeletionOrUpdateAdapter<SeedGrower> __updateAdapterOfSeedGrower;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SeedGrowerDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSeedGrower = new EntityInsertionAdapter<SeedGrower>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `SeedGrower` (`sgId`,`macaddress`,`accredno`,`latitude`,`longitude`,`variety`,`seedsource`,`otherseedsource`,`seedclass`,`dateplanted`,`areaplanted`,`quantity`,`seedbedarea`,`seedlingage`,`seedlot`,`controlno`,`barangay`,`datecollected`,`isSent`,`riceProgram`,`coop`,`designation`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SeedGrower value) {
        stmt.bindLong(1, value.sgId);
        if (value.macaddress == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.macaddress);
        }
        if (value.accredno == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.accredno);
        }
        if (value.latitude == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.latitude);
        }
        if (value.longitude == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.longitude);
        }
        if (value.variety == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.variety);
        }
        if (value.seedsource == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.seedsource);
        }
        if (value.otherseedsource == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.otherseedsource);
        }
        if (value.seedclass == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.seedclass);
        }
        if (value.dateplanted == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.dateplanted);
        }
        if (value.areaplanted == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.areaplanted);
        }
        if (value.quantity == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.quantity);
        }
        if (value.seedbedarea == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.seedbedarea);
        }
        if (value.seedlingage == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.seedlingage);
        }
        if (value.seedlot == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.seedlot);
        }
        if (value.controlno == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.controlno);
        }
        if (value.barangay == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.barangay);
        }
        if (value.datecollected == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.datecollected);
        }
        final Integer _tmp;
        _tmp = value.isSent == null ? null : (value.isSent ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindLong(19, _tmp);
        }
        if (value.riceProgram == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.riceProgram);
        }
        if (value.coop == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.coop);
        }
        if (value.designation == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.designation);
        }
      }
    };
    this.__deletionAdapterOfSeedGrower = new EntityDeletionOrUpdateAdapter<SeedGrower>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `SeedGrower` WHERE `sgId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SeedGrower value) {
        stmt.bindLong(1, value.sgId);
      }
    };
    this.__updateAdapterOfSeedGrower = new EntityDeletionOrUpdateAdapter<SeedGrower>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `SeedGrower` SET `sgId` = ?,`macaddress` = ?,`accredno` = ?,`latitude` = ?,`longitude` = ?,`variety` = ?,`seedsource` = ?,`otherseedsource` = ?,`seedclass` = ?,`dateplanted` = ?,`areaplanted` = ?,`quantity` = ?,`seedbedarea` = ?,`seedlingage` = ?,`seedlot` = ?,`controlno` = ?,`barangay` = ?,`datecollected` = ?,`isSent` = ?,`riceProgram` = ?,`coop` = ?,`designation` = ? WHERE `sgId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SeedGrower value) {
        stmt.bindLong(1, value.sgId);
        if (value.macaddress == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.macaddress);
        }
        if (value.accredno == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.accredno);
        }
        if (value.latitude == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.latitude);
        }
        if (value.longitude == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.longitude);
        }
        if (value.variety == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.variety);
        }
        if (value.seedsource == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.seedsource);
        }
        if (value.otherseedsource == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.otherseedsource);
        }
        if (value.seedclass == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.seedclass);
        }
        if (value.dateplanted == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.dateplanted);
        }
        if (value.areaplanted == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.areaplanted);
        }
        if (value.quantity == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.quantity);
        }
        if (value.seedbedarea == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.seedbedarea);
        }
        if (value.seedlingage == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.seedlingage);
        }
        if (value.seedlot == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.seedlot);
        }
        if (value.controlno == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.controlno);
        }
        if (value.barangay == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.barangay);
        }
        if (value.datecollected == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.datecollected);
        }
        final Integer _tmp;
        _tmp = value.isSent == null ? null : (value.isSent ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindLong(19, _tmp);
        }
        if (value.riceProgram == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.riceProgram);
        }
        if (value.coop == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.coop);
        }
        if (value.designation == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindString(22, value.designation);
        }
        stmt.bindLong(23, value.sgId);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM seedgrower";
        return _query;
      }
    };
  }

  @Override
  public void insertSeedGrower(final SeedGrower seedGrower) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSeedGrower.insert(seedGrower);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSeedGrower(final SeedGrower seedGrower) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSeedGrower.handle(seedGrower);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateSeedGrower(final SeedGrower seedGrower) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSeedGrower.handle(seedGrower);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<SeedGrower>> getAll() {
    final String _sql = "SELECT * from seedgrower WHERE isSent=0 ORDER BY sgId DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"seedgrower"}, false, new Callable<List<SeedGrower>>() {
      @Override
      public List<SeedGrower> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgId = CursorUtil.getColumnIndexOrThrow(_cursor, "sgId");
          final int _cursorIndexOfMacaddress = CursorUtil.getColumnIndexOrThrow(_cursor, "macaddress");
          final int _cursorIndexOfAccredno = CursorUtil.getColumnIndexOrThrow(_cursor, "accredno");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfVariety = CursorUtil.getColumnIndexOrThrow(_cursor, "variety");
          final int _cursorIndexOfSeedsource = CursorUtil.getColumnIndexOrThrow(_cursor, "seedsource");
          final int _cursorIndexOfOtherseedsource = CursorUtil.getColumnIndexOrThrow(_cursor, "otherseedsource");
          final int _cursorIndexOfSeedclass = CursorUtil.getColumnIndexOrThrow(_cursor, "seedclass");
          final int _cursorIndexOfDateplanted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateplanted");
          final int _cursorIndexOfAreaplanted = CursorUtil.getColumnIndexOrThrow(_cursor, "areaplanted");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfSeedbedarea = CursorUtil.getColumnIndexOrThrow(_cursor, "seedbedarea");
          final int _cursorIndexOfSeedlingage = CursorUtil.getColumnIndexOrThrow(_cursor, "seedlingage");
          final int _cursorIndexOfSeedlot = CursorUtil.getColumnIndexOrThrow(_cursor, "seedlot");
          final int _cursorIndexOfControlno = CursorUtil.getColumnIndexOrThrow(_cursor, "controlno");
          final int _cursorIndexOfBarangay = CursorUtil.getColumnIndexOrThrow(_cursor, "barangay");
          final int _cursorIndexOfDatecollected = CursorUtil.getColumnIndexOrThrow(_cursor, "datecollected");
          final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "isSent");
          final int _cursorIndexOfRiceProgram = CursorUtil.getColumnIndexOrThrow(_cursor, "riceProgram");
          final int _cursorIndexOfCoop = CursorUtil.getColumnIndexOrThrow(_cursor, "coop");
          final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
          final List<SeedGrower> _result = new ArrayList<SeedGrower>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SeedGrower _item;
            final String _tmpMacaddress;
            _tmpMacaddress = _cursor.getString(_cursorIndexOfMacaddress);
            final String _tmpAccredno;
            _tmpAccredno = _cursor.getString(_cursorIndexOfAccredno);
            final String _tmpLatitude;
            _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
            final String _tmpLongitude;
            _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
            final String _tmpVariety;
            _tmpVariety = _cursor.getString(_cursorIndexOfVariety);
            final String _tmpSeedsource;
            _tmpSeedsource = _cursor.getString(_cursorIndexOfSeedsource);
            final String _tmpOtherseedsource;
            _tmpOtherseedsource = _cursor.getString(_cursorIndexOfOtherseedsource);
            final String _tmpSeedclass;
            _tmpSeedclass = _cursor.getString(_cursorIndexOfSeedclass);
            final String _tmpDateplanted;
            _tmpDateplanted = _cursor.getString(_cursorIndexOfDateplanted);
            final String _tmpAreaplanted;
            _tmpAreaplanted = _cursor.getString(_cursorIndexOfAreaplanted);
            final String _tmpQuantity;
            _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
            final String _tmpSeedbedarea;
            _tmpSeedbedarea = _cursor.getString(_cursorIndexOfSeedbedarea);
            final String _tmpSeedlingage;
            _tmpSeedlingage = _cursor.getString(_cursorIndexOfSeedlingage);
            final String _tmpSeedlot;
            _tmpSeedlot = _cursor.getString(_cursorIndexOfSeedlot);
            final String _tmpControlno;
            _tmpControlno = _cursor.getString(_cursorIndexOfControlno);
            final String _tmpBarangay;
            _tmpBarangay = _cursor.getString(_cursorIndexOfBarangay);
            final String _tmpDatecollected;
            _tmpDatecollected = _cursor.getString(_cursorIndexOfDatecollected);
            final Boolean _tmpIsSent;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfIsSent)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfIsSent);
            }
            _tmpIsSent = _tmp == null ? null : _tmp != 0;
            final String _tmpRiceProgram;
            _tmpRiceProgram = _cursor.getString(_cursorIndexOfRiceProgram);
            final String _tmpCoop;
            _tmpCoop = _cursor.getString(_cursorIndexOfCoop);
            _item = new SeedGrower(_tmpMacaddress,_tmpAccredno,_tmpLatitude,_tmpLongitude,_tmpVariety,_tmpSeedsource,_tmpOtherseedsource,_tmpSeedclass,_tmpDateplanted,_tmpAreaplanted,_tmpQuantity,_tmpSeedbedarea,_tmpSeedlingage,_tmpSeedlot,_tmpControlno,_tmpBarangay,_tmpDatecollected,_tmpIsSent,_tmpRiceProgram,_tmpCoop);
            _item.sgId = _cursor.getInt(_cursorIndexOfSgId);
            _item.designation = _cursor.getString(_cursorIndexOfDesignation);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<SeedGrower>> getSentForms() {
    final String _sql = "SELECT * from seedgrower WHERE isSent=1 ORDER BY sgId DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"seedgrower"}, false, new Callable<List<SeedGrower>>() {
      @Override
      public List<SeedGrower> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSgId = CursorUtil.getColumnIndexOrThrow(_cursor, "sgId");
          final int _cursorIndexOfMacaddress = CursorUtil.getColumnIndexOrThrow(_cursor, "macaddress");
          final int _cursorIndexOfAccredno = CursorUtil.getColumnIndexOrThrow(_cursor, "accredno");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfVariety = CursorUtil.getColumnIndexOrThrow(_cursor, "variety");
          final int _cursorIndexOfSeedsource = CursorUtil.getColumnIndexOrThrow(_cursor, "seedsource");
          final int _cursorIndexOfOtherseedsource = CursorUtil.getColumnIndexOrThrow(_cursor, "otherseedsource");
          final int _cursorIndexOfSeedclass = CursorUtil.getColumnIndexOrThrow(_cursor, "seedclass");
          final int _cursorIndexOfDateplanted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateplanted");
          final int _cursorIndexOfAreaplanted = CursorUtil.getColumnIndexOrThrow(_cursor, "areaplanted");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfSeedbedarea = CursorUtil.getColumnIndexOrThrow(_cursor, "seedbedarea");
          final int _cursorIndexOfSeedlingage = CursorUtil.getColumnIndexOrThrow(_cursor, "seedlingage");
          final int _cursorIndexOfSeedlot = CursorUtil.getColumnIndexOrThrow(_cursor, "seedlot");
          final int _cursorIndexOfControlno = CursorUtil.getColumnIndexOrThrow(_cursor, "controlno");
          final int _cursorIndexOfBarangay = CursorUtil.getColumnIndexOrThrow(_cursor, "barangay");
          final int _cursorIndexOfDatecollected = CursorUtil.getColumnIndexOrThrow(_cursor, "datecollected");
          final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "isSent");
          final int _cursorIndexOfRiceProgram = CursorUtil.getColumnIndexOrThrow(_cursor, "riceProgram");
          final int _cursorIndexOfCoop = CursorUtil.getColumnIndexOrThrow(_cursor, "coop");
          final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
          final List<SeedGrower> _result = new ArrayList<SeedGrower>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SeedGrower _item;
            final String _tmpMacaddress;
            _tmpMacaddress = _cursor.getString(_cursorIndexOfMacaddress);
            final String _tmpAccredno;
            _tmpAccredno = _cursor.getString(_cursorIndexOfAccredno);
            final String _tmpLatitude;
            _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
            final String _tmpLongitude;
            _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
            final String _tmpVariety;
            _tmpVariety = _cursor.getString(_cursorIndexOfVariety);
            final String _tmpSeedsource;
            _tmpSeedsource = _cursor.getString(_cursorIndexOfSeedsource);
            final String _tmpOtherseedsource;
            _tmpOtherseedsource = _cursor.getString(_cursorIndexOfOtherseedsource);
            final String _tmpSeedclass;
            _tmpSeedclass = _cursor.getString(_cursorIndexOfSeedclass);
            final String _tmpDateplanted;
            _tmpDateplanted = _cursor.getString(_cursorIndexOfDateplanted);
            final String _tmpAreaplanted;
            _tmpAreaplanted = _cursor.getString(_cursorIndexOfAreaplanted);
            final String _tmpQuantity;
            _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
            final String _tmpSeedbedarea;
            _tmpSeedbedarea = _cursor.getString(_cursorIndexOfSeedbedarea);
            final String _tmpSeedlingage;
            _tmpSeedlingage = _cursor.getString(_cursorIndexOfSeedlingage);
            final String _tmpSeedlot;
            _tmpSeedlot = _cursor.getString(_cursorIndexOfSeedlot);
            final String _tmpControlno;
            _tmpControlno = _cursor.getString(_cursorIndexOfControlno);
            final String _tmpBarangay;
            _tmpBarangay = _cursor.getString(_cursorIndexOfBarangay);
            final String _tmpDatecollected;
            _tmpDatecollected = _cursor.getString(_cursorIndexOfDatecollected);
            final Boolean _tmpIsSent;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfIsSent)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfIsSent);
            }
            _tmpIsSent = _tmp == null ? null : _tmp != 0;
            final String _tmpRiceProgram;
            _tmpRiceProgram = _cursor.getString(_cursorIndexOfRiceProgram);
            final String _tmpCoop;
            _tmpCoop = _cursor.getString(_cursorIndexOfCoop);
            _item = new SeedGrower(_tmpMacaddress,_tmpAccredno,_tmpLatitude,_tmpLongitude,_tmpVariety,_tmpSeedsource,_tmpOtherseedsource,_tmpSeedclass,_tmpDateplanted,_tmpAreaplanted,_tmpQuantity,_tmpSeedbedarea,_tmpSeedlingage,_tmpSeedlot,_tmpControlno,_tmpBarangay,_tmpDatecollected,_tmpIsSent,_tmpRiceProgram,_tmpCoop);
            _item.sgId = _cursor.getInt(_cursorIndexOfSgId);
            _item.designation = _cursor.getString(_cursorIndexOfDesignation);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public SeedGrower findFormById(final String sgId) {
    final String _sql = "SELECT * from seedgrower WHERE sgId =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (sgId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sgId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSgId = CursorUtil.getColumnIndexOrThrow(_cursor, "sgId");
      final int _cursorIndexOfMacaddress = CursorUtil.getColumnIndexOrThrow(_cursor, "macaddress");
      final int _cursorIndexOfAccredno = CursorUtil.getColumnIndexOrThrow(_cursor, "accredno");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfVariety = CursorUtil.getColumnIndexOrThrow(_cursor, "variety");
      final int _cursorIndexOfSeedsource = CursorUtil.getColumnIndexOrThrow(_cursor, "seedsource");
      final int _cursorIndexOfOtherseedsource = CursorUtil.getColumnIndexOrThrow(_cursor, "otherseedsource");
      final int _cursorIndexOfSeedclass = CursorUtil.getColumnIndexOrThrow(_cursor, "seedclass");
      final int _cursorIndexOfDateplanted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateplanted");
      final int _cursorIndexOfAreaplanted = CursorUtil.getColumnIndexOrThrow(_cursor, "areaplanted");
      final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
      final int _cursorIndexOfSeedbedarea = CursorUtil.getColumnIndexOrThrow(_cursor, "seedbedarea");
      final int _cursorIndexOfSeedlingage = CursorUtil.getColumnIndexOrThrow(_cursor, "seedlingage");
      final int _cursorIndexOfSeedlot = CursorUtil.getColumnIndexOrThrow(_cursor, "seedlot");
      final int _cursorIndexOfControlno = CursorUtil.getColumnIndexOrThrow(_cursor, "controlno");
      final int _cursorIndexOfBarangay = CursorUtil.getColumnIndexOrThrow(_cursor, "barangay");
      final int _cursorIndexOfDatecollected = CursorUtil.getColumnIndexOrThrow(_cursor, "datecollected");
      final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "isSent");
      final int _cursorIndexOfRiceProgram = CursorUtil.getColumnIndexOrThrow(_cursor, "riceProgram");
      final int _cursorIndexOfCoop = CursorUtil.getColumnIndexOrThrow(_cursor, "coop");
      final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
      final SeedGrower _result;
      if(_cursor.moveToFirst()) {
        final String _tmpMacaddress;
        _tmpMacaddress = _cursor.getString(_cursorIndexOfMacaddress);
        final String _tmpAccredno;
        _tmpAccredno = _cursor.getString(_cursorIndexOfAccredno);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpVariety;
        _tmpVariety = _cursor.getString(_cursorIndexOfVariety);
        final String _tmpSeedsource;
        _tmpSeedsource = _cursor.getString(_cursorIndexOfSeedsource);
        final String _tmpOtherseedsource;
        _tmpOtherseedsource = _cursor.getString(_cursorIndexOfOtherseedsource);
        final String _tmpSeedclass;
        _tmpSeedclass = _cursor.getString(_cursorIndexOfSeedclass);
        final String _tmpDateplanted;
        _tmpDateplanted = _cursor.getString(_cursorIndexOfDateplanted);
        final String _tmpAreaplanted;
        _tmpAreaplanted = _cursor.getString(_cursorIndexOfAreaplanted);
        final String _tmpQuantity;
        _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
        final String _tmpSeedbedarea;
        _tmpSeedbedarea = _cursor.getString(_cursorIndexOfSeedbedarea);
        final String _tmpSeedlingage;
        _tmpSeedlingage = _cursor.getString(_cursorIndexOfSeedlingage);
        final String _tmpSeedlot;
        _tmpSeedlot = _cursor.getString(_cursorIndexOfSeedlot);
        final String _tmpControlno;
        _tmpControlno = _cursor.getString(_cursorIndexOfControlno);
        final String _tmpBarangay;
        _tmpBarangay = _cursor.getString(_cursorIndexOfBarangay);
        final String _tmpDatecollected;
        _tmpDatecollected = _cursor.getString(_cursorIndexOfDatecollected);
        final Boolean _tmpIsSent;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfIsSent)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfIsSent);
        }
        _tmpIsSent = _tmp == null ? null : _tmp != 0;
        final String _tmpRiceProgram;
        _tmpRiceProgram = _cursor.getString(_cursorIndexOfRiceProgram);
        final String _tmpCoop;
        _tmpCoop = _cursor.getString(_cursorIndexOfCoop);
        _result = new SeedGrower(_tmpMacaddress,_tmpAccredno,_tmpLatitude,_tmpLongitude,_tmpVariety,_tmpSeedsource,_tmpOtherseedsource,_tmpSeedclass,_tmpDateplanted,_tmpAreaplanted,_tmpQuantity,_tmpSeedbedarea,_tmpSeedlingage,_tmpSeedlot,_tmpControlno,_tmpBarangay,_tmpDatecollected,_tmpIsSent,_tmpRiceProgram,_tmpCoop);
        _result.sgId = _cursor.getInt(_cursorIndexOfSgId);
        _result.designation = _cursor.getString(_cursorIndexOfDesignation);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}

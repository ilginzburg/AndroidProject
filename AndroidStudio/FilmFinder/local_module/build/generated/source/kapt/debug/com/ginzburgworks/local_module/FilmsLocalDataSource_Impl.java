package com.ginzburgworks.local_module;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.ginzburgworks.local_module.dao.FilmDao;
import com.ginzburgworks.local_module.dao.FilmDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FilmsLocalDataSource_Impl extends FilmsLocalDataSource {
  private volatile FilmDao _filmDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `cached_films` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `page` INTEGER NOT NULL, `category` TEXT NOT NULL, `title` TEXT NOT NULL, `poster_path` TEXT NOT NULL, `overview` TEXT NOT NULL, `vote_average` REAL NOT NULL, `isInFavorites` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_cached_films_page_category_title` ON `cached_films` (`page`, `category`, `title`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b512a3e410bb76a6e09ea802fcd625eb')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `cached_films`");
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
        final HashMap<String, TableInfo.Column> _columnsCachedFilms = new HashMap<String, TableInfo.Column>(8);
        _columnsCachedFilms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("page", new TableInfo.Column("page", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("poster_path", new TableInfo.Column("poster_path", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("overview", new TableInfo.Column("overview", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("vote_average", new TableInfo.Column("vote_average", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedFilms.put("isInFavorites", new TableInfo.Column("isInFavorites", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCachedFilms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCachedFilms = new HashSet<TableInfo.Index>(1);
        _indicesCachedFilms.add(new TableInfo.Index("index_cached_films_page_category_title", true, Arrays.asList("page","category","title"), Arrays.asList("ASC","ASC","ASC")));
        final TableInfo _infoCachedFilms = new TableInfo("cached_films", _columnsCachedFilms, _foreignKeysCachedFilms, _indicesCachedFilms);
        final TableInfo _existingCachedFilms = TableInfo.read(_db, "cached_films");
        if (! _infoCachedFilms.equals(_existingCachedFilms)) {
          return new RoomOpenHelper.ValidationResult(false, "cached_films(com.ginzburgworks.local_module.Film).\n"
                  + " Expected:\n" + _infoCachedFilms + "\n"
                  + " Found:\n" + _existingCachedFilms);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "b512a3e410bb76a6e09ea802fcd625eb", "22c176e3298b73ac7fddd1ef3f9b3311");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "cached_films");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `cached_films`");
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
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FilmDao.class, FilmDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public FilmDao filmDao() {
    if (_filmDao != null) {
      return _filmDao;
    } else {
      synchronized(this) {
        if(_filmDao == null) {
          _filmDao = new FilmDao_Impl(this);
        }
        return _filmDao;
      }
    }
  }
}

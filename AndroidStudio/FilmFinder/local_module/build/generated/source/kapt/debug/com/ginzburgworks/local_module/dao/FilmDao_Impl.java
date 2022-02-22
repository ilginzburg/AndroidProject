package com.ginzburgworks.local_module.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.rxjava3.RxRoom;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ginzburgworks.local_module.Film;
import io.reactivex.rxjava3.core.Observable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FilmDao_Impl implements FilmDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Film> __insertionAdapterOfFilm;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FilmDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFilm = new EntityInsertionAdapter<Film>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `cached_films` (`id`,`page`,`category`,`title`,`poster_path`,`overview`,`vote_average`,`isInFavorites`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Film value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getPage());
        if (value.getCategory() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getCategory());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTitle());
        }
        if (value.getPoster() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPoster());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDescription());
        }
        stmt.bindDouble(7, value.getRating());
        final int _tmp = value.isInFavorites() ? 1 : 0;
        stmt.bindLong(8, _tmp);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM cached_films";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<Film> list) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFilm.insert(list);
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
  public Observable<List<Film>> getCachedFilmsByCategory(final String requestedCategory) {
    final String _sql = "SELECT * FROM cached_films WHERE category=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (requestedCategory == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, requestedCategory);
    }
    return RxRoom.createObservable(__db, false, new String[]{"cached_films"}, new Callable<List<Film>>() {
      @Override
      public List<Film> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPage = CursorUtil.getColumnIndexOrThrow(_cursor, "page");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPoster = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_path");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "overview");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "vote_average");
          final int _cursorIndexOfIsInFavorites = CursorUtil.getColumnIndexOrThrow(_cursor, "isInFavorites");
          final List<Film> _result = new ArrayList<Film>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Film _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPage;
            _tmpPage = _cursor.getInt(_cursorIndexOfPage);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpPoster;
            if (_cursor.isNull(_cursorIndexOfPoster)) {
              _tmpPoster = null;
            } else {
              _tmpPoster = _cursor.getString(_cursorIndexOfPoster);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final boolean _tmpIsInFavorites;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInFavorites);
            _tmpIsInFavorites = _tmp != 0;
            _item = new Film(_tmpId,_tmpPage,_tmpCategory,_tmpTitle,_tmpPoster,_tmpDescription,_tmpRating,_tmpIsInFavorites);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

package com.ginzburgworks.local_module.dao;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\'J\u001c\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u00052\u0006\u0010\b\u001a\u00020\tH\'J\u0016\u0010\n\u001a\u00020\u00032\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\'\u00a8\u0006\f"}, d2 = {"Lcom/ginzburgworks/local_module/dao/FilmDao;", "", "deleteAll", "", "getCachedFilmsByCategory", "Lio/reactivex/rxjava3/core/Observable;", "", "Lcom/ginzburgworks/local_module/Film;", "requestedCategory", "", "insertAll", "list", "local_module_debug"})
public abstract interface FilmDao {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ginzburgworks.local_module.Film> list);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM cached_films WHERE category=:requestedCategory")
    public abstract io.reactivex.rxjava3.core.Observable<java.util.List<com.ginzburgworks.local_module.Film>> getCachedFilmsByCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String requestedCategory);
    
    @androidx.room.Query(value = "DELETE FROM cached_films")
    public abstract void deleteAll();
}
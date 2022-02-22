package com.ginzburgworks.local_module;

import java.lang.System;

@androidx.room.Database(entities = {com.ginzburgworks.local_module.Film.class}, version = 1, exportSchema = false)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcom/ginzburgworks/local_module/FilmsLocalDataSource;", "Landroidx/room/RoomDatabase;", "()V", "filmDao", "Lcom/ginzburgworks/local_module/dao/FilmDao;", "local_module_debug"})
public abstract class FilmsLocalDataSource extends androidx.room.RoomDatabase {
    
    public FilmsLocalDataSource() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ginzburgworks.local_module.dao.FilmDao filmDao();
}
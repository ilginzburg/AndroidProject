package com.ginzburgworks.local_module;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\fJ\u0014\u0010\r\u001a\u00020\u00062\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ginzburgworks/local_module/LocalRepository;", "", "filmDao", "Lcom/ginzburgworks/local_module/dao/FilmDao;", "(Lcom/ginzburgworks/local_module/dao/FilmDao;)V", "deleteAll", "", "getFilmsInCategory", "Lio/reactivex/rxjava3/core/Observable;", "", "Lcom/ginzburgworks/local_module/Film;", "category", "", "putPageOfFilms", "pageOfFilms", "local_module_debug"})
public final class LocalRepository {
    private final com.ginzburgworks.local_module.dao.FilmDao filmDao = null;
    
    public LocalRepository(@org.jetbrains.annotations.NotNull()
    com.ginzburgworks.local_module.dao.FilmDao filmDao) {
        super();
    }
    
    public final void putPageOfFilms(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ginzburgworks.local_module.Film> pageOfFilms) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.rxjava3.core.Observable<java.util.List<com.ginzburgworks.local_module.Film>> getFilmsInCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category) {
        return null;
    }
    
    public final void deleteAll() {
    }
}
package com.ginzburgworks.remote_module;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J6\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u00062\b\b\u0001\u0010\t\u001a\u00020\nH\'J@\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u00062\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\f\u001a\u00020\u00062\b\b\u0001\u0010\r\u001a\u00020\u000eH\'\u00a8\u0006\u000f"}, d2 = {"Lcom/ginzburgworks/remote_module/TmdbApi;", "", "getFilms", "Lio/reactivex/rxjava3/core/Single;", "Lcom/ginzburgworks/remote_module/entity/TmdbResultsDto;", "category", "", "apiKey", "language", "page", "", "getSearchResult", "searchQuery", "adult", "", "remote_module_debug"})
public abstract interface TmdbApi {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "3/movie/{category}")
    public abstract io.reactivex.rxjava3.core.Single<com.ginzburgworks.remote_module.entity.TmdbResultsDto> getFilms(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Path(value = "category")
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "api_key")
    java.lang.String apiKey, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "language")
    java.lang.String language, @retrofit2.http.Query(value = "page")
    int page);
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "3/search/movie")
    public abstract io.reactivex.rxjava3.core.Single<com.ginzburgworks.remote_module.entity.TmdbResultsDto> getSearchResult(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "api_key")
    java.lang.String apiKey, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "language")
    java.lang.String language, @retrofit2.http.Query(value = "page")
    int page, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "query")
    java.lang.String searchQuery, @retrofit2.http.Query(value = "include_adult")
    boolean adult);
}
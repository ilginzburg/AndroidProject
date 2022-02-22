package com.ginzburgworks.remote_module;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&\u00a8\u0006\u0004"}, d2 = {"Lcom/ginzburgworks/remote_module/RemoteProvider;", "", "provideRemote", "Lcom/ginzburgworks/remote_module/TmdbApi;", "remote_module_debug"})
public abstract interface RemoteProvider {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ginzburgworks.remote_module.TmdbApi provideRemote();
}
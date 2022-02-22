// Generated by Dagger (https://dagger.dev).
package com.ginzburgworks.remote_module;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RemoteModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final RemoteModule module;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public RemoteModule_ProvideRetrofitFactory(RemoteModule module,
      Provider<OkHttpClient> okHttpClientProvider) {
    this.module = module;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(module, okHttpClientProvider.get());
  }

  public static RemoteModule_ProvideRetrofitFactory create(RemoteModule module,
      Provider<OkHttpClient> okHttpClientProvider) {
    return new RemoteModule_ProvideRetrofitFactory(module, okHttpClientProvider);
  }

  public static Retrofit provideRetrofit(RemoteModule instance, OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(instance.provideRetrofit(okHttpClient));
  }
}

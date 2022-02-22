// Generated by Dagger (https://dagger.dev).
package com.ginzburgworks.remote_module;

import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerRemoteComponent implements RemoteComponent {
  private final DaggerRemoteComponent remoteComponent = this;

  private Provider<OkHttpClient> provideOkHttpClientProvider;

  private Provider<Retrofit> provideRetrofitProvider;

  private Provider<TmdbApi> provideTmdbApiProvider;

  private DaggerRemoteComponent(RemoteModule remoteModuleParam) {

    initialize(remoteModuleParam);

  }

  public static Builder builder() {
    return new Builder();
  }

  public static RemoteComponent create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final RemoteModule remoteModuleParam) {
    this.provideOkHttpClientProvider = DoubleCheck.provider(RemoteModule_ProvideOkHttpClientFactory.create(remoteModuleParam));
    this.provideRetrofitProvider = DoubleCheck.provider(RemoteModule_ProvideRetrofitFactory.create(remoteModuleParam, provideOkHttpClientProvider));
    this.provideTmdbApiProvider = DoubleCheck.provider(RemoteModule_ProvideTmdbApiFactory.create(remoteModuleParam, provideRetrofitProvider));
  }

  @Override
  public TmdbApi provideRemote() {
    return provideTmdbApiProvider.get();
  }

  public static final class Builder {
    private RemoteModule remoteModule;

    private Builder() {
    }

    public Builder remoteModule(RemoteModule remoteModule) {
      this.remoteModule = Preconditions.checkNotNull(remoteModule);
      return this;
    }

    public RemoteComponent build() {
      if (remoteModule == null) {
        this.remoteModule = new RemoteModule();
      }
      return new DaggerRemoteComponent(remoteModule);
    }
  }
}
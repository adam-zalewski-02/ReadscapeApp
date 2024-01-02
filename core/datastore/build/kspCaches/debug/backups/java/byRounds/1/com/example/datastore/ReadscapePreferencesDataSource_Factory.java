package com.example.datastore;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ReadscapePreferencesDataSource_Factory implements Factory<ReadscapePreferencesDataSource> {
  @Override
  public ReadscapePreferencesDataSource get() {
    return newInstance();
  }

  public static ReadscapePreferencesDataSource_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ReadscapePreferencesDataSource newInstance() {
    return new ReadscapePreferencesDataSource();
  }

  private static final class InstanceHolder {
    private static final ReadscapePreferencesDataSource_Factory INSTANCE = new ReadscapePreferencesDataSource_Factory();
  }
}

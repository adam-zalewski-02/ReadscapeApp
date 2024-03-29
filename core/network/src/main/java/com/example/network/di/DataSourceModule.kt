package com.example.network.di

import com.example.network.CmsNetworkDatasource
import com.example.network.GoogleNetworkDataSource
import com.example.network.ReadscapeNetworkDataSource
import com.example.network.retrofit.RetrofitCmsNetwork
import com.example.network.retrofit.RetrofitGoogleNetwork
import com.example.network.retrofit.RetrofitReadscapeNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun providesReadscapeNetworkDataSource(
        okhttpCallFactory: Call.Factory,
    ): ReadscapeNetworkDataSource {
        return RetrofitReadscapeNetwork(okhttpCallFactory)
    }
    @Provides
    @Singleton
    fun providesGoogleNetworkDataSource(
        okhttpCallFactory: Call.Factory,
    ) : GoogleNetworkDataSource {
        return RetrofitGoogleNetwork(okhttpCallFactory)
    }
    @Provides
    @Singleton
    fun provideRetrofitCmsNetwork(
        callFactory: Call.Factory
    ): CmsNetworkDatasource {
        return RetrofitCmsNetwork(callFactory)
    }
}


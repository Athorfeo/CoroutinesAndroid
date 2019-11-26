package com.athorfeo.source.di.module

import android.app.Application
import androidx.room.Room
import com.athorfeo.source.BuildConfig
import com.athorfeo.source.api.API
import com.athorfeo.source.database.AppDatabase
import com.athorfeo.source.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideApiService(): API {
        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.urlBase)
            .build()
            .create(API::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "appdatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao {
        return db.movieDao()
    }
}
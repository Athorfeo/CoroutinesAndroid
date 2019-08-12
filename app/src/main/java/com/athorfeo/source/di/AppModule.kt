package com.athorfeo.source.di

import android.app.Application
import androidx.room.Room
import com.athorfeo.source.api.API
import com.athorfeo.source.database.AppDatabase
import com.athorfeo.source.database.dao.MovieDao
import com.athorfeo.source.utility.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideApiService(): API {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
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
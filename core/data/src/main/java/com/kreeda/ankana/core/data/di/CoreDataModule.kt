package com.kreeda.ankana.core.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kreeda.ankana.core.data.repository.AuthRepository
import com.kreeda.ankana.core.data.repository.AuthRepositoryImpl
import com.kreeda.ankana.core.data.repository.BookingRepository
import com.kreeda.ankana.core.data.repository.BookingRepositoryImpl
import com.kreeda.ankana.core.data.repository.ChallengesRepository
import com.kreeda.ankana.core.data.repository.ChallengesRepositoryImpl
import com.kreeda.ankana.core.data.repository.GroundsRepository
import com.kreeda.ankana.core.data.repository.GroundsRepositoryImpl
import com.kreeda.ankana.core.data.repository.ProfileRepository
import com.kreeda.ankana.core.data.repository.ProfileRepositoryImpl
import com.kreeda.ankana.core.data.repository.ScoreRepository
import com.kreeda.ankana.core.data.repository.ScoreRepositoryImpl
import com.kreeda.ankana.core.database.KreedaDatabase
import com.kreeda.ankana.core.database.dao.BookingDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataProvidesModule {
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideKreedaDatabase(@ApplicationContext context: Context): KreedaDatabase {
        return Room.databaseBuilder(
            context,
            KreedaDatabase::class.java,
            "kreeda_ankana.db"
        ).build()
    }

    @Provides
    fun provideBookingDao(database: KreedaDatabase): BookingDao = database.bookingDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreDataBindModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindBookingRepository(
        impl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    abstract fun bindGroundsRepository(
        impl: GroundsRepositoryImpl
    ): GroundsRepository

    @Binds
    abstract fun bindChallengesRepository(
        impl: ChallengesRepositoryImpl
    ): ChallengesRepository

    @Binds
    abstract fun bindScoreRepository(
        impl: ScoreRepositoryImpl
    ): ScoreRepository

    @Binds
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}

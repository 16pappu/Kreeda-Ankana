package com.kreeda.ankana.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kreeda.ankana.core.database.dao.BookingDao
import com.kreeda.ankana.core.database.entity.BookingEntity
import com.kreeda.ankana.core.database.entity.ChallengeEntity
import com.kreeda.ankana.core.database.entity.GroundEntity
import com.kreeda.ankana.core.database.entity.ReplyEntity
import com.kreeda.ankana.core.database.entity.ScoreEntity
import com.kreeda.ankana.core.database.entity.SlotEntity
import com.kreeda.ankana.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GroundEntity::class,
        SlotEntity::class,
        BookingEntity::class,
        ChallengeEntity::class,
        ReplyEntity::class,
        ScoreEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class KreedaDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
}

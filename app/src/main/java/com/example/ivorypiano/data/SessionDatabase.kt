package com.example.ivorypiano.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
Defines the list of entites and DAOs.

 */

@Database(entities = [PianoSession::class], version = 1, exportSchema = false)
abstract class SessionDatabase : RoomDatabase(){

    abstract fun sessionDao(): PianoSessionDao

    // singleton magic
    companion object{
        @Volatile // ensures this massive block of memory is never cached
        private var Instance: SessionDatabase? = null

        fun getDatabase(context: Context): SessionDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                // prevents race condition where multiple threads accidentally make multiple instances
                Room.databaseBuilder(context, SessionDatabase::class.java, "session_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it}
            }
        }
    }
}
package com.example.todolist.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.utils.Constants

@Database(entities = [CalendarEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun CalendarDao(): CalendarDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getAppDatabase(context: Context):AppDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_TABLE,
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
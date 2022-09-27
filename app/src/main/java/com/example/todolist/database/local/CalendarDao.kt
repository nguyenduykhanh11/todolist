package com.example.todolist.database.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.utils.Constants

@Dao
interface CalendarDao {
    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE complete = '${Constants.NO_YET}'ORDER BY dayTime DESC,id ASC")
    fun readAllData(): LiveData<List<CalendarEntity>>

    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE complete = '${Constants.NO_YET}'AND role =:role ORDER BY dayTime DESC,id ASC")
    fun readAllRole(role: String): LiveData<List<CalendarEntity>>

    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE complete = '${Constants.DONE}'ORDER BY dayTime DESC,id ASC")
    fun readAllDataComplete(): LiveData<List<CalendarEntity>>

    @Query("SELECT * FROM ${Constants.CALENDAR_TABLE_NAME} WHERE name LIKE :searchQuery")
    fun searchData(searchQuery: String): LiveData<List<CalendarEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(calendar: CalendarEntity)

    @Delete
    suspend fun delete(calendar: CalendarEntity?)

    @Update
    suspend fun update(calendar: CalendarEntity?)
}
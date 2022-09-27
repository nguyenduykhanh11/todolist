package com.example.todolist.database.repository

import android.provider.CalendarContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.database.local.CalendarDao

class CalendarRepository(private val calendarDao: CalendarDao) {
    val readAllData: LiveData<List<CalendarEntity>> = calendarDao.readAllData()
    val readAllDataComplete: LiveData<List<CalendarEntity>> = calendarDao.readAllDataComplete()

    fun readAllRole(role :String):LiveData<List<CalendarEntity>>{
       return calendarDao.readAllRole(role)
    }
    fun searchData(searchQuery: String): LiveData<List<CalendarEntity>>{
        return calendarDao.searchData(searchQuery)
    }

    suspend fun insertCalendar(calendarEntity: CalendarEntity){
        calendarDao.insert(calendarEntity)
    }

    suspend fun updateCalendar(calendarEntity: CalendarEntity){
        calendarDao.update(calendarEntity)
    }
    suspend fun deleteCalendar(calendarEntity: CalendarEntity){
        calendarDao.delete(calendarEntity)
    }
}
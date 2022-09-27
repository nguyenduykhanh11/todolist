package com.example.todolist.database.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.utils.Constants

@Entity(tableName = Constants.CALENDAR_TABLE_NAME)
data class CalendarEntity (
    @PrimaryKey(autoGenerate = true) val id : Int?,
    @ColumnInfo(name="name")val name :String?,
    @ColumnInfo(name = "dayTime")val dayTime :String?,
    @ColumnInfo(name = "time")val time :String?,
    @ColumnInfo(name = "repeat")val repeat :String?,
    @ColumnInfo(name = "role")val role :String?,
    @ColumnInfo(name = "complete")val complete :String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(dayTime)
        parcel.writeString(time)
        parcel.writeString(repeat)
        parcel.writeString(role)
        parcel.writeString(complete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarEntity> {
        override fun createFromParcel(parcel: Parcel): CalendarEntity {
            return CalendarEntity(parcel)
        }

        override fun newArray(size: Int): Array<CalendarEntity?> {
            return arrayOfNulls(size)
        }
    }
}

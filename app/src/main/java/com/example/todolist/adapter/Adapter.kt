package com.example.todolist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.databinding.RecyclerDaytTimeBinding

import com.example.todolist.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class Adapter(private val showMenuOnlongClick: (Boolean) -> Unit) : RecyclerView.Adapter<Adapter.ViewHodel>() {
    private var calendarList = emptyList<CalendarEntity>()
    private var isEnable = false
    private val itemSelectedList = mutableListOf<CalendarEntity>()
    var deleteSelectItem: ((CalendarEntity) -> Unit)? = null
    var updateCheckBoxSelectItem: ((CalendarEntity) -> Unit)? = null
    var numOnLongclick: ((Int) -> Unit)? = null
    var updateCheckBoxUnSelectItem: ((CalendarEntity) -> Unit)? = null
    var onItemClick: ((CalendarEntity) -> Unit)? = null
    var selectedItemCheckBox: ((CalendarEntity) -> Unit)? = null
    var uncheckCheckbox: ((CalendarEntity) -> Unit)? = null

    class ViewHodel(val binding: RecyclerDaytTimeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        val view = RecyclerDaytTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHodel(view)
    }

    override fun onBindViewHolder(holder: Adapter.ViewHodel, position: Int) {
        val currenItem = calendarList[position]
        with(holder.binding){
            tvMission.text = currenItem.name
            tvDayTimeRecyclerView.text = currenItem.dayTime
            tvTimeRecyclerView.text = currenItem.time
            tvRoleRecyclerView.text = currenItem.role
        }
        setUpView(holder,currenItem)
        setUpTime(holder,currenItem)
        setUpCheckbox(holder, currenItem)
        setUpListenerCheckBoxItem(holder, currenItem)
        setUpListenerOnClickItem(holder, currenItem)
        setUpListenerLongClickItem(holder, currenItem)
    }

    override fun getItemCount(): Int = calendarList.size

    private fun setUpListenerOnClickItem(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        holder.binding.rowLayout.setOnClickListener {
            selectOnItem(holder, currenItem)
        }
    }

    private fun setUpListenerLongClickItem(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        holder.binding.rowLayout.setOnLongClickListener {
            selectItem(holder, currenItem)
            true
        }
    }

    private fun setUpView(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        setUpTextColor(holder,currenItem)
        if (itemSelectedList.size == 0) {
            holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.background_recycler_view)
        }

    }

    private fun setUpTextColor(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        with(holder.binding){
            if (currenItem.dayTime == "") {
                tvDayTimeRecyclerView.isVisible = false
                tvTimeRecyclerView.isVisible = false
            } else {
                tvDayTimeRecyclerView.isVisible = true
                tvTimeRecyclerView.isVisible = true
            }
        }
    }

    private fun selectOnItem(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        intentUpdate(currenItem)
        afterPressLong(holder,currenItem)
    }

    private fun intentUpdate(currenItem: CalendarEntity) {
        if (itemSelectedList.size == 0) {
            onItemClick?.invoke(currenItem)
        }
    }
    private fun afterPressLong(holder: Adapter.ViewHodel,currenItem: CalendarEntity) {
        if (itemSelectedList.contains(currenItem)) {
            holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.background_recycler_view)
            itemSelectedList.remove(currenItem)
            numOnLongclick?.invoke(itemSelectedList.size)
            if (itemSelectedList.size == 0) {
                showMenuOnlongClick(false)
                isEnable = false
            }
        }
        else if(isEnable){
            selectItem(holder, currenItem)
        }
    }

    private fun selectItem(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        isEnable = true
        itemSelectedList.add(currenItem)
        showMenuOnlongClick(true)
        holder.binding.recyclerViewBackground.setBackgroundResource(R.drawable.border)
        numOnLongclick?.invoke(itemSelectedList.size)
    }

    private fun setUpCheckbox(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        holder.binding.chkMissionRecyclerView.isChecked = currenItem.complete == Constants.DONE
    }

    private fun setUpListenerCheckBoxItem(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        holder.binding.chkMissionRecyclerView.setOnClickListener {
            when(holder.binding.chkMissionRecyclerView.isChecked){
                true->{
                    selectedItemCheckBox?.invoke(currenItem)
                }
                false->{
                    uncheckCheckbox?.invoke(currenItem)
                }
            }
        }
    }

    fun setData(calendar: List<CalendarEntity>) {
        this.calendarList = calendar
        notifyDataSetChanged()
    }

    fun deleteSelecterItem() {
        if (itemSelectedList.isNotEmpty()) {
            itemSelectedList.forEach {
                if (itemSelectedList.contains(it)) {
                    deleteSelectItem?.invoke(it)
                }
            }
            isEnable = false
            itemSelectedList.clear()
        }
    }
    fun unCheckbox() {
        if (itemSelectedList.isNotEmpty()) {
            itemSelectedList.forEach {
                if (itemSelectedList.contains(it)) {
                    updateCheckBoxUnSelectItem?.invoke(it)
                }
            }
            isEnable = false
            itemSelectedList.clear()
        }
    }

    fun CheckBoxSelecterItem() {
        if (itemSelectedList.isNotEmpty()) {
            itemSelectedList.forEach {
                if (itemSelectedList.contains(it)) {
                    updateCheckBoxSelectItem?.invoke(it)
                }
            }
            isEnable = false
            itemSelectedList.clear()
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun setUpTime(holder: Adapter.ViewHodel, currenItem: CalendarEntity) {
        var today = Date()
        val daytime =currenItem?.dayTime.toString()
        val hour =currenItem?.time.toString()
        if (daytime != ""){
            if (hour==""){
                val hourr = " 0:0"
                setTime(holder,today,daytime,hourr)
            }else{
                setTime(holder,today,daytime,hour)
            }

        }

    }

    @SuppressLint("ResourceAsColor")
    private fun setTime(holder: Adapter.ViewHodel, today: Date, daytime: String, hour: String) {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val formatTime = format.parse("${daytime}${hour}")
        val day = (today.time - formatTime.time)/86400000
        when (day.toInt()) {
            0 -> {
                val sethour= (today.time - formatTime.time)%86400000
                if(sethour >= 0){
                    holder.binding.tvDayTimeRecyclerView.setText("Ngày hôm nay")
                }else{
                    holder.binding.tvDayTimeRecyclerView.setText("Ngày mai")

                }
            }
            1 -> {
                with(holder.binding) {
                    tvDayTimeRecyclerView.setText("Ngày hôm qua")
                    tvDayTimeRecyclerView.setTextColor(R.color.red)
                    tvTimeRecyclerView.setTextColor(R.color.red)
                }
            }
//                NotificationIntent?.invoke(currenItem)
        }
    }
}
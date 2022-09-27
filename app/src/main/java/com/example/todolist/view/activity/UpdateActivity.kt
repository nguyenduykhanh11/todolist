package com.example.todolist.view.activity

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.*
import com.example.todolist.Notification
import com.example.todolist.viewModel.CalendarViewModel
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.databinding.ActivityUpdateBinding
import com.example.todolist.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var mCalendarViewModel: CalendarViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        mCalendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        setContentView(binding.root)
        setUpMenu()
        setUpView()
        setUpCalendar()
        createNotificationChanel()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val title = binding.edtMissionUpdate.text.toString()
        val message = binding.edtHourUpdate.text.toString()

        val intent = Intent(this.applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notifyID,
            intent,
            PendingIntent.FLAG_IMMUTABLE  or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val daytime =binding.edtDayTimeUpdate.text.toString()
        val hour =binding.edtHourUpdate.text.toString()
        val time = getTime(daytime,hour)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun getTime(daytime: String, hours: String): Long {
        val time = "${daytime} ${hours}"
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val formatTime = format.parse(time)

       // val formatMinute = SimpleDateFormat("mm")
//        val formatHour = SimpleDateFormat("HH")
//        val formatDay = SimpleDateFormat("dd")
//        val formatMonth = SimpleDateFormat("MM")
//        val formatYear = SimpleDateFormat("yyyy")
        val calendar = Calendar.getInstance()
        val minute = calendar.get(Calendar.MINUTE)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)


        calendar.set(2022, month, 23, 17, 17 )
        return calendar.timeInMillis


//        val currentime = format.format(calendar.getTime())
//        val _cvcurrent = format.parse(currentime);
//        val diff = (_cvcurrent.getTime() - datetime.getTime()) % 86400000 % 36000000 % 60000
//        val days=(_cvcurrent.getTime() - datetime.getTime()) / 86400000
//        val hours=(_cvcurrent.getTime() - datetime.getTime()) % 86400000 / 36000000
//        val minutes=(_cvcurrent.getTime() - datetime.getTime()) % 86400000 % 36000000 / 60000
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val hour = calendar.get(Calendar.HOUR)
//        val minute = calendar.get(Calendar.MINUTE)
//        val kq = minute+minutes
//        calendar.set(year,month, day,hour,53)
    }
//        val format1 = SimpleDateFormat("yyyy")
//        val format2 = SimpleDateFormat("MM")
//        val format3 = SimpleDateFormat("dd")
//        val format4 = SimpleDateFormat("HH")
//        val format5 = SimpleDateFormat("mm")
//        val formatTime = format.parse("${daytime} ${hour}")
//        val year = format1.parse("${daytime} ${hour}")
//        val month = format2.parse("${daytime} ${hour}")
//        val day = format3.parse("${daytime} ${hour}")
//        val hourr = format4.parse("${daytime} ${hour}")
//        val mili = format5.parse("${daytime} ${hour}")
//        calendar.set(year,month,day,hourr,mili)
//        return calendar.timeInMillis
//        val today = Date()
//        val format = SimpleDateFormat("yyyy/MM/dd HH:mm")
//        val formatTime = format.parse("${daytime} ${hourr}")
//        val calendar = DateFormat.getInstance().format(formatTime)
////        val dayy = (today.time - formatTime.time)%86400000/3600000
//
//        return Long
//        when(dayy.toInt()){
//            0->{
//                val calendar = Calendar.getInstance()
//                val year = calendar.get(Calendar.YEAR)
//                val month = calendar.get(Calendar.MONTH)
//                val day = calendar.get(Calendar.DAY_OF_MONTH)
//                val hour = calendar.get(Calendar.HOUR)
//                val minute = calendar.get(Calendar.MINUTE)
//                calendar.set(year, month, day, hour, minute)
//                calendar.timeInMillis
//            }
//            else -> {getTime(daytime, hourr.toString())}
//        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
        val name = "Notif Channel"
        val desc = "A desc"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateItem() {
        binding.floatingActionButtonUpdate.setOnClickListener {
            val calendarIntent = intent.getParcelableExtra<CalendarEntity>("calendar")
            val name = binding.edtMissionUpdate.text.toString()
            val dayTime = binding.edtDayTimeUpdate.text.toString()
            val time = binding.edtHourUpdate.text.toString()
            val repeat = binding.tvRoleRepeatUpdate.text.toString()
            val role = binding.tvRole.text.toString()
            if (binding.chkComplete.isChecked==true){
                val complete = Constants.DONE
                saveUpdateItem(calendarIntent?.id,name, dayTime, time, repeat, role, complete)
            }
            else{
                val complete = Constants.NO_YET
                saveUpdateItem(calendarIntent?.id,name, dayTime, time, repeat, role, complete)
                scheduleNotification()
            }
        }
    }
    private fun setUpCalendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        binding.lnlTime.setOnClickListener {
            val dpd =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    binding.edtDayTimeUpdate.setText("" + i + "/" + (i2+1) + "/" + i3)
                    binding.lnlHour.isVisible = true
                    binding.lnlRepeat.isVisible = true
                }, year, month, day)
            dpd.show()
        }

        binding.lnlHour.setOnClickListener {
            val dpd =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    binding.edtHourUpdate.setText(" " + i + ":" + i2)
                }, hour, minute, true)
            dpd.show()
        }
    }


    private fun saveUpdateItem(id: Int?, name: String, dayTime: String, time: String, repeat: String, role: String, complete: String) {
        if (inputCheck(name)) {
            val calendar = CalendarEntity(id, name, dayTime, time, repeat, role, complete)
            mCalendarViewModel.updateCalendar(calendar)
            Toast.makeText(
                this,
                "Update thành công",
                Toast.LENGTH_LONG
            ).show()
            finish()
        } else {
            Toast.makeText(this, "Bạn chưa nhập nhiệm vụ", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpView() {
        val calendar = intent.getParcelableExtra<CalendarEntity>("calendar")
        if (calendar != null) {
            with(binding){
                edtMissionUpdate.setText(calendar.name)
                edtDayTimeUpdate.setText(calendar.dayTime)
                edtHourUpdate.setText(calendar.time)
                tvRole.text = calendar.role
                if (calendar.complete == Constants.DONE){
                    chkComplete.isChecked = true
                }
            }
        }
        updateItem()
    }

    private fun setUpMenu() {
        setSupportActionBar(binding.toolbarUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onSupportNavigateUp(): Boolean {
        setUpListenerArrowBack()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.update_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_update_share -> {
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_update_delete -> {
                delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun arrowBack() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Bạn có chắc chắn?")
        builder.setMessage("Thoát mà không lưu?")

        builder.setPositiveButton("Vâng", DialogInterface.OnClickListener { dialogInterface, i ->
            finish()
            dialogInterface.dismiss()
        })
        builder.setNegativeButton("Hủy bỏ", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
    private fun setUpListenerArrowBack(){
        val calendar = intent.getParcelableExtra<CalendarEntity>("calendar")
        val name = binding.edtMissionUpdate.text.toString()
        val dayTime = binding.edtDayTimeUpdate.text.toString()
        val time = binding.edtHourUpdate.text.toString()
        val repeat = binding.tvRoleRepeatUpdate.text.toString()
        val role = binding.tvRole.text.toString()
        if (calendar != null) {
            if (calendar.name == name && calendar.dayTime==dayTime && calendar.time == time && calendar.repeat ==repeat && calendar.role == role){
                onBackPressed()
            }
            else{
                arrowBack()
            }
        }
    }

    private fun delete() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Bạn có chắc chắn?")

        builder.setNegativeButton("xóa", DialogInterface.OnClickListener { dialogInterface, i ->
            val calendar = intent.getParcelableExtra<CalendarEntity>("calendar")
            if (calendar != null) {
                mCalendarViewModel.deleteCalendar(calendar)
            }
            finish()
            dialogInterface.dismiss()
        })
        builder.setPositiveButton("Hủy bỏ", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}
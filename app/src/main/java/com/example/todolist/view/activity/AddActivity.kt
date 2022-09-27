package com.example.todolist.view.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.viewModel.CalendarViewModel
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.databinding.ActivityAddBinding
import com.example.todolist.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var mCalendarViewModel: CalendarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mCalendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        setUpView()
        setUpCalendar()
        setUpRepeat()
        setUpRole()
        setUpAddCalendar()
    }

    fun statusRole(){
        binding.tvRole.text.toString()
    }
    private fun setUpView() {
        val roleIntent = intent.getStringExtra("role")

        setSupportActionBar(binding.toolbarAdd)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Nhiệm vụ mới"
        //setCategory Add
        if (roleIntent == Constants.LIST_ALL_COLUMN){
            binding.tvRole.text = Constants.DEFAULT_COLUMN
        }else{
            binding.tvRole.text = roleIntent
        }

        val edtTime = binding.edtDayTime.text.toString()
        if (edtTime == "") {
            binding.lnlHour.isVisible = false
            binding.lnlRepeat.isVisible = false
        }
    }

    private fun setUpRepeat() {
        binding.tvRoleRepeat.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_no_repeat -> {
                        binding.tvRoleRepeat.text = "Không lặp lại"
                        true
                    }
                    R.id.item_once_a_day -> {
                        binding.tvRoleRepeat.text = "Một lần một ngày"
                        true
                    }
                    R.id.item_once_a_day_2_6 -> {
                        binding.tvRoleRepeat.text = "Một lần một ngày (Thứ Hai-Thứ Sáu)"
                        true
                    }
                    R.id.item_once_a_week -> {
                        binding.tvRoleRepeat.text = "Một lần một tuần"
                        true
                    }
                    R.id.item_once_a_month -> {
                        binding.tvRoleRepeat.text = "Một lần một tháng"
                        true
                    }
                    R.id.item_once_a_year -> {
                        binding.tvRoleRepeat.text = "Một lần một năm"
                        true
                    }
                    R.id.item_other -> {
                        binding.tvRoleRepeat.text = "Khác..."
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.loop_menu)
            popupMenu.show()
        }
    }

    private fun setUpRole() {
        binding.tvRole.setOnClickListener {
            val popUpMenu = PopupMenu(this, it)
            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_defaut -> {
                        binding.tvRole.text = Constants.DEFAULT_COLUMN
                        true
                    }
                    R.id.item_individuak -> {
                        binding.tvRole.text = Constants.INDIVIDUAL_COLUMN
                        true
                    }
                    R.id.item_shopping -> {
                        binding.tvRole.text = Constants.SHOPPING_COLUMN
                        true
                    }
                    R.id.item_work -> {
                        binding.tvRole.text = Constants.WORK_COLUMN
                        true
                    }
                    else -> false
                }
            }
            popUpMenu.inflate(R.menu.add_menu)
            popUpMenu.show()
        }
    }

    private fun setUpAddCalendar() {
        binding.floatingActionButtonSave.setOnClickListener {
            inserDataToDB()
        }
    }

    private fun inserDataToDB() {
        val name = binding.edtMission.text.toString()
        val dayTime = binding.edtDayTime.text.toString()
        val time = binding.edtHour.text.toString()
        val role = binding.tvRole.text.toString()
        val repeat = binding.tvRoleRepeat.text.toString()

        if (inputCheck(name)) {
            val calendar = CalendarEntity(null, name, dayTime, time, repeat, role, Constants.NO_YET)
            mCalendarViewModel.insertCalendar(calendar)
            Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            val snackbar =
                Snackbar.make(binding.root, "Nhập nhiệm vụ đầu tiên", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.BLACK)
            snackbar.setTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }

    override fun onSupportNavigateUp(): Boolean {
        val edtMission = binding.edtMission.text.toString()
        val edtDayTime = binding.edtDayTime.text.toString()
        if (edtMission != "" || edtDayTime != "") {
            arrowBack()
        } else {
            finish()
        }
        return super.onSupportNavigateUp()
    }

    private fun setUpCalendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        binding.lnlTime.setOnClickListener {
            val dpd =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    binding.edtDayTime.setText("" + i + "/" + (i2+1) + "/" + i3)
                    binding.lnlHour.isVisible = true
                    binding.lnlRepeat.isVisible = true
                }, year, month, day)
            dpd.show()
        }

        binding.imgAccessTime.setOnClickListener {
            val dpd =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    binding.edtHour.setText(" " + i + ":" + i2)
                }, hour, minute, true)
            dpd.show()
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
    // rén rồi thì nói đi cưng


}
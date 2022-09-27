package com.example.todolist.view.fragment

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.*
import com.example.todolist.adapter.Adapter
import com.example.todolist.viewModel.CalendarViewModel
import com.example.todolist.database.entities.CalendarEntity
import com.example.todolist.databinding.FragmentBinding
import com.example.todolist.view.activity.AddActivity
import com.example.todolist.view.activity.UpdateActivity
import com.example.todolist.utils.Constants
import com.google.android.material.snackbar.Snackbar
import java.util.*

class Fragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener{
    private lateinit var binding: FragmentBinding
    private lateinit var mCalendarViewModel: CalendarViewModel
    private lateinit var adapter: Adapter
    private var mainMenu: Menu? = null
    private var data: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBinding.inflate(inflater, container, false)
        adapter = Adapter { show -> showMenuOnlongClick(show) }
        mCalendarViewModel = ViewModelProvider(this@Fragment).get(CalendarViewModel::class.java)

        setUpView()
        setUpRecyclerView()
        setUpAdd()
        setUpListenerAdd()
        setUpListenerUpdate()
        setUpEvent()
        return binding.root
    }

    private fun setUpAdd() {
        binding.imgCheck.setOnClickListener {
            val name = binding.edtInputWork.text.toString()
            if (name!=""){
                when(data){
                    Constants.LIST_ALL_COLUMN->{
                        val calendar = CalendarEntity(null, name, "", "", "", Constants.DEFAULT_COLUMN, Constants.NO_YET)
                        mCalendarViewModel.insertCalendar(calendar)
                    }
                    else->{
                        val calendar = CalendarEntity(null, name, "", "", "", data, Constants.NO_YET)
                        mCalendarViewModel.insertCalendar(calendar)
                    }
                }
                binding.edtInputWork.setText("")
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Thành công", Snackbar.LENGTH_SHORT).run {
                    setBackgroundTint(Color.BLACK)
                    setTextColor(Color.WHITE)
                    show()
                }
            }
            else {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Chưa nhập nhiệm vụ", Snackbar.LENGTH_SHORT).run {
                    setBackgroundTint(Color.BLACK)
                    setTextColor(Color.WHITE)
                    show()
                }
            }
        }
    }


    private fun status(data: List<CalendarEntity>) {
        if (data.isEmpty() == true) {
            binding.tvList.text =
                "Danh sách ${(requireActivity() as AppCompatActivity).supportActionBar?.title} không có"
            this@Fragment.adapter.setData(data)
        } else {
            binding.tvList.isVisible = false
            this@Fragment.adapter.setData(data)
        }
    }

    private fun setUpView() {
        data = arguments?.get("Data").toString()
        setUpTitle(data!!)
        when (data) {
            Constants.LIST_ALL_COLUMN -> {
                mCalendarViewModel.readAllData.observe(viewLifecycleOwner, Observer { calendar ->
                    status(calendar)
                })
                hidden(true)
            }
            Constants.COMPLETE_COLUMN -> {
                mCalendarViewModel.readAllDataComplete.observe(viewLifecycleOwner, Observer { calendar ->
                        status(calendar)
                })
                hidden(false)
            }
            else -> {
                mCalendarViewModel.readAllRole(data!!).observe(viewLifecycleOwner, Observer { calendar ->
                        status(calendar)
                })
                hidden(true)
            }
        }
    }

    private fun hidden(show: Boolean) {
        with(binding){
            contranLayoutBottomMission.isVisible = show
            floatingActionButton.isVisible = show
        }
    }


    private fun setUpTitle(name: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = name
    }

    private fun setUpRecyclerView() {
        with(binding) {
            recyclerViewWork.run {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                this.adapter = this@Fragment.adapter
                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mainMenu = menu
        inflater.inflate(R.menu.main_menu, menu)
        showMenuOnlongClick(false)

        val search = menu!!.findItem(R.id.menu_search)
        val searchView = search.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!= null){
            searchData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!= null){
            searchData(newText)
        }
        return true
    }


    private fun searchData(query: String){
        val searchQuery ="%$query%"
        mCalendarViewModel.searchData(searchQuery).observe(viewLifecycleOwner, Observer { calendar ->
            this@Fragment.adapter.setData(calendar)
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete->{
                delete()
                true
            }
            R.id.share->{
                Toast.makeText(this.requireContext(), "share", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.checkbox->{
                selectCheckBox()
                true
            }
            R.id.un_checkbox->{
                unCheckbox()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun unCheckbox() {
        AlertDialog.Builder(this.requireContext()).run {
            setTitle("Bạn có chắc chắn?")
            setMessage("Tiếp tục nhiệm vụ?")
            setPositiveButton("Vâng"){_,_ ->
                adapter.unCheckbox()
                showMenuOnlongClick(false)
                val data = arguments?.get("Data").toString()
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    title = data
                    Toast.makeText(this.themedContext, "long click ngon", Toast.LENGTH_SHORT).show()
                    setDisplayUseLogoEnabled(true)
                    setLogo(R.mipmap.ic_logo)
                }
            }
            setNegativeButton("Hủy bỏ"){_,_ -> }
            show()
        }
    }

    private fun selectCheckBox() {
        AlertDialog.Builder(this.requireContext()).run {
            setTitle("Bạn có chắc chắn?")
            setMessage("Hoàn thành nhiệm vụ?")
            setPositiveButton("Vâng"){_,_ ->
                adapter.CheckBoxSelecterItem()
                showMenuOnlongClick(false)
                val data = arguments?.get("Data").toString()
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    title = data
                    Toast.makeText(this.themedContext, "long click ngon", Toast.LENGTH_SHORT).show()
                    setDisplayUseLogoEnabled(true)
                    setLogo(R.mipmap.ic_logo)
                }
            }
            setNegativeButton("Hủy bỏ"){_,_ -> }
            show()
        }
    }

    private fun delete() {
        AlertDialog.Builder(this.requireContext()).run {
            setTitle("Bạn có chắc chắn?")
            setMessage("Xóa nhiệm vụ?")
            setPositiveButton("Vâng"){_,_ ->
                adapter.deleteSelecterItem()
                showMenuOnlongClick(false)
                val data = arguments?.get("Data").toString()
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    title = data
                    setDisplayUseLogoEnabled(true)
                    setLogo(R.mipmap.ic_logo)
                }
            }
            setNegativeButton("Hủy bỏ"){_,_ -> }
            show()
        }
    }

    fun showMenuOnlongClick(show: Boolean) {

        val data = arguments?.get("Data").toString()
        when(data){
            Constants.COMPLETE_COLUMN->{
                mainMenu?.findItem(R.id.checkbox)?.isVisible = false
                mainMenu?.findItem(R.id.share)?.isVisible = show
                mainMenu?.findItem(R.id.delete)?.isVisible = show
                mainMenu?.findItem(R.id.un_checkbox)?.isVisible = show
                mainMenu?.findItem(R.id.menu_category)?.isVisible = !show
                mainMenu?.findItem(R.id.menu_search)?.isVisible = !show
                mainMenu?.findItem(R.id.menu3)?.isVisible = !show
                mainMenu?.findItem(R.id.menu4)?.isVisible = !show
                mainMenu?.findItem(R.id.menu5)?.isVisible = !show
                mainMenu?.findItem(R.id.menu6)?.isVisible = !show
            }
            else->{
                mainMenu?.findItem(R.id.checkbox)?.isVisible = show
                mainMenu?.findItem(R.id.share)?.isVisible = show
                mainMenu?.findItem(R.id.delete)?.isVisible = show
                mainMenu?.findItem(R.id.un_checkbox)?.isVisible = false
                mainMenu?.findItem(R.id.menu_category)?.isVisible = !show
                mainMenu?.findItem(R.id.menu_search)?.isVisible = !show
                mainMenu?.findItem(R.id.menu3)?.isVisible = !show
                mainMenu?.findItem(R.id.menu4)?.isVisible = !show
                mainMenu?.findItem(R.id.menu5)?.isVisible = !show
                mainMenu?.findItem(R.id.menu6)?.isVisible = !show
            }
        }
    }

    private fun setUpEvent() {
        adapter.selectedItemCheckBox = {
            val calendar = CalendarEntity(it.id, it.name, it.dayTime, it.time, it.repeat, it.role, Constants.DONE)
            mCalendarViewModel.updateCalendar(calendar)
        }

        adapter.uncheckCheckbox = {
            val calendar = CalendarEntity(it.id, it.name, it.dayTime, it.time, it.repeat, it.role, Constants.NO_YET)
            mCalendarViewModel.updateCalendar(calendar)
        }

        val name = (requireActivity() as AppCompatActivity).supportActionBar?.title
        adapter.numOnLongclick = {
            (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                title = (it.toString())
                setDisplayUseLogoEnabled(false)
            }
            if (it == 0) {
                (requireActivity() as AppCompatActivity).supportActionBar?.apply {
                    title = name
                    setDisplayUseLogoEnabled(true)
                    setLogo(R.mipmap.ic_logo)
                }
            }
        }

        adapter.deleteSelectItem={
            mCalendarViewModel.deleteCalendar(it)
        }

        adapter.updateCheckBoxSelectItem={
            val calendar = CalendarEntity(it.id, it.name, it.dayTime, it.time, it.repeat, it.role, Constants.DONE)
            mCalendarViewModel.updateCalendar(calendar)
        }

        adapter.updateCheckBoxUnSelectItem={
            val calendar = CalendarEntity(it.id, it.name, it.dayTime, it.time, it.repeat, it.role, Constants.NO_YET)
            mCalendarViewModel.updateCalendar(calendar)
        }


    }

    private fun setUpListenerAdd() {
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this.requireContext(), AddActivity::class.java)
            intent.putExtra("role", (requireActivity() as AppCompatActivity).supportActionBar?.title)
            startActivity(intent)
        }
    }

    private fun setUpListenerUpdate() {
        adapter.onItemClick = {
            val intent = Intent(this@Fragment.requireContext(), UpdateActivity::class.java)
            intent.putExtra("calendar", it)
            startActivity(intent)
        }
    }
}
package com.example.todolist.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.view.fragment.Fragment
import com.example.todolist.utils.Constants

class   MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        setSupportActionBar(binding.toolbar)
        replaceFragment(Fragment(),Constants.LIST_ALL_COLUMN)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.sub_list_all ->{
                replaceFragment(Fragment(),Constants.LIST_ALL_COLUMN)
                true
            }
            R.id.sub_default ->{
                replaceFragment(Fragment(),Constants.DEFAULT_COLUMN)
                true
            }
            R.id.sub_shopping ->{
                replaceFragment(Fragment(),Constants.SHOPPING_COLUMN)
                true
            }
            R.id.sub_work ->{
                replaceFragment(Fragment(),Constants.WORK_COLUMN)
                true
            }
            R.id.sub_individual ->{
                replaceFragment(Fragment(),Constants.INDIVIDUAL_COLUMN)
                true
            }
            R.id.sub_complete ->{
                replaceFragment(Fragment(),Constants.COMPLETE_COLUMN)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment,data: String){
        val fragment = Fragment()
        val bundle = Bundle()
        bundle.putString("Data", data)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment).commit()
        invalidateOptionsMenu()
    }
}
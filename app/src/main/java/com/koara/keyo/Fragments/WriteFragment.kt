package com.koara.keyo.Fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.koara.keyo.Model.Entity
import com.koara.keyo.ViewModel.ViewModel
import com.koara.keyo.databinding.FragmentWriteBinding

class WriteFragment : Fragment() {
    private lateinit var binding : FragmentWriteBinding
    private lateinit var tempViewModel : ViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteBinding.inflate(layoutInflater)
        tempViewModel = ViewModelProvider(this)[ViewModel::class.java]
        binding.addButton.setOnClickListener {
            insertDataToTable()
        }
        return binding.root
    }
    //Inserts Data To Database
    private fun insertDataToTable(){
        val site = binding.siteInput.text.toString()
        val username = binding.usernameInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if(inputCheck(site, username, password)){
            val credential = Entity(0,site,username,password)
            //Add Data to Database
            tempViewModel.addCredentials(credential)
            Toast.makeText(requireContext(),"password saved successfully",Toast.LENGTH_SHORT).show()
            binding.siteInput.text = null
            binding.usernameInput.text = null
            binding.passwordInput.text = null
        }else{
            Toast.makeText(requireContext(),"please enter valid inputs",Toast.LENGTH_SHORT).show()
        }
    }
    //Checks Inputs before saving
    private fun inputCheck(site : String,username :String,password :String) : Boolean{
        return !(TextUtils.isEmpty(site) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
    }

}
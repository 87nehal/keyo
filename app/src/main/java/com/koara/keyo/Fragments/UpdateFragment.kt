package com.koara.keyo.Fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.koara.keyo.Model.Entity
import com.koara.keyo.R
import com.koara.keyo.ViewModel.ViewModel
import com.koara.keyo.databinding.FragmentUpdateBinding
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {
    private lateinit var binding : FragmentUpdateBinding
    private lateinit var tempViewModel : ViewModel
    private lateinit var clipBoard : ClipboardManager
    private val args by navArgs<UpdateFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(layoutInflater)
        clipBoard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        tempViewModel = ViewModelProvider(this)[ViewModel::class.java]
        binding.updateSiteInput.setText(args.currentCredentials.site)
        binding.updateUsernameInput.setText(args.currentCredentials.username)
        binding.updatePasswordInput.setText(args.currentCredentials.password)
        strengthIndicator()
        binding.updateButton.setOnClickListener{
            updateCredentials()
        }

        binding.copyUsername.setOnClickListener{
            copyUsername()
        }
        binding.copyPassword.setOnClickListener{
            copyPassword()
        }

        setHasOptionsMenu(true)

        return binding.root
    }
    //Handles Update
    private fun updateCredentials(){
        val site = updateSiteInput.text.toString()
        val username = updateUsernameInput.text.toString()
        val password = updatePasswordInput.text.toString()
        if(inputCheck(site,username,password)){
            val updatedCredentails =Entity(args.currentCredentials.id,site,username, password)
            tempViewModel.updateCredentials(updatedCredentails)
            Toast.makeText(requireContext(),"credentials updated",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(),"credentials not updated!!",Toast.LENGTH_SHORT).show()
        }
    }

    //Checks if all inputs are valid or not
    private fun inputCheck(site : String,username :String,password :String) : Boolean{
        return !(TextUtils.isEmpty(site) && TextUtils.isEmpty(username) && TextUtils.isEmpty(password))
    }

    //Adds Delete Menu Option
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete){
            deleteCredentials()
        }
        return super.onOptionsItemSelected(item)
    }

    //Handles delete od details
    private fun deleteCredentials(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("delete credentials")
            .setMessage("once deleted credentials cannot be recovered\nwanna still continue?")
            .setPositiveButton("no"){ _, _ ->
            }
            .setNegativeButton("yes"){ _, _ ->
                tempViewModel.deleteCredential(args.currentCredentials)
                Toast.makeText(requireContext(),"credential deleted successfully",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_feedFragment)
            }
            .show()
    }

    //Handles copy username
    private fun copyUsername(){
        val username  = binding.updateUsernameInput.text.toString()
        val clip = ClipData.newPlainText("username",username)
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(requireContext(),"username copied",Toast.LENGTH_SHORT).show()
    }

    //Handles copy password
    private fun copyPassword(){
        val password  = binding.updatePasswordInput.text.toString()
        val clip = ClipData.newPlainText("password",password)
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(requireContext(),"password copied",Toast.LENGTH_SHORT).show()
    }

    //Sets the color for strength of password
    private fun strengthIndicator(){
        val strength = passwordStrength(args.currentCredentials.password)
        when {
            (strength <= 2)->{
                binding.strengthIndicator.setBackgroundResource(R.color.notsecure)
            }
            (strength == 3)->{
                binding.strengthIndicator.setBackgroundResource(R.color.okay)
            }
            (strength == 4)->{
                binding.strengthIndicator.setBackgroundResource(R.color.good)
            }
            (strength >= 5)->{
                binding.strengthIndicator.setBackgroundResource(R.color.excellent)
            }
        }
    }
    private fun passwordStrength(password:String):Int{
        var check = 0
        if(password.length>8){
            check++
        }else if(password.length>16){
            check+=2
        }
        if(isSpecial(password)) check++

        if(isNumber(password)) check++

        if(isLowerCase(password)) check++

        if (isUpperCase(password)) check++
        return check
    }
    private fun isSpecial(s: String): Boolean {
        var hasSpecial = false
        val chars = ('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
        for(i in s){
            for (j in chars){
                if (i==j){
                    hasSpecial=true
                }
            }
        }
        return !TextUtils.isEmpty(s) && hasSpecial
    }
    private fun isNumber(s: String): Boolean {
        val hasDigits = s.any { it.isDigit() }
        return !TextUtils.isEmpty(s) && hasDigits
    }
    private fun isUpperCase(s: String): Boolean {
        val hasLower = s.any { it.isLowerCase() }
        return !TextUtils.isEmpty(s) && hasLower
    }
    private fun isLowerCase(s: String): Boolean {
        val hasUpper = s.any { it.isUpperCase() }
        return !TextUtils.isEmpty(s) && hasUpper
    }
}
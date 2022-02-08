package com.koara.keyo.Fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.koara.keyo.databinding.FragmentUtilityBinding

/*
    Generates Password
*/
class UtilityFragment : Fragment() {
    private lateinit var binding : FragmentUtilityBinding
    private lateinit var clipBoard : ClipboardManager
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUtilityBinding.inflate(layoutInflater)
        clipBoard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val length = binding.passwordLengthSeekBar
        //Set max and min of seek bar -> Password Length
        length.min = 5
        length.max = 25
        var passwordLength: Int
        length.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(length: SeekBar, progress: Int, fromUser: Boolean) {
                binding.lengthTextEditor.setText(length.progress.toString())
            }

            override fun onStartTrackingTouch(length: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(length: SeekBar) {
                passwordLength = length.progress
            }
        })
        //Generate password button
        binding.generatePasswordButton.setOnClickListener{
            passwordLength = binding.lengthTextEditor.text.toString().toInt()
            generatePassword(passwordLength)
        }
        //Copy generated password
        binding.copyGeneratedPassword.setOnClickListener{
            var password  = binding.generatedPassword.text.toString()
            password = password.subSequence(10,password.length) as String
            val clip = ClipData.newPlainText("username",password)
            clipBoard.setPrimaryClip(clip)
            Toast.makeText(requireContext(),"password copied",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
    //Generates the password
    private fun randomPassword(length : Int , range : List<Char>): String = List(length) { range .random() }.joinToString("")

    //Handles type of password
    @SuppressLint("SetTextI18n")
    private fun generatePassword(passwordLength : Int){
        val lower = binding.lowerCaseCheckBox.isChecked
        val upper = binding.upperCaseCheckBox.isChecked
        val num = binding.numberCaseCheckBox.isChecked
        val symbol = binding.specialCaseCheckBox.isChecked
        when{
            (lower && upper && num && symbol)->{
                val chars = ('a'..'z')+('A'..'Z')+('0'..'9')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (upper && num && symbol)-> {
                val chars = ('A'..'Z')+('0'..'9')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower && num && symbol)->{
                val chars = ('a'..'z')+('0'..'9')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower && upper && symbol)->{
                val chars = ('a'..'z')+('A'..'Z')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower && num && upper)->{
                val chars = ('a'..'z')+('0'..'9')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (num && symbol)->{
                val chars = ('0'..'9')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (upper && symbol)->{
                val chars = ('A'..'Z')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower && symbol)->{
                val chars = ('a'..'z')+('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (upper && num)->{
                val chars = ('A'..'Z')+('0'..'9')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower && num)->{
                val chars = ('a'..'z')+('0'..'9')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower && upper)->{
                val chars = ('a'..'z')+('A'..'Z')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (symbol)->{
                val chars = ('!'..'/')+(':'..'@')+('['..'`')+('{'..'~')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (num)->{
                val chars = ('0'..'5')+('6'..'9')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (upper)->{
                val chars = ('A'..'X')+('Y'..'Z')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
            (lower)->{
                val chars = ('a'..'x')+('y'..'z')
                binding.generatedPassword.text = "password : "+randomPassword(passwordLength,chars)
            }
        }
    }
}


package com.koara.keyo.Fragments

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.koara.keyo.BuildConfig
import com.koara.keyo.R
import com.koara.keyo.UserSettings
import com.koara.keyo.database.Databse
import com.koara.keyo.databinding.FragmentSettingsBinding
import com.mortgage.fauxiq.pawnbroker.utils.CSVWriter
import java.io.File
import java.io.FileWriter


class SettingsFragment : Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    private lateinit var userSettings: UserSettings
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userSettings = UserSettings(requireContext())
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.darkThemeToggle.isChecked = userSettings.loadDarkModeState()
        //Dark Mode Toggle
        binding.darkThemeToggle.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                userSettings.setDarkModeState(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                userSettings.setDarkModeState(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        binding.appLockToggle.isChecked = userSettings.loadLockState()
        //App Lock Toggle
        binding.appLockToggle.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                userSettings.setLockState(true)
                Toast.makeText(requireContext(),"all your passwords are safe now",Toast.LENGTH_SHORT).show()
            }else{
                userSettings.setLockState(false)
                Toast.makeText(requireContext(),"anyone can see your passwords now",Toast.LENGTH_SHORT).show()
            }
        }
        //Handle FeedBack
        binding.feedback.setOnClickListener { sendFeedback() }
        //Handle rateApp
        binding.rateus.setOnClickListener { rateApp() }
        //Handel Export Csv
        binding.exportCsv.setOnClickListener{
            if(checkPermission()){
                exportCredentials()
            }else{
                takePermission()
            }
        }

        binding.versionText.text = getVersion()
        return binding.root
    }
    //Checks file permission
    private fun checkPermission():Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }else{
            val writePermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            writePermission == PackageManager.PERMISSION_GRANTED
        }
    }
    //Requests file permission
    private fun takePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse("package:${requireContext().applicationContext.packageName}")
                ContextCompat.startActivity(requireContext(), intent, null)
            }catch (e: java.lang.Exception){
                Toast.makeText(requireContext(),"failed to get permission",Toast.LENGTH_SHORT).show()
            }
        }else{
            try {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),967)
            }catch (e: java.lang.Exception){
                Toast.makeText(requireContext(),"failed to get permission",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Getting App Build Details
    private fun getVersion():String{
        val versionName: String = BuildConfig.VERSION_NAME
        val versionCode: String = BuildConfig.VERSION_CODE.toString()
        return "Version : $versionName | Build : $versionCode"
    }
    //Opens Playstore to rate the app
    private fun rateApp() {
        /* get package name */
        val appPackageName = requireContext().packageName

        /* handle link of the Google Play Store */
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (errorException: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }
    private fun sendFeedback() {
        val feedbackIntent = Intent(Intent.ACTION_SEND)
        feedbackIntent.type = "text/pain"

        // check if GMAIL app is installed
        feedbackIntent.setPackage("com.google.android.gm")
        feedbackIntent.putExtra(
            Intent.EXTRA_EMAIL,
            //Email Here
            arrayOf("87nehal@gmail.com")
        )
        feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, arrayOf(getString(R.string.feedbackSubject)))

        // try to open gmail app, if !installed, exception will be showed
        try {
            startActivity(feedbackIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, getString(R.string.nogmail), Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportCredentials(){
        val exportDir = File(Environment.getExternalStorageDirectory(), "/keyo")// your path where you want save your file
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val fileName = "keyoExport"
        val fileCsv = File(exportDir, "$fileName.csv")
        val fileTxt = File(exportDir, "$fileName.txt")//$TABLE_NAME.csv is like user.csv or any name you want to save
        try {
            fileCsv.createNewFile()
            fileTxt.createNewFile()
            val csvWrite = CSVWriter(FileWriter(fileCsv))
            val txtWrite = CSVWriter(FileWriter(fileTxt))
            val curCSV = Databse.getDatabase(requireContext()).query("SELECT * FROM keyoTable", null)// query for get all data of your database table
            csvWrite.writeNext(curCSV.columnNames)
            txtWrite.writeNext(curCSV.columnNames)
            while (curCSV.moveToNext()) {
                //Which column you want to export
                val arrStr = arrayOfNulls<String>(curCSV.columnCount)
                for (i in 0 until curCSV.columnCount - 0) {
                    when (i) {
                        20, 22 -> {
                        }
                        else -> arrStr[i] = curCSV.getString(i)
                    }
                }
                csvWrite.writeNext(arrStr)
                txtWrite.writeNext(arrStr)
            }
            csvWrite.close()
            txtWrite.close()
            curCSV.close()
            Toast.makeText(requireContext(),"exported : ../keyo/keyoExport",Toast.LENGTH_SHORT).show()
        } catch (sqlEx: Exception) {
            Toast.makeText(requireContext(),"error exporting failed",Toast.LENGTH_SHORT).show()
        }

    }
}
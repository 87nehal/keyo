package com.koara.keyo

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.koara.keyo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var userSettings: UserSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        userSettings = UserSettings(this)
        //Checking Settings
        if(userSettings.loadDarkModeState()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        if(userSettings.loadLockState()){
            val keyguardManager : KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if(keyguardManager.isKeyguardSecure){
            val intent = keyguardManager.createConfirmDeviceCredentialIntent("enter lockscreen password",null)
            startActivityForResult(intent,100)
            }else{
                userSettings.setLockState(false)
                Toast.makeText(this,"no password found, turning setting off",Toast.LENGTH_SHORT).show()
            }
        }
        super.onCreate(savedInstanceState)
        //New Splash Screen Api
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        //Disabling Screen Capture in the app
        this.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(binding.root)

        //Establishing Bottom Nav
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        binding.bottomMenu.setupWithNavController(navController)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    //Setting Permission For Locking App
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){
            if(resultCode == RESULT_OK){}
            if(resultCode == RESULT_CANCELED){
                finish()
            }
        }else{
            finish()
        }
    }
}
package com.example.trailsurfing

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trailsurfing.data.Point
import com.example.trailsurfing.data.Route
import com.example.trailsurfing.databinding.ActivityMainBinding
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object {
        lateinit var login: String
        lateinit var route: Route
        var path: MutableList<Point>? = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

       singin()
        //создание контроллера навигации
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
        //подключение нжней пнаели к навигации
        with(binding) {
            bottomNav.setupWithNavController(navController)
        }
    }

    private fun singin() {
        val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = pref.edit()

        login = pref.getString("login", "Anon").toString()

        if(login == "Anon"){
            login = UUID.randomUUID().toString()
            editor.apply { //Сохранение UUID пользователя
                putString("login", login)
            }.apply()
        }
    }
}

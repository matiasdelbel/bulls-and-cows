package com.delbel.bullscows.app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.delbel.bullscows.app.R
import com.delbel.bullscows.app.databinding.ScreenMainBinding

class MainScreen : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var screenBinding: ScreenMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenBinding = ScreenMainBinding.inflate(layoutInflater)
        setContentView(screenBinding.root)

        navController = findNavController(R.id.nav_host_fragment)
        setUpToolbar()
    }

    private fun setUpToolbar() {
        val toolbarConfig = AppBarConfiguration(topLevelDestinationIds = setOf(R.id.screen_menu))

        setSupportActionBar(screenBinding.toolbar)
        screenBinding.toolbar.setupWithNavController(navController, toolbarConfig)
    }
}
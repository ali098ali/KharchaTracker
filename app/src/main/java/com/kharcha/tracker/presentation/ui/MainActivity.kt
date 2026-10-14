package com.kharcha.tracker.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kharcha.tracker.R
import com.kharcha.tracker.databinding.ActivityMainBinding
import com.kharcha.tracker.presentation.ui.lock.LockActivity
import com.kharcha.tracker.utils.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fromLock = intent?.getBooleanExtra("from_lock", false) == true
        if (fromLock || savedInstanceState != null) {
            setupUi()
            return
        }
        val settings = SettingsDataStore(this)
        lifecycleScope.launch {
            val needLock = settings.pinEnabled.first() || settings.biometricEnabled.first()
            if (needLock) {
                startActivity(Intent(this@MainActivity, LockActivity::class.java))
                finish()
            } else {
                runOnUiThread { setupUi() }
            }
        }
    }

    private fun setupUi() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nav = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNav.setupWithNavController(nav.navController)
    }
}

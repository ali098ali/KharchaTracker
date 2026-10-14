package com.kharcha.tracker.presentation.ui.lock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kharcha.tracker.databinding.ActivityLockBinding
import com.kharcha.tracker.presentation.ui.MainActivity
import com.kharcha.tracker.utils.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.security.MessageDigest

@AndroidEntryPoint
class LockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockBinding
    private lateinit var settings: SettingsDataStore
    private var enteredPin = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settings = SettingsDataStore(this)
        setupKeypad()
        lifecycleScope.launch {
            if (settings.biometricEnabled.first()) tryBiometric()
        }
    }

    private fun setupKeypad() {
        val map = mapOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9"
        )
        map.forEach { (btn, digit) -> btn.setOnClickListener { onDigit(digit) } }
        binding.btnClear.setOnClickListener { enteredPin.clear(); updateDots() }
        binding.btnBio.setOnClickListener { tryBiometric() }
    }

    private fun onDigit(d: String) {
        if (enteredPin.length >= 4) return
        enteredPin.append(d)
        updateDots()
        if (enteredPin.length == 4) lifecycleScope.launch { verifyPin(enteredPin.toString()) }
    }

    private fun updateDots() {
        val len = enteredPin.length
        binding.dot1.alpha = if (len >= 1) 1f else 0.3f
        binding.dot2.alpha = if (len >= 2) 1f else 0.3f
        binding.dot3.alpha = if (len >= 3) 1f else 0.3f
        binding.dot4.alpha = if (len >= 4) 1f else 0.3f
    }

    private suspend fun verifyPin(pin: String) {
        val hash = sha256(pin)
        val stored = settings.pinHash.first()
        if (stored.isEmpty() || stored == hash) {
            if (stored.isEmpty()) settings.setPinHash(hash)
            unlock()
        } else {
            Toast.makeText(this, "Wrong PIN", Toast.LENGTH_SHORT).show()
            enteredPin.clear()
            updateDots()
        }
    }

    private fun tryBiometric() {
        val manager = BiometricManager.from(this)
        if (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this, "Biometric not available", Toast.LENGTH_SHORT).show()
            return
        }
        val prompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) = unlock()
                override fun onAuthenticationError(code: Int, msg: CharSequence) {
                    Toast.makeText(this@LockActivity, msg, Toast.LENGTH_SHORT).show()
                }
            })
        prompt.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Expense Tracker unlock")
                .setSubtitle("Use fingerprint or face")
                .setNegativeButtonText("Use PIN")
                .build()
        )
    }

    private fun unlock() {
        startActivity(Intent(this, MainActivity::class.java).putExtra("from_lock", true))
        finish()
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

package com.kharcha.tracker.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore("kharcha_settings")

class SettingsDataStore(private val context: Context) {
    private val ds = context.settingsDataStore

    val currency: Flow<String> = ds.data.map { it[Keys.CURRENCY] ?: "PKR" }
    val darkMode: Flow<Boolean> = ds.data.map { it[Keys.DARK] ?: false }
    val pinEnabled: Flow<Boolean> = ds.data.map { it[Keys.PIN_ENABLED] ?: false }
    val pinHash: Flow<String> = ds.data.map { it[Keys.PIN_HASH] ?: "" }
    val biometricEnabled: Flow<Boolean> = ds.data.map { it[Keys.BIOMETRIC] ?: false }
    val notificationsEnabled: Flow<Boolean> = ds.data.map { it[Keys.NOTIF] ?: true }

    suspend fun setCurrency(c: String) = ds.edit { it[Keys.CURRENCY] = c }
    suspend fun setDarkMode(v: Boolean) = ds.edit { it[Keys.DARK] = v }
    suspend fun setPinEnabled(v: Boolean) = ds.edit { it[Keys.PIN_ENABLED] = v }
    suspend fun setPinHash(h: String) = ds.edit { it[Keys.PIN_HASH] = h }
    suspend fun setBiometric(v: Boolean) = ds.edit { it[Keys.BIOMETRIC] = v }
    suspend fun setNotifications(v: Boolean) = ds.edit { it[Keys.NOTIF] = v }

    private object Keys {
        val CURRENCY = stringPreferencesKey("currency")
        val DARK = booleanPreferencesKey("dark")
        val PIN_ENABLED = booleanPreferencesKey("pin_enabled")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val BIOMETRIC = booleanPreferencesKey("biometric")
        val NOTIF = booleanPreferencesKey("notif")
    }
}

package com.udhipe.githubuserex.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.udhipe.githubuserex.R
import com.udhipe.githubuserex.utility.ReminderReceiver
import java.util.*


class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var mREMINDER: String
    private lateinit var mLANGUAGE: String

    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: Preference

    private lateinit var sharedPreference: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()

        sharedPreference = preferenceManager.sharedPreferences
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val locale = Locale.getDefault().displayLanguage
        sharedPreference.edit().putString(mLANGUAGE, locale).apply()

        Toast.makeText(context, locale, Toast.LENGTH_SHORT).show()

    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setSummaries() {

        reminderPreference.isChecked = sharedPreference.getBoolean(mREMINDER, true)
        languagePreference.summary = sharedPreference.getString(mLANGUAGE, "tidak ada")
    }

    private fun init() {
        mREMINDER = getString(R.string.reminder_key)
        mLANGUAGE = getString(R.string.language_key)

        reminderPreference = findPreference<SwitchPreference>(mREMINDER) as SwitchPreference
        languagePreference = findPreference<Preference>(mLANGUAGE) as Preference

        languagePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
    }


    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences?, key: String?) {
        if (key == mREMINDER) {
            reminderPreference.isChecked = sharedPreference?.getBoolean(mREMINDER, true)!!

            val reminderReciever = ReminderReceiver()
            if (sharedPreference.getBoolean(mREMINDER, true)) {
                // test
//                val sdf = SimpleDateFormat("HH:mm")
//                val current = Calendar.getInstance()
//                var time = sdf.format(current.time)

                val time = "09:00"
                context?.let { reminderReciever.setReminder(it, time, "reminder") }

            } else {
                context?.let { reminderReciever.cancelReminder(it) }
            }

        }

        if (key == mLANGUAGE) {
            languagePreference.summary = sharedPreference?.getString(mLANGUAGE, "tidak ada")
        }
    }
}
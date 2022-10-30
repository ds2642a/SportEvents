package com.company.sportevents.ui.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.company.sportevents.R
import com.company.sportevents.data.model.JSONResponse
import com.company.sportevents.databinding.FragmentLoadingBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.GsonBuilder
import com.jaredrummler.android.device.DeviceName
import com.onesignal.OneSignal
import java.util.*


class LoadingFragment : Fragment() {
    private val ONESIGNAL_APP_ID = "########-####-####-####-############"

    private lateinit var binding : FragmentLoadingBinding
    private var viewURL : String? = null
    private var sharedPref : SharedPreferences? = null
    private var deviceName : String? = null
    private var deviceManuf : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoadingBinding.bind(view)
        sharedPref = context?.getSharedPreferences("prefs", MODE_PRIVATE)

        initOneSignal()
        getDeviceInfo()
        getViewURL()
    }

    private fun initOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        context?.let { OneSignal.initWithContext(it) }
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

    private fun getDeviceInfo() {
        DeviceName.init(context)
        deviceName = DeviceName.getDeviceName()
        deviceManuf = Build.MANUFACTURER
    }

    private fun getViewURL() {
        getViewURLFromPrefs()
        if (viewURL == null) {
            getViewURLFromFirebase()
        } else {
            checkURL()
        }
    }

    private fun getViewURLFromPrefs() {
        viewURL = sharedPref?.getString("viewURL", null)
    }

    private fun getViewURLFromFirebase() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        activity?.let {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        val data = remoteConfig["URLData"].asString()
                        val gson = GsonBuilder().create()
                        val response = gson.fromJson(data, JSONResponse::class.java)
                        viewURL = response.URL
                        checkURL()
                    }
                }
        }
    }

    private fun checkURL() {
        if (viewURL?.isEmpty() == true || isGoogle() || isNoSim() || isEmulator()) {
            goToEventsFragment()
        } else {
            saveViewURLToPrefs()
            goToViewFragment()
        }
    }

    private fun isGoogle() : Boolean {
        return deviceManuf?.contains("google", true) ?: true || deviceName?.contains("pixel", true) ?: true
    }

    private fun isNoSim(): Boolean {
        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simState == TelephonyManager.SIM_STATE_ABSENT
    }

    private fun isEmulator(): Boolean {
        return (Build.MANUFACTURER.contains("Genymotion")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.lowercase(Locale.ROOT).contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MODEL.contains("Android SDK built for arm64")
                || Build.HARDWARE == "goldfish"
                || Build.HARDWARE == "vbox86"
                || Build.HARDWARE.lowercase(Locale.ROOT).contains("nox")
                || Build.HARDWARE.lowercase(Locale.ROOT).contains("ranchu")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.PRODUCT == "sdk"
                || Build.PRODUCT == "google_sdk"
                || Build.PRODUCT == "sdk_x86"
                || Build.PRODUCT == "vbox86p"
                || Build.PRODUCT.lowercase(Locale.ROOT).contains("nox")
                || Build.BOARD.lowercase(Locale.ROOT).contains("nox")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")))
    }

    private fun saveViewURLToPrefs() {
        sharedPref?.edit()?.putString("viewURL", viewURL)?.apply()
    }

    private fun goToEventsFragment() {
        val action = LoadingFragmentDirections.toEventsFragment()
        binding.root.findNavController()
            .navigate(action)
    }

    private fun goToViewFragment() {
        val action = LoadingFragmentDirections.toViewFragment()
        binding.root.findNavController()
            .navigate(action)
    }
}
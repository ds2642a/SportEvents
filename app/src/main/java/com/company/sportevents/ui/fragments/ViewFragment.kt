package com.company.sportevents.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.company.sportevents.R
import com.company.sportevents.databinding.FragmentViewBinding
import com.company.sportevents.ui.MainActivity


class ViewFragment : Fragment() {
    private lateinit var binding : FragmentViewBinding
    private var sharedPref : SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = FragmentViewBinding.bind(view)
        sharedPref = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val viewURL = sharedPref?.getString("viewURL", null)
        if (viewURL != null) {
            binding.view.settings.javaScriptEnabled = true
            binding.view.webViewClient = object : WebViewClient() {}
            binding.view.loadUrl(viewURL)
        }
    }
}
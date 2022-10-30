package com.company.sportevents.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.company.sportevents.R
import com.company.sportevents.databinding.FragmentEventPageBinding
import com.company.sportevents.ui.MainActivity

class EventPageFragment : Fragment() {
    private lateinit var binding : FragmentEventPageBinding
    private val args = EventPageFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = FragmentEventPageBinding.bind(view)

        val event = arguments?.let { args.fromBundle(it).event }

        context?.resources?.let {
            binding.eventImage.setImageResource(
                it.getIdentifier(event?.imageName, "drawable", context?.packageName)
            )
        }
        binding.eventName.text = event?.name
        binding.eventDate.text = event?.date
        binding.eventText.text = event?.text
    }
}
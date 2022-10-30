package com.company.sportevents.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.sportevents.R
import com.company.sportevents.data.model.Event
import com.company.sportevents.databinding.FragmentEventsBinding
import com.company.sportevents.ui.MainActivity
import com.company.sportevents.ui.adapters.EventsAdapter

class EventsFragment : Fragment() {
    private lateinit var binding : FragmentEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentEventsBinding.bind(view)

        val events = ArrayList<Event>()
        events.add(Event("event_01", "Graham Potter was booed before kick-off on his return to Amex Stadium", "29th October 2022", getString(R.string.event01Text)))
        events.add(Event("event_02", "Valtteri Bottas on his new bike race and climate concerns", "26 October 2022", getString(R.string.event02Text)))
        events.add(Event("event_03", "Audi to team up with Sauber for Formula 1 debut in 2026", "26 October 2022", getString(R.string.event03Text)))
        events.add(Event("event_04", "Lewis Hamilton & Fernando Alonso grab some of Red Bull’s limelight", "24 October 2022", getString(R.string.event04Text)))
        events.add(Event("event_05", "Former US President claims The Open wants to come back to Turnberry", "27 October 2022", getString(R.string.event05Text)))
        events.add(Event("event_06", "Tour de France Femmes 2023 route features Col du Tourmalet", "27 October 2022", getString(R.string.event06Text)))
        events.add(Event("event_07", "Runner-rapper Rio Mitcham targets gold medals & discs", "20 October 2022", getString(R.string.event07Text)))

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = EventsAdapter(events) { event: Event -> goToEventPageFragment(event) }
    }

    private fun goToEventPageFragment(event: Event) {
        val action = EventsFragmentDirections.toEventPageFragment(event)
        binding.root.findNavController()
            .navigate(action)
    }
}
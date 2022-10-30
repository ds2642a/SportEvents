package com.company.sportevents.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.company.sportevents.R
import com.company.sportevents.data.model.Event
import com.company.sportevents.databinding.ItemEventBinding

class EventsAdapter(private val events: List<Event>, private val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    lateinit var context : Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        return ViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]

        holder.eventImage.setImageResource(
            context.resources.getIdentifier(event.imageName, "drawable", context.packageName)
        )
        holder.eventName.text = event.name
        holder.eventDate.text = event.date
        holder.eventText.text = event.text

        holder.bind(event, onClick)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class ViewHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val eventImage : ImageView = binding.eventImage
        val eventName : TextView = binding.eventName
        val eventDate : TextView = binding.eventDate
        val eventText : TextView = binding.eventText

        fun bind(event: Event, onClick:(Event) -> Unit) {
            binding.root.setOnClickListener {
                onClick(event)
            }
        }
    }
}
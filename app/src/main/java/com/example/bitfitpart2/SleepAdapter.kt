package com.example.bitfitpart2

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class SleepAdapter(private val context: Context, private val sleepList: List<SleepEntity>) :
    RecyclerView.Adapter<SleepAdapter.SleepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sleep, parent, false)
        return SleepViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val sleepData = sleepList[position]
        Log.d("Debug", "Binding item at position $position: $sleepData")
        holder.bind(sleepData)
    }

    override fun getItemCount() = sleepList.size

    inner class SleepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val hoursTextView: TextView = itemView.findViewById(R.id.hoursTextView)


        fun bind(sleepData: SleepEntity) {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("MMM d yyyy", Locale.getDefault())
            val parsedDate = inputDateFormat.parse(sleepData.date)
            val formattedDate = outputDateFormat.format(parsedDate)

            dateTextView.text = sleepData.date
            dateTextView.setTypeface(null, Typeface.BOLD)
            hoursTextView.text = "${sleepData.hoursOfSleep} hours"
            hoursTextView.setTypeface(null, Typeface.BOLD)
        }
    }
}
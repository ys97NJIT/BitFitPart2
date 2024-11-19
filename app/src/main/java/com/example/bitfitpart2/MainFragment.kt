package com.example.bitfitpart2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitfitpart2.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var sleepAdapter: SleepAdapter
    private val sleepList = mutableListOf<SleepEntity>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMainBinding.bind(view)
        Log.d("MainFragment", "Binding initialized")

        // RecyclerView Setting
        sleepAdapter = SleepAdapter(requireContext(), sleepList)
        binding.sleepRecyclerView.adapter = sleepAdapter
        binding.sleepRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("MainFragment", "RecyclerView initialized.")

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.sleepRecyclerView.addItemDecoration(dividerItemDecoration)

        observeDatabase()

        binding.addNewSleepButton.setOnClickListener {
            val intent = Intent(requireContext(), AddSleepActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeDatabase() {
        lifecycleScope.launch {
            Log.d("MainFragment", "Observing database.")
            (requireActivity().application as BitFitApplication).db.sleepDao().getAll().collect { databaseList ->
                Log.d("MainFragment", "Database list size: ${databaseList.size}")
                sleepList.clear()
                sleepList.addAll(databaseList)
                sleepAdapter.notifyDataSetChanged()
                Log.d("MainFragment", "Adapter notified with ${sleepList.size} items.")

                val averageSleep = if (sleepList.isNotEmpty()) {
                    sleepList.sumOf { it.hoursOfSleep }.toDouble() / sleepList.size
                } else {
                    0.0
                }
                Log.d("MainFragment", "Average sleep calculated: $averageSleep")
                binding.averageSleepTextView.text = "Average hours of sleep: %.1f hours".format(averageSleep)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.bitfitpart2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bitfitpart2.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDashboardBinding.bind(view)

        lifecycleScope.launch {
            val sleepDao = (requireActivity().application as BitFitApplication).db.sleepDao()

            // 데이터베이스에서 데이터 가져오기
            sleepDao.getAll().collect { sleepData ->
                if (sleepData.isNotEmpty()) {
                    setupBarChart(sleepData)
                }
            }
        }
    }

    private fun setupBarChart(sleepData: List<SleepEntity>) {
        // BarEntry 리스트 생성
        val barEntries = sleepData.mapIndexed { index, sleep ->
            BarEntry(index.toFloat(), sleep.hoursOfSleep.toFloat())
        }

        // X축 레이블에 표시할 날짜 리스트 생성 (년도 제외)
        val dates = sleepData.map { it.date.substring(5) } // MM-DD 형식으로 변환

        // BarDataSet 설정
        val barDataSet = BarDataSet(barEntries, "Hours of Sleep")
        barDataSet.color = requireContext().getColor(R.color.colorAccent) // 연두색 계열 색상 설정
        barDataSet.valueTextSize = 12f

        // BarData 설정
        val barData = BarData(barDataSet)
        binding.barChart.data = barData

        // BarChart 설정
        with(binding.barChart) {
            description.isEnabled = false // 설명 비활성화
            setFitBars(true) // 막대 맞추기
            animateY(1000) // Y축 애니메이션

            // X축 설정
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM // X축을 하단에 표시
                valueFormatter = XAxisValueFormatter(dates) // 커스텀 레이블 적용
                granularity = 1f // 한 칸씩 이동
                textSize = 12f
                labelRotationAngle = -45f // 레이블 회전
            }

            axisLeft.axisMinimum = 0f // Y축 최소값 설정
            axisRight.isEnabled = false // 오른쪽 Y축 숨기기
            invalidate() // 그래프 새로고침
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // X축에 날짜를 표시하기 위한 커스텀 ValueFormatter
    class XAxisValueFormatter(private val dates: List<String>) : com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index in dates.indices) dates[index] else ""
        }
    }
}

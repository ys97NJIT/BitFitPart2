package com.example.bitfitpart2

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.bitfitpart2.databinding.ActivityAddSleepBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddSleepActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSleepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSleepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sleep data record 버튼 클릭 리스너
        binding.recordSleepButton.setOnClickListener {
            saveSleepData()
        }
    }

    private fun saveSleepData() {
        // 사용자가 입력한 값을 가져옴
        val date = binding.dateEditText.text.toString()
        val hoursOfSleep = binding.hoursEditText.text.toString().toIntOrNull()

        // 입력 값 유효성 검사
        if (date.isNotBlank() && hoursOfSleep != null) {
            val sleepEntity = SleepEntity(date = date, hoursOfSleep = hoursOfSleep)

            // Coroutine을 사용하여 비동기적으로 데이터 삽입
            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getInstance(applicationContext)
                db.sleepDao().insert(sleepEntity)
                // 데이터 삽입 후 액티비티 종료
                runOnUiThread {
                    Toast.makeText(this@AddSleepActivity, "Sleep data recorded", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Please enter valid date and hours of sleep", Toast.LENGTH_SHORT).show()
        }
    }
}

package com.example.bitfitpart2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bitfitpart2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("Debug", "ContentView successfully loaded.")

        try {
            // NavHostFragment 동적 추가 또는 가져오기
            var navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            if (navHostFragment == null) {
                Log.d("Debug", "NavHostFragment not found. Adding manually.")
                navHostFragment = NavHostFragment.create(R.navigation.nav_graph)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, navHostFragment)
                    .commitNow()
            }

            // NavController 설정
            navController = navHostFragment.navController
            binding.bottomNavigationView.setupWithNavController(navController)
            Log.d("Debug", "NavController successfully set up.")

            // Fragment 로드 확인
            supportFragmentManager.executePendingTransactions()
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
            if (currentFragment == null) {
                navController.navigate(R.id.mainFragment) // MainFragment로 이동
                Log.d("Debug", "Navigated to MainFragment.")
            } else {
                Log.d("Debug", "Current Fragment: ${currentFragment::class.java.simpleName}")
            }
        } catch (e: Exception) {
            Log.e("Debug", "Error initializing NavHostFragment or NavController: ${e.message}")
            e.printStackTrace()
        }
    }
}

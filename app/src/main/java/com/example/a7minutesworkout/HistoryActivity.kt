package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding:ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)

        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()
        getAllDates(dao)

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun getAllDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllHistory().collect{
                if(it.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.INVISIBLE

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val dates = ArrayList<String>()
                    for(date in it){
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(dates)
                    binding?.rvHistory?.adapter = historyAdapter
                }else{
                    binding?.tvHistory?.visibility = View.INVISIBLE
                    binding?.rvHistory?.visibility = View.INVISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
            }
        }
    }
}
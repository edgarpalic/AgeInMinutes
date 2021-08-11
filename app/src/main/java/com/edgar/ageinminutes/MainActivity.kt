package com.edgar.ageinminutes

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.edgar.ageinminutes.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var minBool = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.btnMinutes.setOnClickListener {
            binding.tvSecondTextSwap.text = getString(R.string.age_in_minutes)
            binding.btnMinutes.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnDays.setTextColor(Color.parseColor("#FFC8C8C8"))
            minBool = true
        }

        binding.btnDays.setOnClickListener {
            binding.tvSecondTextSwap.text = getString(R.string.age_in_days)
            binding.btnMinutes.setTextColor(Color.parseColor("#FFC8C8C8"))
            binding.btnDays.setTextColor(Color.parseColor("#FFFFFFFF"))
            minBool = false
        }

        binding.btnDatePicker.setOnClickListener {
            clickDatePicker()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH) + 1
        val dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            var selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

            if (selectedMonth < 9) {
                selectedDate = if (selectedDay < 10) {
                    "0$selectedDay/0${selectedMonth + 1}/$selectedYear"
                } else {
                    "$selectedDay/0${selectedMonth + 1}/$selectedYear"
                }
            } else {
                "$selectedDay/${selectedMonth + 1}/$selectedYear"
            }

            binding.tvSelectedDate.text = selectedDate

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val theDate = sdf.parse(selectedDate)
            val selectedDateInMinutes = theDate!!.time / 600000
            val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
            val currentDateInMinutes = currentDate!!.time / 600000
            val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes
            val daysOld = differenceInMinutes / 144
            when (minBool) {
                true -> binding.tvSelectedDateInMinutes.text = differenceInMinutes.toString()
                else -> binding.tvSelectedDateInMinutes.text = daysOld.toString()
            }
            binding.btnMinutes.setOnClickListener {
                minBool = true
                binding.tvSecondTextSwap.text = getString(R.string.age_in_minutes)
                binding.tvSelectedDateInMinutes.text = differenceInMinutes.toString()
                binding.btnMinutes.setTextColor(Color.parseColor("#FFFFFFFF"))
                binding.btnDays.setTextColor(Color.parseColor("#FFC8C8C8"))
            }

            binding.btnDays.setOnClickListener {
                minBool = false
                binding.tvSecondTextSwap.text = getString(R.string.age_in_days)
                binding.tvSelectedDateInMinutes.text = daysOld.toString()
                binding.btnMinutes.setTextColor(Color.parseColor("#FFC8C8C8"))
                binding.btnDays.setTextColor(Color.parseColor("#FFFFFFFF"))
            }
        }, year, month, dayOfMonth)

        dpd.datePicker.maxDate = Date().time - 86400000
        dpd.show()
    }

}
package com.example.lab_week_10

import android.os.Bundle
import android.widget.Toast
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.*
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var db: TotalDatabase
    private lateinit var viewModel: TotalViewModel

    companion object {
        const val ID: Long = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-db"
        ).allowMainThreadQueries().build()

        viewModel = ViewModelProvider(this)[TotalViewModel::class.java]

        initDatabase()
        setupViewModel()
        setupButtons()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun setupViewModel() {
        viewModel.total.observe(this) {
            findViewById<TextView>(R.id.text_total).text = "Total: $it"
        }
    }

    private fun initDatabase() {
        val existing = db.totalDao().getTotal(ID)

        if (existing == null) {
            // Jika data belum ada → buat awal
            val first = Total(
                id = ID,
                total = TotalObject(
                    value = 0,
                    date = Date().toString()
                )
            )
            db.totalDao().insert(first)
        } else {
            // Jika data ada → update ViewModel
            viewModel.setTotal(existing.total.value)
        }
    }

    override fun onStart() {
        super.onStart()

        val data = db.totalDao().getTotal(ID)
        if (data != null) {
            Toast.makeText(
                this,
                "Last updated: ${data.total.date}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onPause() {
        super.onPause()

        val updated = Total(
            id = ID,
            total = TotalObject(
                value = viewModel.total.value ?: 0,
                date = Date().toString()
            )
        )

        db.totalDao().update(updated)
    }
}

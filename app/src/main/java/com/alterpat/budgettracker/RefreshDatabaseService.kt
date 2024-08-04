package com.alterpat.budgettracker

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RefreshDatabaseService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            refreshDatabase()

            val broadcastIntent = Intent("com.alterpat.budgettracker.ACTION_REFRESH")
            sendBroadcast(broadcastIntent)
        }
        return START_NOT_STICKY
    }

    private fun refreshDatabase() {
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "transactions").build()
        db.transactionDao().getAll() // Simulate a database refresh operation
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

package com.alterpat.budgettracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.alterpat.budgettracker.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.alterpat.budgettracker.MessageDialogFragment

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction : Transaction
    private lateinit var binding: ActivityDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.descriptionInput.setText(transaction.description)


        binding.rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.labelInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if(it!!.count() > 0)
                binding.labelLayout.error = null
        }

        binding.amountInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if(it!!.count() > 0)
                binding.amountLayout.error = null
        }

        binding.descriptionInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
        }

        binding.updateBtn.setOnClickListener {
            val label =  binding.labelInput.text.toString()
            val description =  binding.descriptionInput.text.toString()
            val amount =  binding.amountInput.text.toString().toDoubleOrNull()

            if(label.isEmpty())
                showErrorDialog("Please enter a valid amount")

            else if(amount == null)
                showErrorDialog("Please enter a valid amount")
            else {
                val transaction  = Transaction(transaction.id, label, amount, description)
                update(transaction)
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }

    private fun showErrorDialog(message: String) {
        val dialog = MessageDialogFragment.newInstance(message)
        dialog.show(supportFragmentManager, "errorDialog")
    }

}

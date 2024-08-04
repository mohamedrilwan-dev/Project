package com.alterpat.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.alterpat.budgettracker.databinding.ActivityAddTransactionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.alterpat.budgettracker.MessageDialogFragment

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.labelInput.addTextChangedListener {
            if(it!!.count() > 0)
                binding.labelLayout.error = null
        }

        binding.amountInput.addTextChangedListener {
            if(it!!.count() > 0)
                binding.amountLayout.error = null
        }

        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()
            val isExpense = binding.transactionTypeGroup.checkedRadioButtonId == binding.expenseRadioButton.id

            if(label.isEmpty())
                showErrorDialog("Please enter a valid label")

            else if(amount == null)
                showErrorDialog("Please enter a valid amount")
            else {
                val adjustedAmount = if (isExpense) -amount else amount
                val transaction  =Transaction(0, label, adjustedAmount, description)
                insert(transaction)
            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }

    private fun showErrorDialog(message: String) {
        val dialog = MessageDialogFragment.newInstance(message)
        dialog.show(supportFragmentManager, "errorDialog")
    }

}
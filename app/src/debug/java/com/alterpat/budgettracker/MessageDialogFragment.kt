package com.alterpat.budgettracker

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MessageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Input Error !")
        builder.setMessage(arguments?.getString("errorMessage"))
        builder.setPositiveButton("OK") { _, _ -> dismiss() }

        return builder.create()
    }

    companion object {
        fun newInstance(errorMessage: String): MessageDialogFragment {
            val args = Bundle()
            args.putString("errorMessage", errorMessage)
            val fragment = MessageDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
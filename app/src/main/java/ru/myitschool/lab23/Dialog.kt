package ru.myitschool.lab23

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class Dialog : DialogFragment() {

    private lateinit var typeSpinner: Spinner
    private lateinit var amountEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.fragment_dialog, null)
            builder.setView(view)

            typeSpinner = view.findViewById(R.id.type_spinner)
            amountEditText = view.findViewById(R.id.expense_amount_edit_text)

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.choice_arr,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinner.adapter = adapter

            builder.setPositiveButton(R.string.add_fab_button) { dialog, _ ->
                val type = typeSpinner.selectedItem.toString()
                val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    val currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                    val newItem = ExpenseItem(type, currentDate, amount)
                    (activity as MainActivity).addItem(newItem)
                }
                dialog.dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

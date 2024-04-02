package com.example.harshamoduleproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class TipFragment : Fragment() {

    // Declare variables
    private lateinit var etBillAmount: EditText
    private lateinit var etTipPercentage: EditText
    private lateinit var etNumberOfPersons: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnCalculate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tip, container, false)

        // Initialize variables
        etBillAmount = rootView.findViewById(R.id.etBillAmount)
        etTipPercentage = rootView.findViewById(R.id.etTipPercentage)
        etNumberOfPersons = rootView.findViewById(R.id.etNumberOfPersons)
        tvResult = rootView.findViewById(R.id.tvResult)
        btnCalculate = rootView.findViewById(R.id.btnCalculate)

        // Set a click listener for the calculate button
        btnCalculate.setOnClickListener {
            if (etBillAmount.text.isEmpty() || etTipPercentage.text.isEmpty() || etNumberOfPersons.text.isEmpty()) {
                // Show a message to enter proper data
                tvResult.text = "Please enter all the required data to calculate."
            } else {
                calculateTip()
            }
        }

        return rootView
    }

    // Method to calculate the tip
    private fun calculateTip() {
        // Get the input values from the EditText fields
        val billAmount = etBillAmount.text.toString().toDouble()
        val tipPercentage = etTipPercentage.text.toString().toDouble()
        val numberOfPersons = etNumberOfPersons.text.toString().toInt()

        // Calculate the tip amount
        val tipAmount = (billAmount * tipPercentage) / 100

        // Calculate the total amount (bill + tip)
        val totalAmount = billAmount + tipAmount

        // Calculate the split amount per person
        val splitAmount = totalAmount / numberOfPersons

        // Display the result in the TextView
        tvResult.text = String.format(
            "Tip:- INR: %.2f\n\n Total:- INR: %.2f\n\n Split (per person):- INR: %.2f",
            tipAmount, totalAmount, splitAmount
        )
    }
}

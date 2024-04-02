package com.example.harshamoduleproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class AdminReservationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_reservation, container, false)

        // Set up click listeners for buttons
        val pendingButton = view.findViewById<Button>(R.id.pendingButton)
        val checkInButton = view.findViewById<Button>(R.id.checkInButton)
        val checkOutButton = view.findViewById<Button>(R.id.checkOutButton)

        pendingButton.setOnClickListener {
            replaceFragment(PendingFragment())
        }

        checkInButton.setOnClickListener {
            replaceFragment(CheckInFragment())
        }

        checkOutButton.setOnClickListener {
            replaceFragment(CheckOutFragment())
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}

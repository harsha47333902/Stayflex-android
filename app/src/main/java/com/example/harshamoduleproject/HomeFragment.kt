package com.example.harshamoduleproject
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private lateinit var roomsContainer: LinearLayout
    private val roomsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Rooms")
    private lateinit var userId: String

    companion object {
        fun newInstance(userId: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Retrieve userId from arguments
        userId = arguments?.getString("userId") ?: ""

        roomsContainer = view.findViewById(R.id.roomsContainer)

        // Fetch rooms data
        fetchRoomsData()

        return view
    }

    private fun fetchRoomsData() {
        roomsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Rooms", "No data found for rooms")
                    return
                }

                for (roomSnapshot in snapshot.children) {
                    val roomData = roomSnapshot.getValue(AddRoomData::class.java)

                    roomData?.let {
                        val imageUrl = roomData.imageUrl
                        val roomType = roomData.roomtype
                        val price = roomData.price
                        val description= roomData.description

                        // Create room item view
                        val roomItemView = createRoomItemView(imageUrl, roomType, price, description)
                        roomsContainer.addView(roomItemView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Rooms", "Database error: ${error.message}")
            }
        })
    }

    private fun createRoomItemView(imageUrl: String?, roomType: String?, price: String?, description: String?): View {
        val roomItemView = layoutInflater.inflate(R.layout.item_room, null)
        val roomImageView = roomItemView.findViewById<ImageView>(R.id.roomImageView)
        val roomTypeTextView = roomItemView.findViewById<TextView>(R.id.roomTypeTextView)
        val priceTextView = roomItemView.findViewById<TextView>(R.id.priceTextView)
        val descriptionText = roomItemView.findViewById<TextView>(R.id.descriptionText)

        // Load image using Picasso
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(roomImageView)
        }

        // Set room type and price
        roomTypeTextView.text = roomType ?: ""
        priceTextView.text = price ?: ""
        descriptionText.text = description?: ""

        // Set click listener to start details activity
        roomItemView.setOnClickListener {
            val intent = Intent(activity, CostumerroomdetailsActivity::class.java).apply {
                putExtra("image_url", imageUrl)
                putExtra("room_type", roomType)
                putExtra("price", price)
                putExtra("description", description)
                putExtra("userId", userId) // Pass userId to CostumerroomdetailsActivity
            }
            startActivity(intent)
        }


        return roomItemView
    }
}

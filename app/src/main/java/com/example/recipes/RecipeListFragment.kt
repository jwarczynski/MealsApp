package com.example.recipes

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecipeListFragment : ListFragment() {

    interface Listener {
        fun itemClicked(recipeName: String)
    }

    private var listener: Listener? = null
    val names = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDataFromFirebaseAndSetAdapter(inflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onListItemClick(listView: ListView, itemView:View, position:Int, id: Long) {
        val recipeName = names[id.toInt()]
        listener?.itemClicked(recipeName)
    }

    private fun getDataFromFirebaseAndSetAdapter(inflater: LayoutInflater) {
        val database = FirebaseDatabase.getInstance("https://put-am-recipe-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("recipes")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                names.clear()
                for (c in snapshot.children) {
                    names.add(c.key.toString())
                }
                val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                    inflater.context, R.layout.simple_list_item_1, names as List<String?>
                )
                listAdapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("Info_a", "Failed to read value.", error.toException())
            }
        })
    }
}
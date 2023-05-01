package com.example.recipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TabFragment(private var category1: Boolean=false) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (savedInstanceState != null) {
            category1 = savedInstanceState.getBoolean("category1")
        }
        val cocktailRecycler = inflater.inflate(R.layout.fragment_tab, container, false) as RecyclerView
        val layoutManager = GridLayoutManager(context, 2)
        getDataFromFirebaseAndSetAdapter(cocktailRecycler)
        cocktailRecycler.layoutManager = layoutManager
        return cocktailRecycler
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("category1", category1)
    }

    private fun getDataFromFirebaseAndSetAdapter(cocktailRecycler: RecyclerView) {
        val database = FirebaseDatabase.getInstance("https://put-am-recipe-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("recipes")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val names = mutableListOf<String>()
                val imagesUrl = mutableListOf<String?>()
                for (c in snapshot.children) {
                    if((c.child("polishDish").value != true) == category1) {
                        names.add(c.key.toString())
                        imagesUrl.add(c.child("image").value?.toString())
                    }
                }
                val adapter = CaptionedImagesAdapter(names, imagesUrl)
                adapter.setListener(object : CaptionedImagesAdapter.Listener {
                    override fun onClick(name: String) {
                        val intent = Intent(activity, RecipeDetailActivity::class.java)
                        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_NAME, name)
                        activity?.startActivity(intent)
                    }
                })
                cocktailRecycler.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("Info_a", "Failed to read value.", error.toException())
            }
        })
    }
}

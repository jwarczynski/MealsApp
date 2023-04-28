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

class Tab1Fragment : Fragment() {

    val names = mutableListOf<String>()
    val imagesUrl = mutableListOf<String?>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val cocktailRecycler = inflater.inflate(R.layout.fragment_tab1, container, false) as RecyclerView
        getDataFromFirebaseAndSetAdapter(cocktailRecycler)
        val layoutManager = GridLayoutManager(context, 2)
        cocktailRecycler.layoutManager = layoutManager
        return cocktailRecycler
    }

    private fun getDataFromFirebaseAndSetAdapter(cocktailRecycler: RecyclerView) {
        val database = FirebaseDatabase.getInstance("https://put-am-recipe-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("recipes")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                names.clear()
                imagesUrl.clear()
                for (c in snapshot.children) {
                    if(c.child("polishDish").value != true) {
                        names.add(c.key.toString())
                        imagesUrl.add(c.child("image").value?.toString())
                    }
                }
                val adapter = CaptionedImagesAdapter(names, imagesUrl)
                cocktailRecycler.adapter = adapter
                adapter.setListener(object : CaptionedImagesAdapter.Listener {
                    override fun onClick(name: String) {
                        val intent = Intent(activity, RecipeDetailActivity::class.java)
                        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_NAME, name)
                        activity?.startActivity(intent)
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("Info_a", "Failed to read value.", error.toException())
            }
        })
    }
}


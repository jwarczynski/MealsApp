package com.example.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class RecipeDetailFragment : Fragment() {
    private var mealId: Long = 0

    fun setMeal(id: Long) {
        this.mealId = id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mealId = savedInstanceState.getLong("mealId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedinstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        val view = view
        if (view != null) {
            val title = view.findViewById<View>(R.id.textTitle) as TextView
            val meal: Meal = Meal.meals[mealId.toInt()]
            title.text = meal.getName()
            val description = view.findViewById<View>(R.id.textDescription) as TextView
            description.text = meal.getRecipe()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong("mealId", mealId)
    }






}
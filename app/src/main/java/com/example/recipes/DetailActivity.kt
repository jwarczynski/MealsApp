package com.example.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RECIPE_ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val frag = supportFragmentManager.findFragmentById(R.id.detail_frag) as RecipeDetailFragment
        val recipeId = intent.extras?.getInt(EXTRA_RECIPE_ID)?.toLong()
        if (recipeId != null) {
            frag.setMeal(recipeId)
        }
    }



}
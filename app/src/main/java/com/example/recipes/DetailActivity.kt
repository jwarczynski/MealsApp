package com.example.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RECIPE_NAME = "recipeName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val frag = supportFragmentManager.findFragmentById(R.id.detail_frag) as RecipeDetailFragment
        val recipeName = intent.extras?.getString(EXTRA_RECIPE_NAME)
        if (recipeName != null) {
            frag.setMeal(recipeName)
        }
    }



}
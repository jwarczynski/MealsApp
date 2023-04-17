package com.example.recipes

import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso

class Recipe(private val recipeData: DataSnapshot) {
    private var selectedNumberOfServings: Int = 0

    init {
        selectedNumberOfServings = getDefaultNumberOfServings()
    }

    fun getName(): String {
        return recipeData.key.toString()
    }

    fun getListIngredientsString(): String {
        val scale: Float = selectedNumberOfServings.toFloat() / getDefaultNumberOfServings()
        var text = ""
        val ingredients = recipeData.child("ingredients")
        for (ingredient in ingredients.children) {
            val name = ingredient.key ?: ""
            val number =
                (ingredient.child("number").getValue(Int::class.java)?.times(scale))?.toInt()
                    .toString() ?: ""
            val unit = ingredient.child("unit").value?.toString() ?: ""
            text += " - $name \t- $number $unit\n"
        }
        return text
    }

    fun getPreparation(): String {
        return recipeData.child("preparation").value?.toString() ?: ""
    }

    fun getExpectedTime(): String {
        val txt = recipeData.child("time").value?.toString() ?: "???"
        return "$txt minut"
    }

    fun getNumberOfServings(): String {
        return selectedNumberOfServings.toString()
    }

    private fun getDefaultNumberOfServings(): Int {
        return recipeData.child("numberOfServings").getValue(Int::class.java) ?: 1
    }

    fun addNumberOfServings(x: Int): Boolean {
        if (selectedNumberOfServings + x > 0) {
            selectedNumberOfServings += x
            return true
        }
        return false
    }

    fun setImageInImageView(imgView: ImageView) {
        val link = recipeData.child("image").value?.toString()
        if (link != null) Picasso.get().load(link).into(imgView)
    }
}


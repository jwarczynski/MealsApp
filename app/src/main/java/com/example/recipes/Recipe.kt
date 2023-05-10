package com.example.recipes

import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class Recipe(private val recipeData: DataSnapshot) {
    private var selectedNumberOfServings: Int = 0

    init {
        selectedNumberOfServings = getDefaultNumberOfServings()
    }

    fun setNumberOfServings(numberOfServings: Int) {
        selectedNumberOfServings = numberOfServings
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
            val number = ingredient.child("number").getValue(Float::class.java)?.times(scale)
            val number2  = (number?.times(100) ?: 0f).roundToInt() / 100f

            val unit = ingredient.child("unit").value?.toString() ?: ""
            text += if(number == number2.roundToInt().toFloat()) " - $name \t- ${number2.toInt()} $unit\n"
            else " - $name \t- $number2 $unit\n"
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

    fun getIntNumberOfServings(): Int {
        return selectedNumberOfServings
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

    fun getListTimers(): MutableList<Pair<String, Int>> {
        val listTimers: MutableList<Pair<String, Int>> = mutableListOf()
        val timers = recipeData.child("timers")
        for (timer in timers.children) {
            val name: String = timer.key ?: ""
            val time: Int = timer.getValue(Int::class.java) ?: 0
            listTimers.add(Pair(name, time))
        }
        return listTimers
    }
}


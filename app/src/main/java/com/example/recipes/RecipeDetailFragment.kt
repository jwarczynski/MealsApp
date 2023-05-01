package com.example.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RecipeDetailFragment : Fragment() {
    // selectedMealName - null jak nie wybrano nazwy posiłku || String z nazwą wybranego posiłku
    private var selectedMealName: String? = null
    private var selectedNumberOfServings: Int = -1
    private var recipe: Recipe? = null

    fun setMeal(mealName: String) {
        this.selectedMealName = mealName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            selectedMealName = savedInstanceState.getString("selectedMealName")
            selectedNumberOfServings = savedInstanceState.getInt("selectedNumberOfServings")
        }
        onShowRecipeFromFirebase(savedInstanceState == null)
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
            val btnServingsPlus = view.findViewById(R.id.btnServingsMore) as Button
            val btnServingsMinus = view.findViewById(R.id.btnServingsLess) as Button
            val fab = view.findViewById(R.id.fab) as FloatingActionButton
            btnServingsPlus.setOnClickListener {
                if(recipe?.addNumberOfServings(1) == true) showRecipe()
            }
            btnServingsMinus.setOnClickListener {
                if(recipe?.addNumberOfServings(-1) == true) showRecipe()
            }
            fab.setOnClickListener{
                Snackbar.make(view, "Kliknięto: 'Udostępnij składniki'", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("selectedMealName", selectedMealName)
        savedInstanceState.putInt("selectedNumberOfServings", recipe?.getIntNumberOfServings() ?: -1)
    }

    private fun onShowRecipeFromFirebase(instanceIsNull: Boolean) {
        val database = FirebaseDatabase.getInstance("https://put-am-recipe-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("recipes")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(recipesFromDb: DataSnapshot) {
                for (recipeFromDb in recipesFromDb.children) {
                    if(selectedMealName == null || recipeFromDb.key.toString() == selectedMealName) {
                        recipe = Recipe(recipeFromDb)
                        if(instanceIsNull) addTimers(recipe?.getListTimers())
                        else recipe?.setNumberOfServings(selectedNumberOfServings)
                        break
                    }
                }
                showRecipe()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("Info_a", "Failed to read value.", error.toException())
            }
        })
    }

    private fun showRecipe() {
        val title = view?.findViewById<View>(R.id.textTitle) as TextView
        val imgRecipe = view?.findViewById<ImageView>(R.id.imgRecipe) as ImageView
        val textViewNumberOfServings = view?.findViewById<View>(R.id.textNumberOfServings) as TextView
        val textViewIngredients = view?.findViewById<View>(R.id.textIngredients) as TextView
        val textViewPreparation = view?.findViewById<View>(R.id.textPreparation) as TextView
        val textViewExpectedTime = view?.findViewById<View>(R.id.textExpectedTime) as TextView

        title.text = recipe?.getName()
        textViewNumberOfServings.text = recipe?.getNumberOfServings()
        textViewExpectedTime.text = recipe?.getExpectedTime()
        textViewIngredients.text = recipe?.getListIngredientsString()
        textViewPreparation.text = recipe?.getPreparation()
        recipe?.setImageInImageView(imgRecipe)
    }

    private fun addTimers(listTimers: MutableList<Pair<String, Int>>?) {
        val ft = childFragmentManager.beginTransaction();
        if (listTimers != null) {
            for(timer in listTimers) {
                val timerFragment = TimerFragment()
                timerFragment.initTimer(timer.first, timer.second)
                ft.add(R.id.timer_container, timerFragment);
            }
        }
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit()
    }
}
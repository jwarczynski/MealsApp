package com.example.recipes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity(), RecipeListFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagerAdapter = SectionsPagerAdapter(supportFragmentManager, this)
        val pager = findViewById<View>(R.id.pager) as ViewPager
        pager.adapter = pagerAdapter

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(pager)
    }

    override fun itemClicked(recipeName: String) {
        val fragmentContainer = findViewById<View>(R.id.fragment_container)
        if (fragmentContainer != null) {
            val details = RecipeDetailFragment()
            val ft = supportFragmentManager.beginTransaction()
            details.setMeal(recipeName)
            ft.replace(R.id.fragment_container, details)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_RECIPE_NAME, recipeName)
            startActivity(intent)
        }
    }
}

private class SectionsPagerAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return TopFragment()
            1 -> return Tab1Fragment()
            2 -> return RecipeListFragment()
            else -> TopFragment()
        }
    }


    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return context.resources.getText(R.string.home_tab)
            1 -> return context.resources.getText(R.string.kat1_tab)
            2 -> return context.resources.getText(R.string.kat2_tab)
        }
        return null
    }

}

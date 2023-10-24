package com.example.ul_todo_android_app

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ul_todo_android_app.constants.StaticTexts
import com.example.ul_todo_android_app.fragments.BadHabitsFragment
import com.example.ul_todo_android_app.fragments.GoodHabitsFragment
import com.google.android.material.tabs.TabLayout

class GuideActivity : AppCompatActivity() {
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
            }
        }

    private var toolBar: androidx.appcompat.widget.Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        toolBar = findViewById(R.id.toolbar)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(GoodHabitsFragment(), "Good Habits")
        adapter.addFragment(BadHabitsFragment(), "Bad Habits")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        toolBar?.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.whiteSmoke))

        actionBar()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Check if the orientation changed to landscape
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape) // Use landscape layout
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_guide) // Use portrait layout
            recreate()
        }
    }

    private fun initUI() {
    }

    private fun actionBar() {
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // defining action bar properties
        actionBar?.title = StaticTexts.GUIDE

        // navigating back functionality
        toolBar?.setNavigationOnClickListener {
            val intent = Intent(applicationContext, ToDoActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()
    private val fragmentTitles = mutableListOf<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        fragmentTitles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }
}

